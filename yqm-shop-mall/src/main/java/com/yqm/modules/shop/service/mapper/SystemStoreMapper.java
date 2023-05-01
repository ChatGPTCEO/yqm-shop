/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service.mapper;

import com.yqm.common.mapper.CoreMapper;
import com.yqm.modules.product.vo.YqmSystemStoreQueryVo;
import com.yqm.modules.shop.domain.YqmSystemStore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author weiximei
* @date 2020-05-12
*/
@Repository
public interface SystemStoreMapper extends CoreMapper<YqmSystemStore> {

    @Select("SELECT*,ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((#{lat} * PI() / 180 - latitude * PI() / 180" +
            "    ) / 2),2) + COS(40.0497810000 * PI() / 180) * COS(latitude * PI() / 180) * POW(" +
            "    SIN((#{lon} * PI() / 180 - longitude * PI() / 180) / 2),2))) * 1000) AS distance" +
            "    FROM yqm_system_store WHERE is_del=0 AND is_show = 1 ORDER BY distance ASC"
    )
    List<YqmSystemStoreQueryVo> getStoreList(Page page, @Param("lon") double lon, @Param("lat") double lat);
}
