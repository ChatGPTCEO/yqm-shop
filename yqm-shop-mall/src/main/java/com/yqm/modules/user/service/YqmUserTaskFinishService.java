/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service;


import com.yqm.common.service.BaseService;
import com.yqm.modules.user.domain.YqmUserTaskFinish;

/**
 * <p>
 * 用户任务完成记录表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-07
 */
public interface YqmUserTaskFinishService extends BaseService<YqmUserTaskFinish> {

    /**
     * 设置任务完成
     * @param uid uid
     * @param taskId 任务id
     */
    void setFinish(Long uid,int taskId);


}
