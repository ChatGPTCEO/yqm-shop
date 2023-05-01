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
import com.yqm.modules.activity.domain.YqmStoreBargain;
import com.yqm.modules.product.vo.YqmStoreProductQueryVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
* @author weiximei
* @date 2020-05-13
*/
@Repository
public interface YqmStoreBargainMapper extends CoreMapper<YqmStoreBargain> {
    @Update("update yqm_store_bargain set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{bargainId}")
    int incStockDecSales(@Param("num") int num, @Param("bargainId") Long bargainId);

    @Update("update yqm_store_bargain set stock=stock-#{num}, sales=sales+#{num}" +
            " where id=#{bargainId} and stock >= #{num}")
    int decStockIncSales(@Param("num") int num,@Param("bargainId") Long bargainId);

    @Select("SELECT c.id,c.image,c.min_price as price,c.price as otPrice," +
            "c.title as storeName,c.status as isShow,c.cost," +
            "c.is_postage as isPostage,c.postage,c.sales,c.stock,c.is_del as isDel" +
            " FROM yqm_store_bargain c " +
            " WHERE c.id = #{id} and c.is_del = 0")
    YqmStoreProductQueryVo bargainInfo(Long id);

    @Select("select IFNULL(sum(look),0)" +
            "from yqm_store_bargain")
    int lookCount();

    @Select("select IFNULL(sum(share),0) as shareCount " +
            "from yqm_store_bargain")
    int shareCount();

    @Update("update yqm_store_bargain set share=share+1" +
            " where id=#{id}")
    void addBargainShare(@Param("id") Long id);

    @Update("update yqm_store_bargain set look=look+1" +
            " where id=#{id}")
    void addBargainLook(@Param("id") Long id);

    @Delete("delete from yqm_system_attachment where name = #{name}")
    void deleteBargainImg(@Param("name") String name);
}
