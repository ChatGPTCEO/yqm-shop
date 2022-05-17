package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmStoreSkuLog;
import com.yqm.common.request.YqmStoreSkuLogRequest;

/**
 * <p>
 * 商品sku日志 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-17
 */
public interface IYqmStoreSkuLogService extends IService<YqmStoreSkuLog> {

    QueryWrapper<YqmStoreSkuLog> getQuery(YqmStoreSkuLogRequest request);
}
