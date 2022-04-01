package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmProductSales;
import com.yqm.common.entity.YqmProductSkuInventory;
import com.yqm.common.entity.YqmStoreSku;
import com.yqm.common.request.YqmStoreSkuRequest;

import java.util.List;

/**
 * <p>
 * 商品sku 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
public interface IYqmStoreSkuService extends IService<YqmStoreSku> {

    QueryWrapper<YqmStoreSku> getQuery(YqmStoreSkuRequest request);

    List<YqmProductSkuInventory> getProductSkuInventory(List<String> productIdList);

    List<YqmProductSales> getProductSales(List<String> productIdList);


}
