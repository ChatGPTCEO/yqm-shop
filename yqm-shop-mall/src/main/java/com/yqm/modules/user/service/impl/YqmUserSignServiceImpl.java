/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.BillDetailEnum;
import com.yqm.modules.shop.service.YqmSystemGroupDataService;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.domain.YqmUserBill;
import com.yqm.modules.user.domain.YqmUserSign;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.YqmUserLevelService;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.user.service.YqmUserSignService;
import com.yqm.modules.user.service.mapper.UserBillMapper;
import com.yqm.modules.user.service.mapper.YqmUserSignMapper;
import com.yqm.modules.user.vo.SignVo;
import com.yqm.modules.user.vo.YqmUserQueryVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 签到记录表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YqmUserSignServiceImpl extends BaseServiceImpl<YqmUserSignMapper, YqmUserSign> implements YqmUserSignService {

    @Autowired
    private YqmUserSignMapper yqmUserSignMapper;
    @Autowired
    private UserBillMapper userBillMapper;

    @Autowired
    private IGenerator generator;

    @Autowired
    private  YqmSystemGroupDataService systemGroupDataService;
    @Autowired
    private YqmUserService yqmUserService;
    @Autowired
    private YqmUserBillService billService;
    @Autowired
    private YqmUserLevelService userLevelService;


    /**
     *
     * @param yqmUser 用户
     * @return 签到积分
     */
    @Override
    public int sign(YqmUser yqmUser) {
        List<JSONObject> list = systemGroupDataService.getDatas(ShopConstants.YQM_SHOP_SIGN_DAY_NUM);
        if(ObjectUtil.isNull(list) || list.isEmpty()) {
            throw new YqmShopException("请先配置签到天数");
        }

        boolean isDaySign = this.getToDayIsSign(yqmUser.getUid());
        if(isDaySign) {
            throw new YqmShopException("已签到");
        }
        int signNumber = 0; //积分
        int userSignNum = yqmUser.getSignNum(); //签到次数
        if(getYesterDayIsSign(yqmUser.getUid())){
            if(yqmUser.getSignNum() > (list.size() - 1)){
                userSignNum = 0;
            }
        }else{
            userSignNum = 0;
        }
        int index = 0;
        for (Map<String,Object> map : list) {
            if(index == userSignNum){
                signNumber = Integer.valueOf(map.get("sign_num").toString());
                break;
            }
            index++;
        }

        userSignNum += 1;

        YqmUserSign userSign = new YqmUserSign();
        userSign.setUid(yqmUser.getUid());
        String title = "签到奖励";
        if(userSignNum == list.size()){
            title = "连续签到奖励";
        }
        userSign.setTitle(title);
        userSign.setNumber(signNumber);
        userSign.setBalance(yqmUser.getIntegral().intValue());
        yqmUserSignMapper.insert(userSign);

        //用户积分增加
        YqmUser user = YqmUser.builder()
                .integral(NumberUtil.add(yqmUser.getIntegral(),signNumber))
                .uid(yqmUser.getUid())
                .signNum(userSignNum)
                .build();
        boolean res = yqmUserService.updateById(user);
        if(!res) {
            throw new YqmShopException("签到失败");
        }

        //插入流水
        billService.income(yqmUser.getUid(),title, BillDetailEnum.CATEGORY_2.getValue(),
                BillDetailEnum.TYPE_10.getValue(),signNumber,yqmUser.getIntegral().doubleValue(),
                "","");

        //检查是否符合会员升级条件
        userLevelService.setLevelComplete(yqmUser.getUid());
        return signNumber;
    }

    /**
     * 分页获取用户签到数据
     * @param uid 用户id
     * @param page  page
     * @param limit limit
     * @return list
     */
    @Override
    public List<SignVo> getSignList(Long uid, int page, int limit) {
        Page<YqmUserBill> pageModel = new Page<>(page, limit);
        return userBillMapper.getSignList(uid,pageModel);
    }


    /**
     * 获取签到用户信息
     * @param yqmUser  yqmUser
     * @return YqmUserQueryVo
     */
    @Override
    public YqmUserQueryVo userSignInfo(YqmUser yqmUser) {
        YqmUserQueryVo userQueryVo = generator.convert(yqmUser,YqmUserQueryVo.class);
        Long uid = yqmUser.getUid();
        int sumSignDay = this.getSignSumDay(uid);
        boolean isDaySign = this.getToDayIsSign(uid);
        boolean isYesterDaySign = this.getYesterDayIsSign(uid);
        userQueryVo.setSumSignDay(sumSignDay);
        userQueryVo.setIsDaySign(isDaySign);
        userQueryVo.setIsYesterDaySign(isYesterDaySign);
        if(!isDaySign && !isYesterDaySign) {
            userQueryVo.setSignNum(0);
        }
        return userQueryVo;
    }

    /**
     * 获取用户今天是否签到
     * @param uid uid
     * @return boolean true=YES false=NO
     */
    private boolean getToDayIsSign(Long uid) {
        Date today = DateUtil.beginOfDay(new Date());
        int count = this.lambdaQuery().eq(YqmUserSign::getUid,uid)
                .ge(YqmUserSign::getCreateTime,today)
                .count();
        if(count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户昨天是否签到
     * @param uid uid
     * @return boolean
     */
    private boolean getYesterDayIsSign(Long uid) {
        Date today = DateUtil.beginOfDay(new Date());
        Date yesterday = DateUtil.beginOfDay(DateUtil.yesterday());

        int count = this.lambdaQuery().eq(YqmUserSign::getUid,uid)
                .lt(YqmUserSign::getCreateTime,today)
                .ge(YqmUserSign::getCreateTime,yesterday)
                .count();
        if(count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户累计签到次数
     * @param uid 用户id
     * @return int
     */
    private int getSignSumDay(Long uid) {
        return this.lambdaQuery().eq(YqmUserSign::getUid,uid).count();
    }


}
