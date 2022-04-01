package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.YqmProductSales;
import com.yqm.common.entity.YqmProductSkuInventory;
import com.yqm.common.entity.YqmStoreSku;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品sku Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
public interface YqmStoreSkuMapper extends BaseMapper<YqmStoreSku> {

    List<YqmProductSkuInventory> getProductSkuInventory(@Param("productIdList") List<String> productIdList);

    List<YqmProductSales> getProductSales(@Param("productIdList") List<String> productIdList);


}
