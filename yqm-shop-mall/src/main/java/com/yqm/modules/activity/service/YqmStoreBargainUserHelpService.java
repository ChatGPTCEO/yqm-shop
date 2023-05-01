/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service;


import com.yqm.common.service.BaseService;
import com.yqm.modules.activity.domain.YqmStoreBargainUserHelp;
import com.yqm.modules.activity.vo.YqmStoreBargainUserHelpQueryVo;

import java.util.List;

/**
 * <p>
 * 砍价用户帮助表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-21
 */
public interface YqmStoreBargainUserHelpService extends BaseService<YqmStoreBargainUserHelp> {

    /**
     * 获取砍价帮
     * @param bargainId 砍价商品id
     * @param bargainUserUid 砍价用户id
     * @param page page
     * @param limit limit
     * @return list
     */
    List<YqmStoreBargainUserHelpQueryVo> getList(Long bargainId, Long bargainUserUid, int page, int limit);

    /**
     * 获取砍价帮总人数
     * @param bargainId 砍价产品ID
     * @param bargainUserUid 用户参与砍价表id
     * @return int
     */
    int getBargainUserHelpPeopleCount(Long bargainId,Long bargainUserUid);


}
