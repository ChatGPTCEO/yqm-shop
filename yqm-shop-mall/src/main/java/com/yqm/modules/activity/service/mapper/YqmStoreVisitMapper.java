/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.mapper;

import com.yqm.common.mapper.CoreMapper;
import com.yqm.modules.activity.domain.YqmStoreVisit;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
* @author weiximei
* @date 2020-05-13
*/
@Repository
public interface YqmStoreVisitMapper extends CoreMapper<YqmStoreVisit> {

    /**
     * 拼团浏览量
     * @param productId
     * @return
     */
    @Update("update yqm_store_visit set count=count+1 " +
            "where uid=#{uid} AND id=#{productId}")
    int incBrowseNum(@Param("uid") Long uid,@Param("productId") Long productId);
}
