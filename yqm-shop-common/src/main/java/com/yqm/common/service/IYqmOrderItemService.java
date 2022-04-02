package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmOrderItem;
import com.yqm.common.request.YqmOrderItemRequest;

/**
 * <p>
 * 订单子表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
public interface IYqmOrderItemService extends IService<YqmOrderItem> {

    QueryWrapper<YqmOrderItem> getQuery(YqmOrderItemRequest request);
}
