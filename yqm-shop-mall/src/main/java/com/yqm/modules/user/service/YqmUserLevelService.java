/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.user.domain.YqmUserLevel;


/**
 * <p>
 * 用户等级记录表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-06
 */
public interface YqmUserLevelService extends BaseService<YqmUserLevel> {


    /**
     * 检查是否能成为会员
     * @param uid 用户id
     */
    boolean setLevelComplete(Long uid);

    //UserLevelInfoDto getUserLevelInfo(int id);

    /**
     * 获取当前用户会员等级返回当前用户等级
     * @param uid uid
     * @param grade 用户级别
     * @return YqmUserLevel
     */
    YqmUserLevel getUserLevel(Long uid, Integer grade);


}
