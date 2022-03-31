package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmOrder;
import com.yqm.common.request.YqmOrderRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
public interface IYqmOrderService extends IService<YqmOrder> {

    QueryWrapper<YqmOrder> getQuery(YqmOrderRequest request);
}
