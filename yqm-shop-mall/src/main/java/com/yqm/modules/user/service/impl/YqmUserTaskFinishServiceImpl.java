/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;


import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.modules.user.domain.YqmUserTaskFinish;
import com.yqm.modules.user.service.YqmUserTaskFinishService;
import com.yqm.modules.user.service.mapper.YqmUserTaskFinishMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 用户任务完成记录表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-07
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YqmUserTaskFinishServiceImpl extends BaseServiceImpl<YqmUserTaskFinishMapper, YqmUserTaskFinish> implements YqmUserTaskFinishService {

    private final YqmUserTaskFinishMapper yqmUserTaskFinishMapper;


    /**
     * 设置任务完成
     * @param uid uid
     * @param taskId 任务id
     */
    @Override
    public void setFinish(Long uid, int taskId) {
        int count = this.lambdaQuery()
                .eq(YqmUserTaskFinish::getUid,uid)
                .eq(YqmUserTaskFinish::getTaskId,taskId)
                .count();
        if(count == 0){
            YqmUserTaskFinish userTaskFinish = new YqmUserTaskFinish();
            userTaskFinish.setUid(uid);
            userTaskFinish.setTaskId(taskId);
            yqmUserTaskFinishMapper.insert(userTaskFinish);
        }
    }



}
