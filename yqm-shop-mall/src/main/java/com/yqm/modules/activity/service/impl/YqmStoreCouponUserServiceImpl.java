/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.CouponEnum;
import com.yqm.enums.CouponGetEnum;
import com.yqm.modules.activity.domain.YqmStoreCoupon;
import com.yqm.modules.activity.domain.YqmStoreCouponUser;
import com.yqm.modules.activity.service.YqmStoreCouponService;
import com.yqm.modules.activity.service.YqmStoreCouponUserService;
import com.yqm.modules.activity.service.dto.YqmStoreCouponUserDto;
import com.yqm.modules.activity.service.dto.YqmStoreCouponUserQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreCouponUserMapper;
import com.yqm.modules.activity.vo.StoreCouponUserVo;
import com.yqm.modules.activity.vo.YqmStoreCouponUserQueryVo;
import com.yqm.modules.cart.service.YqmStoreCartService;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
* @author weiximei
* @date 2020-05-13
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreCouponUserServiceImpl extends BaseServiceImpl<YqmStoreCouponUserMapper, YqmStoreCouponUser> implements YqmStoreCouponUserService {

    @Autowired
    private IGenerator generator;


    @Autowired
    private YqmStoreCouponUserMapper yqmStoreCouponUserMapper;

    @Autowired
    private YqmUserService userService;
    @Autowired
    private YqmStoreCouponService storeCouponService;
    @Autowired
    private YqmStoreCartService yqmStoreCartService;

    /**
     * 获取当前用户优惠券数量
     * @param uid uid
     * @return int
     */
    @Override
    public int getUserValidCouponCount(Long uid) {
        this.checkInvalidCoupon();
        return this.lambdaQuery()
                .eq(YqmStoreCouponUser::getStatus, CouponEnum.STATUS_0.getValue())
                .eq(YqmStoreCouponUser::getUid,uid)
                .count();
    }

    /**
     * 获取满足条件的可用优惠券
     * @param cartIds 购物车ids
     * @return list
     */
    @Override
    public List<StoreCouponUserVo> beUsableCouponList(Long uid,String cartIds) {

        Map<String, Object> cartGroup = yqmStoreCartService.getUserProductCartList(uid,
                cartIds, ShopConstants.YQM_SHOP_ONE_NUM);

        List<YqmStoreCartQueryVo> cartInfo = (List<YqmStoreCartQueryVo>)cartGroup.get("valid");

        BigDecimal sumPrice = BigDecimal.ZERO;
        for (YqmStoreCartQueryVo storeCart : cartInfo) {
            sumPrice = NumberUtil.add(sumPrice,NumberUtil.mul(storeCart.getCartNum(),storeCart.getTruePrice()));
        }

        List<String> productIds = cartInfo.stream()
                .map(YqmStoreCartQueryVo::getProductId)
                .map(Object::toString)
                .collect(Collectors.toList());


        return this.getUsableCouponList(uid, sumPrice.doubleValue(), productIds);
    }

    /**
     * 获取下单时候满足的优惠券
     * @param uid uid
     * @param price 总价格
     * @param productIds list
     * @return list
     */
    @Override
    public List<StoreCouponUserVo> getUsableCouponList(Long uid, double price, List<String> productIds) {
        Date now = new Date();
        List<StoreCouponUserVo> storeCouponUsers = yqmStoreCouponUserMapper.selectCouponList(now, price, uid);
        return storeCouponUsers.stream()
                .filter(coupon ->
                        CouponEnum.TYPE_2.getValue().equals(coupon.getType()) ||
                                CouponEnum.TYPE_0.getValue().equals(coupon.getType())
                                || (CouponEnum.TYPE_1.getValue().equals(coupon.getType())
                                && isSame(Arrays.asList(coupon.getProductId().split(",")),productIds)))
                .collect(Collectors.toList());

    }




    /**
     * 获取用户优惠券
     * @param id 优惠券id
     * @param uid 用户id
     * @return YqmStoreCouponUser
     */
    @Override
    public YqmStoreCouponUser getCoupon(Integer id,Long uid) {
        return this.lambdaQuery()
                .eq(YqmStoreCouponUser::getIsFail, CouponEnum.FALI_0.getValue())
                .eq(YqmStoreCouponUser::getStatus,CouponEnum.STATUS_0.getValue())
                .eq(YqmStoreCouponUser::getUid,uid)
                .eq(YqmStoreCouponUser::getId,id)
                .one();
    }

    @Override
    public void useCoupon(int id) {
        YqmStoreCouponUser couponUser = new YqmStoreCouponUser();
        couponUser.setId((long)id);
        couponUser.setStatus(1);
        couponUser.setUseTime(new Date());
        yqmStoreCouponUserMapper.updateById(couponUser);
    }



    /**
     * 获取用户优惠券
     * @param uid uid
     * @return list
     */
    @Override
    public List<YqmStoreCouponUserQueryVo> getUserCoupon(Long uid) {
        //this.checkInvalidCoupon();
        List<YqmStoreCouponUser> storeCouponUsers = yqmStoreCouponUserMapper
                .selectList(Wrappers.<YqmStoreCouponUser>lambdaQuery()
                        .eq(YqmStoreCouponUser::getUid,uid));
        List<YqmStoreCouponUserQueryVo> storeCouponUserQueryVoList = new ArrayList<>();
        long nowTime = System.currentTimeMillis();
        for (YqmStoreCouponUser couponUser : storeCouponUsers) {
            YqmStoreCouponUserQueryVo queryVo = generator.convert(couponUser,YqmStoreCouponUserQueryVo.class);
            if(couponUser.getIsFail() == 1){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已失效");
            }else if (couponUser.getStatus() == 1){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已使用");
            }else if (couponUser.getStatus() == 2){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已过期");
            }else if(couponUser.getCreateTime().getTime() > nowTime || couponUser.getEndTime().getTime() < nowTime){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已过期");
            }else{
                queryVo.set_type(CouponEnum.USE_1.getValue());
                queryVo.set_msg("可使用");
            }

            storeCouponUserQueryVoList.add(queryVo);
        }
        return storeCouponUserQueryVoList;
    }

    /**
     * 添加优惠券记录
     * @param uid 用户id
     * @param cid 优惠券id
     */
    @Override
    public void addUserCoupon(Long uid, Integer cid) {
        YqmStoreCoupon storeCoupon = storeCouponService.getById(cid);
        if(storeCoupon == null) {
            throw new YqmShopException("优惠劵不存在");
        }

        Date now = new Date();

        Date endTime = DateUtil.offsetDay(now,storeCoupon.getCouponTime());

        YqmStoreCouponUser storeCouponUser = YqmStoreCouponUser.builder()
                .cid(storeCoupon.getId())
                .uid(uid)
                .couponPrice(storeCoupon.getCouponPrice())
                .couponTitle(storeCoupon.getTitle())
                .useMinPrice(storeCoupon.getUseMinPrice())
                .endTime(endTime)
                .type(CouponGetEnum.GET.getValue())
                .build();

        this.save(storeCouponUser);

    }


    /**
     * 判断两个list是否有相同值
     * @param list1 list
     * @param list2 list
     * @return boolean
     */
    private boolean isSame(List<String> list1,List<String> list2){
        if(list2.isEmpty()) {
            return true;
        }
        list1 = new ArrayList<>(list1);
        list2 = new ArrayList<>(list2);
        list1.addAll(list2);
        int total = list1.size();

        List<String> newList = new ArrayList<>(new HashSet<>(list1));

        int newTotal = newList.size();


        return total > newTotal;
    }


    /**
     * 检查优惠券状态
     */
    private void checkInvalidCoupon() {
        Date nowTime = new Date();
       LambdaQueryWrapper<YqmStoreCouponUser> wrapper= new LambdaQueryWrapper<>();
        wrapper.lt(YqmStoreCouponUser::getEndTime,nowTime)
                .eq(YqmStoreCouponUser::getStatus,CouponEnum.STATUS_0.getValue());
        YqmStoreCouponUser couponUser = new YqmStoreCouponUser();
        couponUser.setStatus(CouponEnum.STATUS_2.getValue());
        yqmStoreCouponUserMapper.update(couponUser,wrapper);

    }



    //=========================================================================================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreCouponUserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCouponUser> page = new PageInfo<>(queryAll(criteria));
        List<YqmStoreCouponUserDto> storeOrderDTOS = generator.convert(page.getList(),YqmStoreCouponUserDto.class);
        for (YqmStoreCouponUserDto couponUserDTO : storeOrderDTOS) {
            couponUserDTO.setNickname(userService.getOne(new LambdaQueryWrapper<YqmUser>()
                    .eq(YqmUser::getUid,couponUserDTO.getUid())).getNickname());
        }
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",storeOrderDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreCouponUser> queryAll(YqmStoreCouponUserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreCouponUser.class, criteria));
    }
    @Override
    public void download(List<YqmStoreCouponUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCouponUserDto yqmStoreCouponUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("兑换的项目id", yqmStoreCouponUser.getCid());
            map.put("优惠券所属用户", yqmStoreCouponUser.getUid());
            map.put("优惠券名称", yqmStoreCouponUser.getCouponTitle());
            map.put("优惠券的面值", yqmStoreCouponUser.getCouponPrice());
            map.put("最低消费多少金额可用优惠券", yqmStoreCouponUser.getUseMinPrice());
            map.put("优惠券创建时间", yqmStoreCouponUser.getAddTime());
            map.put("优惠券结束时间", yqmStoreCouponUser.getEndTime());
            map.put("使用时间", yqmStoreCouponUser.getUseTime());
            map.put("获取方式", yqmStoreCouponUser.getType());
            map.put("状态（0：未使用，1：已使用, 2:已过期）", yqmStoreCouponUser.getStatus());
            map.put("是否有效", yqmStoreCouponUser.getIsFail());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Map<String, Object> getUserPCCoupon(Long uid, int page, int limit, Integer type) {
        Page<YqmStoreCouponUser> yqmStoreCouponUserPage = new Page<>(page, limit);
        yqmStoreCouponUserMapper.selectPage(yqmStoreCouponUserPage,Wrappers.<YqmStoreCouponUser>lambdaQuery()
                        .eq(YqmStoreCouponUser::getUid,uid).eq(YqmStoreCouponUser::getStatus,type));

        List<YqmStoreCouponUserQueryVo> storeCouponUserQueryVoList = new ArrayList<>();
        long nowTime = System.currentTimeMillis();
        for (YqmStoreCouponUser couponUser : yqmStoreCouponUserPage.getRecords()) {
            YqmStoreCouponUserQueryVo queryVo = generator.convert(couponUser,YqmStoreCouponUserQueryVo.class);
            if(couponUser.getIsFail() == 1){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已失效");
            }else if (couponUser.getStatus() == 1){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已使用");
            }else if (couponUser.getStatus() == 2){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已过期");
            }else if(couponUser.getCreateTime().getTime() > nowTime || couponUser.getEndTime().getTime() < nowTime){
                queryVo.set_type(CouponEnum.USE_0.getValue());
                queryVo.set_msg("已过期");
            }else{
                queryVo.set_type(CouponEnum.USE_1.getValue());
                queryVo.set_msg("可使用");
            }

            storeCouponUserQueryVoList.add(queryVo);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("list",storeCouponUserQueryVoList);
        map.put("total",yqmStoreCouponUserPage.getTotal());
        map.put("totalPage",yqmStoreCouponUserPage.getPages());
        return map;
    }
}
