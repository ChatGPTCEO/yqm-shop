/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service.mapper;


import com.yqm.common.mapper.CoreMapper;
import com.yqm.modules.product.domain.YqmStoreProductRelation;
import com.yqm.modules.product.vo.YqmStoreProductRelationQueryVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 商品点赞和收藏表 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2019-10-23
 */
@Repository
public interface YqmStoreProductRelationMapper extends CoreMapper<YqmStoreProductRelation> {

    @Select("select B.id pid,A.type as category,B.store_name as storeName,B.price,B.is_integral as isIntegral,A.id id," +
            "B.ot_price as otPrice,B.sales,B.image,B.is_show as isShow" +
            " from yqm_store_product_relation A left join yqm_store_product B " +
            "on A.product_id = B.id where A.type=#{type} and A.uid=#{uid} and A.is_del = 0 and B.is_del = 0 order by A.create_time desc")
    List<YqmStoreProductRelationQueryVo> selectRelationList(Page page, @Param("uid") Long uid, @Param("type") String type);


}
