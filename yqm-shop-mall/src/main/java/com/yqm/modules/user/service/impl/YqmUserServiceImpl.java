/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.ApiCode;
import com.yqm.api.UnAuthenticatedException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.constant.SystemConfigConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.BillDetailEnum;
import com.yqm.enums.Brokerage;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.activity.service.YqmStoreCouponUserService;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.order.domain.YqmStoreOrderCartInfo;
import com.yqm.modules.order.service.YqmStoreOrderCartInfoService;
import com.yqm.modules.order.service.YqmStoreOrderService;
import com.yqm.modules.order.service.mapper.StoreOrderMapper;
import com.yqm.modules.order.vo.YqmStoreOrderQueryVo;
import com.yqm.modules.product.vo.YqmStoreProductQueryVo;
import com.yqm.modules.shop.domain.YqmSystemUserLevel;
import com.yqm.modules.shop.service.YqmSystemConfigService;
import com.yqm.modules.shop.service.YqmSystemStoreStaffService;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.domain.YqmUserLevel;
import com.yqm.modules.user.service.YqmSystemUserLevelService;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.YqmUserLevelService;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.user.service.dto.PromUserDto;
import com.yqm.modules.user.service.dto.UserMoneyDto;
import com.yqm.modules.user.service.dto.YqmUserDto;
import com.yqm.modules.user.service.dto.YqmUserQueryCriteria;
import com.yqm.modules.user.service.mapper.UserBillMapper;
import com.yqm.modules.user.service.mapper.UserMapper;
import com.yqm.modules.user.vo.YqmUserQueryVo;
import com.yqm.utils.FileUtil;
import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmUserServiceImpl extends BaseServiceImpl<UserMapper, YqmUser> implements YqmUserService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private UserMapper yqmUserMapper;
    @Autowired
    private StoreOrderMapper storeOrderMapper;
    @Autowired
    private UserBillMapper userBillMapper;


    @Autowired
    private YqmSystemUserLevelService systemUserLevelService;
    @Autowired
    private YqmUserLevelService userLevelService;
    @Autowired
    private YqmStoreOrderService orderService;
    @Autowired
    private YqmSystemConfigService systemConfigService;
    @Autowired
    private YqmUserBillService billService;
    @Autowired
    private YqmStoreCouponUserService storeCouponUserService;
    @Autowired
    private YqmSystemStoreStaffService systemStoreStaffService;
    @Autowired
    private YqmStoreOrderCartInfoService storeOrderCartInfoService;


    /**
     * 返回用户累计充值金额与消费金额
     * @param uid uid
     * @return Double[]
     */
    @Override
    public Double[] getUserMoney(Long uid){
        double sumPrice = storeOrderMapper.sumPrice(uid);
        double sumRechargePrice = userBillMapper.sumRechargePrice(uid);
        return new Double[]{sumPrice,sumRechargePrice};
    }


    /**
     * 增加购买次数
     * @param uid uid
     */
    @Override
    public void incPayCount(Long uid) {
        yqmUserMapper.incPayCount(uid);
    }

    /**
     * 减去用户余额
     * @param uid uid
     * @param payPrice 金额
     */
    @Override
    public void decPrice(Long uid, BigDecimal payPrice) {
        yqmUserMapper.decPrice(payPrice,uid);
    }

    /**
     * 减去用户积分
     * @param uid 用户id
     * @param integral 积分
     */
    @Override
    public void decIntegral(Long uid, double integral) {
        yqmUserMapper.decIntegral(integral,uid);
    }


    /**
     * 获取我的分销下人员列表
     * @param uid uid
     * @param page page
     * @param limit limit
     * @param grade ShopCommonEnum.GRADE_0
     * @param keyword 关键字搜索
     * @param sort 排序
     * @return list
     */
    @Override
    public List<PromUserDto> getUserSpreadGrade(Long uid, int page, int limit, Integer grade,
                                                String keyword, String sort) {
        List<YqmUser> userList = yqmUserMapper.selectList(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getSpreadUid, uid));
        List<Long> userIds = userList.stream()
                .map(YqmUser::getUid)
                .collect(Collectors.toList());

        List<PromUserDto> list = new ArrayList<>();
        if (userIds.isEmpty()) {
            return list;
        }

        if (StrUtil.isBlank(sort)) {
            sort = "u.uid desc";
        }

        Page<YqmUser> pageModel = new Page<>(page, limit);
        if (ShopCommonEnum.GRADE_0.getValue().equals(grade)) {//-级
            list = yqmUserMapper.getUserSpreadCountList(pageModel, userIds,
                    keyword, sort);
        } else {//二级
            List<YqmUser> userListT = yqmUserMapper.selectList(Wrappers.<YqmUser>lambdaQuery()
                    .in(YqmUser::getSpreadUid, userIds));
            List<Long> userIdsT = userListT.stream()
                    .map(YqmUser::getUid)
                    .collect(Collectors.toList());
            if (userIdsT.isEmpty()) {
                return list;
            }
            list = yqmUserMapper.getUserSpreadCountList(pageModel, userIdsT,
                    keyword, sort);

        }
        return list;
    }

    /**
     * 统计分销人员
     * @param uid uid
     * @return map
     */
    @Override
    public Map<String,Integer> getSpreadCount(Long uid) {
        int countOne = yqmUserMapper.selectCount(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getSpreadUid,uid));

        int countTwo = 0;
        List<YqmUser> userList = yqmUserMapper.selectList((Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getSpreadUid,uid)));
        List<Long> userIds = userList.stream().map(YqmUser::getUid)
                .collect(Collectors.toList());
        if(!userIds.isEmpty()){
            countTwo = yqmUserMapper.selectCount(Wrappers.<YqmUser>lambdaQuery()
                    .in(YqmUser::getSpreadUid,userIds));
        }

        Map<String,Integer> map = new LinkedHashMap<>(2);
        map.put("first",countOne); //一级
        map.put("second",countTwo);//二级

        return map;
    }

    /**
     * 一级返佣
     * @param order 订单
     */
    @Override
    public void backOrderBrokerage(YqmStoreOrderQueryVo order) {
        //如果分销没开启直接返回
        String open = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_OPEN);
        if(StrUtil.isBlank(open) || ShopCommonEnum.ENABLE_2.getValue().toString().equals(open)) {
            return;
        }


        //获取购买商品的用户
        YqmUser userInfo =  this.getById(order.getUid());
        System.out.println("userInfo:"+userInfo);
        //当前用户不存在 没有上级  直接返回
        if(ObjectUtil.isNull(userInfo) || userInfo.getSpreadUid() == 0) {
            return;
        }


        YqmUser preUser = this.getById(userInfo.getSpreadUid());

        //一级返佣金额
        BigDecimal brokeragePrice = this.computeProductBrokerage(order, Brokerage.LEVEL_1);

        //返佣金额小于等于0 直接返回不返佣金

        if(brokeragePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        //计算上级推广员返佣之后的金额
        double balance = NumberUtil.add(preUser.getBrokeragePrice(),brokeragePrice).doubleValue();
        String mark = userInfo.getNickname()+"成功消费"+order.getPayPrice()+"元,奖励推广佣金"+
                brokeragePrice;
        //增加流水
        billService.income(userInfo.getSpreadUid(),"获得推广佣金",BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_2.getValue(),brokeragePrice.doubleValue(),balance, mark,order.getId().toString());

        //添加用户余额
        yqmUserMapper.incBrokeragePrice(brokeragePrice, userInfo.getSpreadUid());

        //一级返佣成功 跳转二级返佣
        this.backOrderBrokerageTwo(order);

    }




    /**
     * 更新用户余额
     * @param uid y用户id
     * @param price 金额
     */
    @Override
    public void incMoney(Long uid, BigDecimal price) {
        if(price!=null&&price.doubleValue()>0){
            yqmUserMapper.incMoney(uid,price);
        }
    }

    /**
     * 增加积分
     * @param uid uid
     * @param integral 积分
     */
    @Override
    public void incIntegral(Long uid, double integral) {
        yqmUserMapper.incIntegral(integral,uid);
    }


    /**
     * 获取用户信息
     * @param uid uid
     * @return YqmUserQueryVo
     */
    @Override
    public YqmUserQueryVo getYqmUserById(Long uid) {
        return generator.convert(this.getById(uid),YqmUserQueryVo.class);
    }


    /**
     * 转换用户信息
     * @param yqmUser user
     * @return YqmUserQueryVo
     */
    @Override
    public YqmUserQueryVo handleUser(YqmUser yqmUser) {
        return generator.convert(yqmUser,YqmUserQueryVo.class);
    }

    /**
     * 获取用户个人详细信息
     * @param yqmUser yqmUser
     * @return YqmUserQueryVo
     */
    @Override
    public YqmUserQueryVo getNewYqmUserById(YqmUser yqmUser) {
        YqmUserQueryVo userQueryVo = generator.convert(yqmUser,YqmUserQueryVo.class);
        if(userQueryVo == null){
            throw new UnAuthenticatedException(ApiCode.UNAUTHORIZED);
        }
        userQueryVo.setOrderStatusNum(orderService.orderData(yqmUser.getUid()));
        userQueryVo.setCouponCount(storeCouponUserService.getUserValidCouponCount(yqmUser.getUid()));
        //判断分销类型,指定分销废弃掉，只有一种分销方式
        /**
            String statu = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_STATU);
            if(StrUtil.isNotEmpty(statu)){
                userQueryVo.setStatu(Integer.valueOf(statu));
            }else{
                userQueryVo.setStatu(0);
            }
         **/

        //获取核销权限
        userQueryVo.setCheckStatus(systemStoreStaffService.checkStatus(yqmUser.getUid(),null));

        this.setUserSpreadCount(yqmUser);
        return userQueryVo;
    }



    /**
     * 返回会员价
     * @param price 原价
     * @param uid 用户id
     * @return vip 价格
     */
    @Override
    public double setLevelPrice(double price, long uid) {
       LambdaQueryWrapper<YqmUserLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmUserLevel::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(YqmUserLevel::getUid,uid)
                .orderByDesc(YqmUserLevel::getGrade)
                .last("limit 1");
        YqmUserLevel userLevel = userLevelService.getOne(wrapper);
        YqmSystemUserLevel systemUserLevel = new YqmSystemUserLevel();
        if(ObjectUtil.isNotNull(userLevel)) {
            systemUserLevel=  systemUserLevelService.getById(userLevel.getLevelId());
        }
        int discount = 100;
        if(ObjectUtil.isNotNull(userLevel)) {
            discount = systemUserLevel.getDiscount().intValue();
        }
        return NumberUtil.mul(NumberUtil.div(discount,100),price);
    }


    /**
     * 设置推广关系
     * @param spread 上级人
     * @param uid 本人
     */
    @Override
    public void setSpread(String spread, long uid) {
        if(StrUtil.isBlank(spread) || !NumberUtil.isNumber(spread)) {
            return;
        }

        //如果分销没开启直接返回
        String open = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_OPEN);
        if(StrUtil.isBlank(open) || ShopCommonEnum.ENABLE_2.getValue().toString().equals(open)) {
            return;
        }
        //当前用户信息
        YqmUser userInfo = this.getById(uid);
        if(ObjectUtil.isNull(userInfo)) {
            return;
        }

        //当前用户有上级直接返回
        if(userInfo.getSpreadUid() != null && userInfo.getSpreadUid() > 0) {
            return;
        }
        //没有推广编号直接返回
        long spreadInt = Long.valueOf(spread);
        if(spreadInt == 0) {
            return;
        }
        if(spreadInt == uid) {
            return;
        }

        //不能互相成为上下级
        YqmUser userInfoT = this.getById(spreadInt);
        if(ObjectUtil.isNull(userInfoT)) {
            return;
        }

        if(userInfoT.getSpreadUid() == uid) {
            return;
        }

        YqmUser yqmUser = YqmUser.builder()
                .spreadUid(spreadInt)
                .spreadTime(new Date())
                .uid(uid)
                .build();

        yqmUserMapper.updateById(yqmUser);

    }


    /**
     * 二级返佣
     * @param order 订单
     */
    private void backOrderBrokerageTwo(YqmStoreOrderQueryVo order) {

        YqmUser userInfo =  this.getById(order.getUid());

        //获取上推广人
        YqmUser userInfoTwo = this.getById(userInfo.getSpreadUid());

        //上推广人不存在 或者 上推广人没有上级    直接返回
        if(ObjectUtil.isNull(userInfoTwo) || userInfoTwo.getSpreadUid() == 0) {
            return;
        }


        //指定分销 判断 上上级是否时推广员  如果不是推广员直接返回
        YqmUser preUser = this.getById(userInfoTwo.getSpreadUid());


        //二级返佣金额
        BigDecimal brokeragePrice = this.computeProductBrokerage(order,Brokerage.LEVEL_2);

        //返佣金额小于等于0 直接返回不返佣金
        if(brokeragePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        //获取上上级推广员信息
        double balance = NumberUtil.add(preUser.getBrokeragePrice(),brokeragePrice).doubleValue();
        String mark = "二级推广人"+userInfo.getNickname()+"成功消费"+order.getPayPrice()+"元,奖励推广佣金"+
                brokeragePrice;

        //增加流水
        billService.income(userInfoTwo.getSpreadUid(),"获得推广佣金",BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_2.getValue(),brokeragePrice.doubleValue(),balance, mark,order.getId().toString());
        //添加用户余额
        yqmUserMapper.incBrokeragePrice(brokeragePrice,
                userInfoTwo.getSpreadUid());
    }


    /**
     * 计算获取返佣金额
     * @param order 订单信息
     * @param level 分销级别
     * @return double
     */
    private BigDecimal computeProductBrokerage(YqmStoreOrderQueryVo order , Brokerage level){
        List<YqmStoreOrderCartInfo> storeOrderCartInfoList = storeOrderCartInfoService
                .list(Wrappers.<YqmStoreOrderCartInfo>lambdaQuery()
                        .in(YqmStoreOrderCartInfo::getCartId, Arrays.asList(order.getCartId().split(","))));
        BigDecimal oneBrokerage = BigDecimal.ZERO;//一级返佣金额
        BigDecimal twoBrokerage = BigDecimal.ZERO;//二级返佣金额

        List<String> cartInfos = storeOrderCartInfoList.stream()
                .map(YqmStoreOrderCartInfo::getCartInfo)
                .collect(Collectors.toList());

        for (String cartInfo : cartInfos){
            YqmStoreCartQueryVo cart = JSON.parseObject(cartInfo,YqmStoreCartQueryVo.class);

            YqmStoreProductQueryVo storeProductVO = cart.getProductInfo();
            //产品是否单独分销
            if(ShopCommonEnum.IS_SUB_1.getValue().equals(storeProductVO.getIsSub())){
                oneBrokerage = NumberUtil.round(NumberUtil.add(oneBrokerage,
                        NumberUtil.mul(cart.getCartNum(),storeProductVO.getAttrInfo().getBrokerage()))
                        ,2);

                twoBrokerage = NumberUtil.round(NumberUtil.add(twoBrokerage,
                        NumberUtil.mul(cart.getCartNum(),storeProductVO.getAttrInfo().getBrokerageTwo()))
                        ,2);
            }

        }

        //获取后台一级返佣比例
        String storeBrokerageRatioStr = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_RATIO);
        String storeBrokerageTwoStr = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_TWO);


        //一级返佣比例未设置直接返回
        if(StrUtil.isBlank(storeBrokerageRatioStr)
                || !NumberUtil.isNumber(storeBrokerageRatioStr)){
            return oneBrokerage;
        }
        //二级返佣比例未设置直接返回
        if(StrUtil.isBlank(storeBrokerageTwoStr)
                || !NumberUtil.isNumber(storeBrokerageTwoStr)){
            return twoBrokerage;
        }


        switch (level){
            case LEVEL_1:
                //根据订单获取一级返佣比例
                BigDecimal storeBrokerageRatio = NumberUtil.round(NumberUtil.div(storeBrokerageRatioStr,"100"),2);
                BigDecimal brokeragePrice = NumberUtil
                        .round(NumberUtil.mul(order.getTotalPrice(),storeBrokerageRatio),2);
                //固定返佣 + 比例返佣 = 总返佣金额
                return NumberUtil.add(oneBrokerage,brokeragePrice);
            case LEVEL_2:
                //根据订单获取一级返佣比例
                BigDecimal storeBrokerageTwo = NumberUtil.round(NumberUtil.div(storeBrokerageTwoStr,"100"),2);
                BigDecimal storeBrokerageTwoPrice = NumberUtil
                        .round(NumberUtil.mul(order.getTotalPrice(),storeBrokerageTwo),2);
                //固定返佣 + 比例返佣 = 总返佣金额
                return NumberUtil.add(twoBrokerage,storeBrokerageTwoPrice);
            default:
        }


        return BigDecimal.ZERO;

    }



    /**
     * 更新下级人数
     * @param yqmUser user
     */
    private void setUserSpreadCount(YqmUser yqmUser) {
        int count = yqmUserMapper.selectCount(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getSpreadUid,yqmUser.getUid()));
        yqmUser.setSpreadCount(count);
        yqmUserMapper.updateById(yqmUser);
    }


    //===========后面管理后台部分=====================//


    /**
     * 查看下级
     * @param uid uid
     * @param grade 等级
     * @return list
     */
    @Override
    public List<PromUserDto> querySpread(Long uid, Integer grade) {
        return this.getUserSpreadGrade(uid,1, 999,grade,"","");
    }


    @Override
    public Map<String, Object> queryAll(YqmUserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmUser> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmUserDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmUser> queryAll(YqmUserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmUser.class, criteria));
    }


    @Override
    public void download(List<YqmUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmUserDto yqmUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户账户(跟accout一样)", yqmUser.getUsername());
            map.put("用户密码（跟pwd）", yqmUser.getPassword());
            map.put("真实姓名", yqmUser.getRealName());
            map.put("生日", yqmUser.getBirthday());
            map.put("身份证号码", yqmUser.getCardId());
            map.put("用户备注", yqmUser.getMark());
            map.put("合伙人id", yqmUser.getPartnerId());
            map.put("用户分组id", yqmUser.getGroupId());
            map.put("用户昵称", yqmUser.getNickname());
            map.put("用户头像", yqmUser.getAvatar());
            map.put("手机号码", yqmUser.getPhone());
            map.put("添加时间", yqmUser.getCreateTime());
            map.put("添加ip", yqmUser.getAddIp());
            map.put("用户余额", yqmUser.getNowMoney());
            map.put("佣金金额", yqmUser.getBrokeragePrice());
            map.put("用户剩余积分", yqmUser.getIntegral());
            map.put("连续签到天数", yqmUser.getSignNum());
            map.put("1为正常，0为禁止", yqmUser.getStatus());
            map.put("等级", yqmUser.getLevel());
            map.put("推广元id", yqmUser.getSpreadUid());
            map.put("推广员关联时间", yqmUser.getSpreadTime());
            map.put("用户类型", yqmUser.getUserType());
            map.put("是否为推广员", yqmUser.getIsPromoter());
            map.put("用户购买次数", yqmUser.getPayCount());
            map.put("下级人数", yqmUser.getSpreadCount());
            map.put("详细地址", yqmUser.getAddres());
            map.put("管理员编号 ", yqmUser.getAdminid());
            map.put("用户登陆类型，h5,wechat,routine", yqmUser.getLoginType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 更新用户状态
     * @param uid uid
     * @param status ShopCommonEnum
     */
    @Override
    public void onStatus(Long uid, Integer status) {
        if(ShopCommonEnum.IS_STATUS_1.getValue().equals(status)){
            status = ShopCommonEnum.IS_STATUS_0.getValue();
        }else{
            status = ShopCommonEnum.IS_STATUS_1.getValue();
        }
        yqmUserMapper.updateOnstatus(status,uid);
    }

    /**
     * 修改余额
     * @param param UserMoneyDto
     */
    @Override
    public void updateMoney(UserMoneyDto param) {
        YqmUser yqmUser = this.getById(param.getUid());
        double newMoney = 0d;
        String mark = "";
        if(param.getPtype() == 1){
            mark = "系统增加了"+param.getMoney()+"余额";
            newMoney = NumberUtil.add(yqmUser.getNowMoney(),param.getMoney()).doubleValue();
            billService.income(yqmUser.getUid(),"系统增加余额", BillDetailEnum.CATEGORY_1.getValue(),
                    BillDetailEnum.TYPE_6.getValue(),param.getMoney(),newMoney, mark,"");
        }else{
            mark = "系统扣除了"+param.getMoney()+"余额";
            newMoney = NumberUtil.sub(yqmUser.getNowMoney(),param.getMoney()).doubleValue();
            if(newMoney < 0) {
                newMoney = 0d;
            }
            billService.expend(yqmUser.getUid(), "系统减少余额",
                    BillDetailEnum.CATEGORY_1.getValue(),
                    BillDetailEnum.TYPE_7.getValue(),
                    param.getMoney(), newMoney, mark);
        }
//        YqmUser user = new YqmUser();
//        user.setUid(yqmUser.getUid());
        yqmUser.setNowMoney(BigDecimal.valueOf(newMoney));
        saveOrUpdate(yqmUser);
    }

    /**
     * 增加佣金
     * @param price 金额
     * @param uid 用户id
     */
    @Override
    public void incBrokeragePrice(BigDecimal price, Long uid) {
        yqmUserMapper.incBrokeragePrice(price,uid);
    }
}
