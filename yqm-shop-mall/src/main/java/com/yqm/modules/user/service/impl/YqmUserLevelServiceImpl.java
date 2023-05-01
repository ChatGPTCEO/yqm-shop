/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.constant.ShopConstants;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.shop.domain.YqmSystemUserLevel;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.domain.YqmUserLevel;
import com.yqm.modules.user.service.YqmSystemUserLevelService;
import com.yqm.modules.user.service.YqmSystemUserTaskService;
import com.yqm.modules.user.service.YqmUserLevelService;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.user.service.mapper.SystemUserTaskMapper;
import com.yqm.modules.user.service.mapper.YqmUserLevelMapper;
import com.yqm.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 用户等级记录表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-06
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YqmUserLevelServiceImpl extends BaseServiceImpl<YqmUserLevelMapper, YqmUserLevel> implements YqmUserLevelService {

    @Autowired
    private YqmUserLevelMapper yqmUserLevelMapper;
    @Autowired
    private SystemUserTaskMapper yqmSystemUserTaskMapper;

    @Autowired
    private YqmUserService userService;
    @Autowired
    private YqmSystemUserLevelService systemUserLevelService;
    @Autowired
    private YqmSystemUserTaskService systemUserTaskService;


    /**
     * 检查是否能成为会员
     * @param uid 用户id
     */
    @Override
    public boolean setLevelComplete(Long uid) {
        //获取当前用户级别
        int levelId = 0;
        YqmUserLevel yqmUserLevel = this.getUserLevel(uid,null);
        if(yqmUserLevel != null ){
            levelId =  yqmUserLevel.getLevelId();
        }
        int nextLevelId = systemUserLevelService.getNextLevelId(levelId);
        if(nextLevelId == 0) {
            return false;
        }

        int finishCount = systemUserTaskService.getTaskComplete(nextLevelId,uid);

        //目前任务固定，如果增加任务需要自己增加逻辑，目前每个会员任务固定3
        if(finishCount == ShopConstants.TASK_FINISH_COUNT){
            this.setUserLevel(uid,nextLevelId);
            return true;
        }
        return false;
    }



    /**
     * 获取当前用户会员等级返回当前用户等级
     * @param uid uid
     * @param grade 用户级别
     * @return YqmUserLevel
     */
    @Override
    public YqmUserLevel getUserLevel(Long uid, Integer grade) {
       LambdaQueryWrapper<YqmUserLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(YqmUserLevel::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(YqmUserLevel::getUid,uid)
                .orderByDesc(YqmUserLevel::getGrade);
        if(grade != null) {
            wrapper.lt(YqmUserLevel::getGrade,grade);
        }
        YqmUserLevel userLevel = this.getOne(wrapper,false);
        if(ObjectUtil.isNull(userLevel)) {
            return null;
        }
        if(ShopCommonEnum.IS_FOREVER_1.getValue().equals(userLevel.getIsForever())) {
            return userLevel;
        }
        int nowTime = OrderUtil.getSecondTimestampTwo();
        if(nowTime > userLevel.getValidTime()){
            if(ShopCommonEnum.IS_STATUS_1.getValue().equals(userLevel.getStatus())){
                userLevel.setStatus(ShopCommonEnum.IS_STATUS_0.getValue());
                yqmUserLevelMapper.updateById(userLevel);
            }
            return this.getUserLevel(uid,userLevel.getGrade());
        }
        return userLevel;
    }


    /**
     * 设置会员等级
     * @param uid 用户id
     * @param levelId 等级id
     */
    private void setUserLevel(Long uid, int levelId){
        YqmSystemUserLevel systemUserLevelQueryVo = systemUserLevelService
                .getById(levelId);
        if(ObjectUtil.isNull(systemUserLevelQueryVo)) {
            return;
        }

        int validTime = systemUserLevelQueryVo.getValidDate() * 86400;

       LambdaQueryWrapper<YqmUserLevel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmUserLevel::getStatus,ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(YqmUserLevel::getUid,uid)
                .eq(YqmUserLevel::getLevelId,levelId)
                .last("limit 1");

        YqmUserLevel yqmUserLevel = new YqmUserLevel();
        yqmUserLevel.setIsForever(systemUserLevelQueryVo.getIsForever());
        yqmUserLevel.setStatus(ShopCommonEnum.IS_STATUS_1.getValue());
        yqmUserLevel.setGrade(systemUserLevelQueryVo.getGrade());
        yqmUserLevel.setUid(uid);
        yqmUserLevel.setLevelId(levelId);
        yqmUserLevel.setDiscount(systemUserLevelQueryVo.getDiscount().intValue());

        if(ShopCommonEnum.IS_FOREVER_1.getValue().equals(systemUserLevelQueryVo.getIsForever())){
            yqmUserLevel.setValidTime(0); //永久
        }else{
            yqmUserLevel.setValidTime(validTime+OrderUtil.getSecondTimestampTwo());
        }

        yqmUserLevel.setMark("恭喜你成为了"+systemUserLevelQueryVo.getName());
        yqmUserLevelMapper.insert(yqmUserLevel);

        //更新用户等级
        YqmUser yqmUser = new YqmUser();
        yqmUser.setLevel(levelId);
        yqmUser.setUid(uid);
        userService.updateById(yqmUser);

    }



    //    @Override
//    public UserLevelInfoDto getUserLevelInfo(int id) {
//        return yqmUserLevelMapper.getUserLevelInfo(id);
//    }



}
