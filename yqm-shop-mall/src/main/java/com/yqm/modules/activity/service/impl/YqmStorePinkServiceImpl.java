/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.OrderInfoEnum;
import com.yqm.enums.PinkEnum;
import com.yqm.modules.activity.domain.YqmStoreCombination;
import com.yqm.modules.activity.domain.YqmStorePink;
import com.yqm.modules.activity.service.YqmStoreCombinationService;
import com.yqm.modules.activity.service.YqmStorePinkService;
import com.yqm.modules.activity.service.YqmStoreVisitService;
import com.yqm.modules.activity.service.dto.PinkAllDto;
import com.yqm.modules.activity.service.dto.PinkDto;
import com.yqm.modules.activity.service.dto.PinkUserDto;
import com.yqm.modules.activity.service.dto.YqmStorePinkDto;
import com.yqm.modules.activity.service.dto.YqmStorePinkQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreCombinationMapper;
import com.yqm.modules.activity.service.mapper.YqmStorePinkMapper;
import com.yqm.modules.activity.vo.PinkInfoVo;
import com.yqm.modules.activity.vo.YqmStoreCombinationQueryVo;
import com.yqm.modules.activity.vo.YqmStorePinkQueryVo;
import com.yqm.modules.cart.domain.YqmStoreCart;
import com.yqm.modules.cart.service.YqmStoreCartService;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.order.domain.YqmStoreOrder;
import com.yqm.modules.order.service.YqmStoreOrderService;
import com.yqm.modules.order.vo.YqmStoreOrderQueryVo;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.user.vo.YqmUserQueryVo;
import com.yqm.utils.FileUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;


/**
 * @author weiximei
 * @date 2020-05-12
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStorePinkServiceImpl extends BaseServiceImpl<YqmStorePinkMapper, YqmStorePink> implements YqmStorePinkService {

	@Autowired
	private IGenerator generator;
	@Autowired
	private YqmStorePinkMapper yqmStorePinkMapper;
	@Autowired
	private YqmStoreCombinationMapper yqmStoreCombinationMapper;
	@Autowired
	private YqmStoreCombinationService combinationService;
	@Autowired
	private YqmStoreOrderService storeOrderService;
	@Autowired
	private YqmUserService userService;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private YqmUserService yqmUserService;

	@Autowired
	private YqmStoreCartService yqmStoreCartService;

	@Autowired
	private YqmStoreVisitService yqmStoreVisitService;


	/**
	 * 取消拼团
	 * @param uid 用户id
	 * @param cid 团购产品id
	 * @param pinkId 拼团id
	 */
	@Override
	public void removePink(Long uid, Long cid, Long pinkId) {
		YqmStorePink pink = this.lambdaQuery().eq(YqmStorePink::getId,pinkId)
				.eq(YqmStorePink::getUid,uid)
				.eq(YqmStorePink::getCid,cid)
				.eq(YqmStorePink::getKId,0)  //团长
				.eq(YqmStorePink::getIsRefund,OrderInfoEnum.PINK_REFUND_STATUS_0.getValue())
				.eq(YqmStorePink::getStatus,OrderInfoEnum.REFUND_STATUS_1.getValue())
				.gt(YqmStorePink::getStopTime,new Date())
				.one();
		if(pink == null) {
			throw new YqmShopException("拼团不存在或已经取消");
		}

		PinkUserDto pinkUserDto = this.getPinkMemberAndPinK(pink);
		List<YqmStorePink> pinkAll = pinkUserDto.getPinkAll();
		YqmStorePink pinkT = pinkUserDto.getPinkT();
		List<Long> idAll = pinkUserDto.getIdAll();
		List<Long> uidAll = pinkUserDto.getUidAll();
		int count = pinkUserDto.getCount();
		if(count < 1){
			this.pinkComplete(uidAll,idAll,uid,pinkT);
			throw new YqmShopException("拼团已完成，无法取消");
		}
		//如果团长取消拼团，团队还有人，就把后面的人作为下一任团长
		YqmStorePink nextPinkT = null;
		if(pinkAll.size() > 0){
			nextPinkT = pinkAll.get(0);
		}

		//先退团长的money
		storeOrderService.orderApplyRefund("","","拼团取消开团",pinkT.getOrderId(),pinkT.getUid());
		this.orderPinkFailAfter(pinkT.getUid(),pinkT.getId());

		//把团长下个人设置为团长
			if (Objects.nonNull(nextPinkT)) {
			LambdaQueryWrapper<YqmStorePink> wrapperO = new LambdaQueryWrapper<>();
			YqmStorePink storePinkO = new YqmStorePink();
			storePinkO.setKId(0L); //设置团长
			storePinkO.setStatus(OrderInfoEnum.PINK_STATUS_1.getValue());
			storePinkO.setStopTime(pinkT.getStopTime());
			storePinkO.setId(nextPinkT.getId());
			yqmStorePinkMapper.updateById(storePinkO);

			//原有团长的数据变更成新团长下面
			wrapperO.eq(YqmStorePink::getKId,pinkT.getId());
			YqmStorePink storePinkT = new YqmStorePink();
			storePinkT.setKId(nextPinkT.getId());
			yqmStorePinkMapper.update(storePinkT,wrapperO);

			//update order
			YqmStoreOrder storeOrder = new YqmStoreOrder();
			storeOrder.setPinkId(nextPinkT.getId());
			storeOrder.setId(nextPinkT.getId());
			storeOrderService.updateById(storeOrder);

		}
	}

	/**
	 * 计算还差几人拼团
	 * @param pink 拼团信息
	 * @return int
	 */
	@Override
	public int surplusPeople(YqmStorePink pink) {
		List<YqmStorePink> listT = new ArrayList<>();
		if(pink.getKId() > 0){ //团长存在
			listT = this.getPinkMember(pink.getKId());
		}else{
			listT = this.getPinkMember(pink.getId());
		}

		return pink.getPeople() - (listT.size() + 1);
	}




	/**
	 * 拼团明细
	 * @param id 拼团id
	 * @param uid 用户id
	 */
	@Override
	public PinkInfoVo pinkInfo(Long id, Long uid) {
		YqmStorePink pink = this.getPinkUserOne(id);
		if(ObjectUtil.isNull(pink)) {
			throw new YqmShopException("拼团不存在");
		}
		if( OrderInfoEnum.PINK_REFUND_STATUS_1.getValue().equals(pink.getIsRefund())){
			throw new YqmShopException("订单已退款");
		}

		int isOk = 0;//判断拼团是否完成
		int userBool = 0;//判断当前用户是否在团内  0未在 1在
		int pinkBool = 0;//判断拼团是否成功  0未 1是 -1结束

		PinkUserDto pinkUserDto = this.getPinkMemberAndPinK(pink);
		YqmStorePink pinkT = pinkUserDto.getPinkT();
		List<YqmStorePink> pinkAll = pinkUserDto.getPinkAll();
		List<Long> idAll = pinkUserDto.getIdAll();
		List<Long> uidAll = pinkUserDto.getUidAll();
		int count = pinkUserDto.getCount();

		if(count < 0) {
			count = 0;
		}
		if(OrderInfoEnum.PINK_STATUS_2.getValue().equals(pinkT.getStatus())){
			pinkBool = PinkEnum.PINK_BOOL_1.getValue();
			isOk = PinkEnum.IS_OK_1.getValue();
		}else if(pinkT.getStatus() == 3){
			pinkBool = PinkEnum.PINK_BOOL_MINUS_1.getValue();
			isOk = PinkEnum.IS_OK_0.getValue();
		}else{
			//组团完成
			if(count < 1){
				isOk = PinkEnum.IS_OK_1.getValue();
				pinkBool = this.pinkComplete(uidAll,idAll,uid,pinkT);
			}else{
				pinkBool = this.pinkFail(pinkAll,pinkT,pinkBool);
			}
		}

		//团员是否在团
		if(ObjectUtil.isNotNull(pinkAll)){
			for (YqmStorePink storePink : pinkAll) {
				if(storePink.getUid().equals(uid)) {
					userBool = PinkEnum.USER_BOOL_1.getValue();
				}
			}
		}
		//团长
		if(pinkT.getUid().equals(uid)) {
			userBool = PinkEnum.USER_BOOL_1.getValue();
		}

		YqmStoreCombinationQueryVo storeCombinationQueryVo = yqmStoreCombinationMapper.getCombDetail(pink.getCid());
		if(ObjectUtil.isNull(storeCombinationQueryVo)) {
			throw new YqmShopException("拼团不存在或已下架");
		}

		YqmUserQueryVo userInfo = userService.getYqmUserById(uid);
		YqmStoreOrder yqmStoreOrder = storeOrderService.getById(pink.getOrderIdKey());
		YqmStoreCart yqmStoreCart = yqmStoreCartService.getById(yqmStoreOrder.getCartId());
		//拼团访问量
		yqmStoreCombinationMapper.incBrowseNum(pink.getPid());
		//拼团访客人数
		yqmStoreVisitService.addStoreVisit(uid, pink.getPid());
		return PinkInfoVo.builder()
				.count(count)
				.currentPinkOrder(this.getCurrentPinkOrderId(id,uid))
				.isOk(isOk)
				.pinkAll(this.handPinkAll(pinkAll))
				.pinkBool(pinkBool)
				.pinkT(this.handPinkT(pinkT))
				.storeCombination(storeCombinationQueryVo)
				.userBool(userBool)
				.userInfo(userInfo)
				.uniqueId(yqmStoreCart.getProductAttrUnique())
				.build();

	}



	/**
	 * 返回正在拼团的人数
	 * @param id 拼团id
	 * @return int
	 */
	@Override
	public int pinkIngCount(Long id) {
		return this.lambdaQuery()
				.eq(YqmStorePink::getId,id)
				.eq(YqmStorePink::getStatus,OrderInfoEnum.PINK_STATUS_1.getValue())
				.count();
	}


	/**
	 * 创建拼团
	 * @param order 订单
	 */
	@Override
	public void createPink(YqmStoreOrderQueryVo order) {
		YqmStoreCombination storeCombination = combinationService.getById(order.getCombinationId());
		order = storeOrderService.handleOrder(order);
		YqmStoreCart storeCart = yqmStoreCartService.getById(order.getCartId());
		int pinkCount = yqmStorePinkMapper.selectCount(Wrappers.<YqmStorePink>lambdaQuery()
				.eq(YqmStorePink::getOrderId,order.getOrderId()));
		if(pinkCount > 0) {
			return;
		}
		if(storeCombination != null){
			YqmStorePink  storePink = YqmStorePink.builder()
					.uid(order.getUid())
					.orderId(order.getOrderId())
					.orderIdKey(order.getId())
					.totalNum(order.getTotalNum())
					.totalPrice(order.getPayPrice())
					.build();
			List<YqmStoreCartQueryVo> cartInfo = order.getCartInfo();
			for (YqmStoreCartQueryVo queryVo : cartInfo) {
				storePink.setCid(queryVo.getCombinationId());
				storePink.setPid(queryVo.getProductId());
				storePink.setPrice(queryVo.getProductInfo().getPrice());
			}

			Date stopTime = DateUtil.offsetHour(new Date(), storeCombination.getEffectiveTime());
			storePink.setPeople(storeCombination.getPeople());
			storePink.setStopTime(stopTime);
			storePink.setUniqueId(storeCart.getProductAttrUnique());
			if(order.getPinkId() > 0){ //其他成员入团
				if(this.getIsPinkUid(order.getPinkId(),order.getUid())) {
					return;
				}
				storePink.setKId(order.getPinkId());
				storePink.setStopTime(null);
				this.save(storePink);

				//处理拼团完成
				PinkUserDto pinkUserDto = this.getPinkMemberAndPinK(storePink);
				YqmStorePink pinkT = pinkUserDto.getPinkT();
				if( OrderInfoEnum.PINK_STATUS_1.getValue().equals(pinkT.getStatus())){
					//int count = (int)map.get("count");
					if(pinkUserDto.getCount() == 0){//处理成功
						this.pinkComplete(pinkUserDto.getUidAll(),pinkUserDto.getIdAll(),order.getUid(), pinkT);
					}else{
						this.pinkFail(pinkUserDto.getPinkAll(),pinkT,PinkEnum.PINK_BOOL_0.getValue());
					}
				}

			}else{//开团
				this.save(storePink);
				//pink_id更新到order表
				YqmStoreOrder yqmStoreOrder =  new YqmStoreOrder();
				yqmStoreOrder.setPinkId(storePink.getId());
				yqmStoreOrder.setId(order.getId());
				storeOrderService.updateById(yqmStoreOrder);

				//开团加入队列
				String redisKey = String.valueOf(CharSequenceUtil.format("{}{}",
						ShopConstants.REDIS_PINK_CANCEL_KEY, storePink.getId()));
				long expireTime = storeCombination.getEffectiveTime().longValue() * 3600;
				redisTemplate.opsForValue().set(redisKey, "1" , expireTime, TimeUnit.SECONDS);

			}


		}
	}

	/**
	 * 判断用户是否在团内
	 * @param id 拼团id
	 * @param uid 用户id
	 * @return boolean true=在
	 */
	@Override
	public boolean getIsPinkUid(Long id, Long uid) {
		int count = this.lambdaQuery()
				.eq(YqmStorePink::getIsRefund, OrderInfoEnum.PINK_REFUND_STATUS_0.getValue())
				.eq(YqmStorePink::getUid,uid)
				.and(i->i.eq(YqmStorePink::getKId,id).or().eq(YqmStorePink::getId,id))
				.count();
		return count > 0;
	}

	/**
	 * 获取拼团完成的商品总件数
	 * @return int
	 */
	@Override
	public int getPinkOkSumTotalNum() {
		return yqmStorePinkMapper.sumNum();
	}

	/**
	 * 获取拼团成功的用户
	 * @param uid uid
	 * @return list
	 */
	@Override
	public List<String> getPinkOkList(Long uid) {
		List<String> list = new ArrayList<>();
		List<PinkDto> pinkDTOList = yqmStorePinkMapper.getPinkOkList(uid);
		for (PinkDto pinkDTO : pinkDTOList) {
			list.add(pinkDTO.getNickname()+"拼团成功");
		}
		return list;
	}


	/**
	 * 获取团长拼团数据
	 * @param cid 拼团产品id
	 * @return PinkAllDto pindAll-参与的拼团的id 集合  list-团长参与的列表
	 */
	@Override
	public PinkAllDto getPinkAll(Long cid) {
		Map<String,Object> map = new LinkedHashMap<>(2);
		List<PinkDto> list = yqmStorePinkMapper.getPinks(cid);
		List<Long> pindAll = new ArrayList<>();//参与的拼团的id 集合
		for (PinkDto pinkDto : list) {
			pinkDto.setCount(String.valueOf(this.getPinkPeople(pinkDto.getId()
					,pinkDto.getPeople())));
			Date date = pinkDto.getStopTime();
			pinkDto.setH(String.valueOf(DateUtil.hour(date,true)));
			pinkDto.setI(String.valueOf(DateUtil.minute(date)));
			pinkDto.setS(String.valueOf(DateUtil.second(date)));
			pindAll.add(pinkDto.getId());
		}


		return PinkAllDto.builder()
				.list(list)
				.pindAll(pindAll)
				.build();
	}


	/**
	 * 处理团员
	 * @param pinkAll 拼团数据
	 * @return list
	 */
	private List<YqmStorePinkQueryVo> handPinkAll(List<YqmStorePink> pinkAll) {

		List<YqmStorePinkQueryVo> list = generator.convert(pinkAll,YqmStorePinkQueryVo.class);
		for (YqmStorePinkQueryVo queryVo : list) {
			YqmUserQueryVo userQueryVo = userService.getYqmUserById(queryVo.getUid().longValue());
			queryVo.setAvatar(userQueryVo.getAvatar());
			queryVo.setNickname(userQueryVo.getNickname());
		}
		return list;
	}

	/**
	 * 处理团长
	 * @param pinkT 拼团
	 * @return YqmStorePinkQueryVo
	 */
	private YqmStorePinkQueryVo handPinkT(YqmStorePink pinkT) {
		YqmStorePinkQueryVo pinkQueryVo =  generator.convert(pinkT,YqmStorePinkQueryVo.class);
		YqmUserQueryVo userQueryVo = userService.getYqmUserById(pinkQueryVo.getUid().longValue());
		pinkQueryVo.setAvatar(userQueryVo.getAvatar());
		pinkQueryVo.setNickname(userQueryVo.getNickname());

		return pinkQueryVo;
	}


	/**
	 * 获取当前拼团数据返回订单编号
	 * @param id 拼团id
	 * @param uid uid
	 * @return string
	 */
	private String getCurrentPinkOrderId(Long id, Long uid) {
		YqmStorePink pink = yqmStorePinkMapper.selectOne(Wrappers.<YqmStorePink>lambdaQuery()
				.eq(YqmStorePink::getId,id).eq(YqmStorePink::getUid,uid));
		if(pink == null){
			pink = yqmStorePinkMapper.selectOne(Wrappers.<YqmStorePink>lambdaQuery()
					.eq(YqmStorePink::getKId,id).eq(YqmStorePink::getUid,uid));
			if(pink == null) {
				return "";
			}
		}
		return pink.getOrderId();
	}


	/**
	 * 获取拼团的团员
	 * @param kid 团长id
	 * @return list
	 */
	private List<YqmStorePink> getPinkMember(Long kid) {
		return this.lambdaQuery().eq(YqmStorePink::getKId,kid)
				.eq(YqmStorePink::getIsRefund,OrderInfoEnum.PINK_REFUND_STATUS_0.getValue())
				.orderByAsc(YqmStorePink::getId)
				.list();
	}



	/**
	 * 获取一条拼团数据
	 * @param id 拼团id
	 * @return YqmStorePink
	 */
	private YqmStorePink getPinkUserOne(Long id) {
		return this.lambdaQuery().eq(YqmStorePink::getId,id).one();
	}

	/**
	 * 拼团人数完成时，判断全部人都是未退款状态
	 * @return boolean
	 */
	private boolean getPinkStatus(List<Long> idAll) {
		int count = this.lambdaQuery().in(YqmStorePink::getId,idAll)
				.eq(YqmStorePink::getIsRefund,OrderInfoEnum.PINK_REFUND_STATUS_1.getValue())
				.count();
		if(count == 0) {
			return true;
		}
		return false;
	}


	/**
	 * 拼团完成更改数据写入内容
	 * @param uidAll 用户id集合
	 * @param idAll 拼团id集合
	 * @param uid uid
	 * @param pinkT 团长
	 */
	private int pinkComplete(List<Long> uidAll,List<Long> idAll,Long uid,
			YqmStorePink pinkT) {
		boolean pinkStatus = this.getPinkStatus(idAll);//判断是否有退款的
		int pinkBool = PinkEnum.PINK_BOOL_0.getValue();
		if(pinkStatus){
			//更改状态
			LambdaQueryWrapper<YqmStorePink> wrapper = new LambdaQueryWrapper<>();
			wrapper.in(YqmStorePink::getId,idAll);

			YqmStorePink storePink = new YqmStorePink();
			storePink.setStopTime(new Date());
			storePink.setStatus(OrderInfoEnum.PINK_STATUS_2.getValue());

			this.update(storePink,wrapper);

			if(uidAll.contains(uid)){
				pinkBool = PinkEnum.PINK_BOOL_1.getValue();
			}

			//todo 模板消息
		}

		return pinkBool;

	}

	/**
	 * 拼团失败退款之后
	 * @param uid 用户id
	 * @param pid 团长id
	 */
	private void orderPinkFailAfter(Long uid, Long pid) {
		YqmStorePink yqmStorePink = new YqmStorePink();
		LambdaQueryWrapper<YqmStorePink> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(YqmStorePink::getId,pid);
		yqmStorePink.setStatus(OrderInfoEnum.PINK_STATUS_3.getValue());
		yqmStorePink.setStopTime(new Date());
		yqmStorePinkMapper.update(yqmStorePink,wrapper);

		LambdaQueryWrapper<YqmStorePink> wrapperT = new LambdaQueryWrapper<>();
		wrapperT.eq(YqmStorePink::getKId,pid);
		yqmStorePinkMapper.update(yqmStorePink,wrapperT);
		//todo 模板消息
	}


	/**
	 * 拼团失败 退款
	 * @param pinkAll 拼团数据,不包括团长
	 * @param pinkT 团长数据
	 * @param pinkBool PinkEnum
	 */
	private int pinkFail(List<YqmStorePink> pinkAll, YqmStorePink pinkT,int pinkBool) {
		Date now = new Date();
		//拼团时间超时  退款
		if(DateUtil.compare(pinkT.getStopTime(),now) < 0){
			pinkBool = PinkEnum.PINK_BOOL_MINUS_1.getValue();
			pinkAll.add(pinkT);
			//处理退款
			for (YqmStorePink storePink : pinkAll) {
				storeOrderService.orderApplyRefund("","","拼团时间超时",storePink.getOrderId(),storePink.getUid());
				this.orderPinkFailAfter(pinkT.getUid(),storePink.getId());
			}
		}

		return pinkBool;
	}

	/**
	 * 获取参团人和团长和拼团总人数
	 * @param pink 拼团
	 * @return PinkUserDto
	 */
	private PinkUserDto getPinkMemberAndPinK(YqmStorePink pink) {
		//查找拼团团员和团长
		List<YqmStorePink> pinkAll = null;
		YqmStorePink pinkT = null;
		//查找拼团团员和团长
		if(pink.getKId() > 0){ //团长存在
			pinkAll = this.getPinkMember(pink.getKId());
			pinkT =  this.getPinkUserOne(pink.getKId());
		}else{
			pinkAll = this.getPinkMember(pink.getId());
			pinkT =  pink;
		}
		//收集拼团用户id和拼团id
		List<Long> idAll = pinkAll.stream().map(YqmStorePink::getId).collect(Collectors.toList());
		List<Long> uidAll = pinkAll.stream().map(YqmStorePink::getUid).collect(Collectors.toList());

		idAll.add(pinkT.getId());
		uidAll.add(pinkT.getUid());
		//还差几人
		int count =  pinkT.getPeople() - (pinkAll.size() + 1);


		return PinkUserDto.builder()
				.pinkAll(pinkAll)
				.pinkT(pinkT)
				.idAll(idAll)
				.uidAll(uidAll)
				.count(count)
				.build();
	}


	/**
	 * 计算获取团长还差多少人拼团成功
	 * @param kid 团长参与拼团id
	 * @param people 当前满足拼团的人数
	 * @return int
	 */
	private int getPinkPeople(Long kid, int people) {
		LambdaQueryWrapper<YqmStorePink> wrapper= new LambdaQueryWrapper<>();
		wrapper.eq(YqmStorePink::getKId,kid)
		.eq(YqmStorePink::getIsRefund, OrderInfoEnum.PINK_REFUND_STATUS_0.getValue());
		//加上团长自己
		int count = yqmStorePinkMapper.selectCount(wrapper) + 1;
		return people - count;
	}

	//=================================================//

	@Override
	//@Cacheable
	public Map<String, Object> queryAll(YqmStorePinkQueryCriteria criteria, Pageable pageable) {
		getPage(pageable);
		PageInfo<YqmStorePink> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);
		List<YqmStorePinkDto> yqmStorePinkDtos = generator.convert(page.getList(), YqmStorePinkDto.class);
		yqmStorePinkDtos.forEach(i ->{
			YqmUser yqmUser = yqmUserService.getById(i.getUid());
			YqmStoreCombination storeCombination = combinationService.getById(i.getCid());
			i.setNickname(yqmUser.getNickname());
			i.setPhone(yqmUser.getPhone());
			i.setUserImg(yqmUser.getAvatar());
			if (Objects.nonNull(storeCombination)) {
				i.setProduct(storeCombination.getTitle());
				i.setImage(storeCombination.getImage());
			}
			i.setCountPeople( this.count(new LambdaQueryWrapper<YqmStorePink>().eq(YqmStorePink::getCid,i.getCid())));
		});
		map.put("content", yqmStorePinkDtos);
		map.put("totalElements", page.getTotal());
		return map;
	}


	@Override
	//@Cacheable
	public List<YqmStorePink> queryAll(YqmStorePinkQueryCriteria criteria){
		return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStorePink.class, criteria));
	}


	@Override
	public void download(List<YqmStorePinkDto> all, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (YqmStorePinkDto yqmStorePink : all) {
			Map<String,Object> map = new LinkedHashMap<>();
			map.put("用户id", yqmStorePink.getUid());
			map.put("订单id 生成", yqmStorePink.getOrderId());
			map.put("订单id  数据库", yqmStorePink.getOrderIdKey());
			map.put("购买商品个数", yqmStorePink.getTotalNum());
			map.put("购买总金额", yqmStorePink.getTotalPrice());
			map.put("拼团产品id", yqmStorePink.getCid());
			map.put("产品id", yqmStorePink.getPid());
			map.put("拼团总人数", yqmStorePink.getPeople());
			map.put("拼团产品单价", yqmStorePink.getPrice());
			map.put(" stopTime",  yqmStorePink.getStopTime());
			map.put("团长id 0为团长", yqmStorePink.getKId());
			map.put("是否发送模板消息0未发送1已发送", yqmStorePink.getIsTpl());
			map.put("是否退款 0未退款 1已退款", yqmStorePink.getIsRefund());
			map.put("状态1进行中2已完成3未完成", yqmStorePink.getStatus());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}
}
