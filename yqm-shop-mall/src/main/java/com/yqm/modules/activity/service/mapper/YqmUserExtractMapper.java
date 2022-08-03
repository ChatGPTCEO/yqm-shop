/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.mapper;

import com.yqm.common.mapper.CoreMapper;
import com.yqm.modules.activity.domain.YqmUserExtract;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
* @author weiximei
* @date 2020-05-13
*/
@Repository
public interface YqmUserExtractMapper extends CoreMapper<YqmUserExtract> {
    @Select("select IFNULL(sum(extract_price),0) from yqm_user_extract " +
            "where status=1 " +
            "and uid=#{uid}")
    double sumPrice(@Param("uid") Long uid);
}
