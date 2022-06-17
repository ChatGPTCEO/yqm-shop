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
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.domain.YqmUserSign;
import com.yqm.modules.user.vo.SignVo;
import com.yqm.modules.user.vo.YqmUserQueryVo;

import java.util.List;

/**
 * <p>
 * 签到记录表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-05
 */
public interface YqmUserSignService extends BaseService<YqmUserSign> {

    /**
     *
     * @param yqmUser 用户
     * @return 签到积分
     */
    int sign(YqmUser yqmUser);

    /**
     * 分页获取用户签到数据
     * @param uid 用户id
     * @param page  page
     * @param limit limit
     * @return list
     */
    List<SignVo> getSignList(Long uid, int page, int limit);

    //boolean getYesterDayIsSign(int uid);

    //boolean getToDayIsSign(int uid);

    //int getSignSumDay(int uid);

    /**
     * 获取签到用户信息
     * @param yqmUser  yqmUser
     * @return YqmUserQueryVo
     */
    YqmUserQueryVo userSignInfo(YqmUser yqmUser);


}
