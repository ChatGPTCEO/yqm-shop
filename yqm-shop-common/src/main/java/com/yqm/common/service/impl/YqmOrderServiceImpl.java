package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmOrder;
import com.yqm.common.mapper.YqmOrderMapper;
import com.yqm.common.request.YqmOrderRequest;
import com.yqm.common.service.IYqmOrderService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
@Service
public class YqmOrderServiceImpl extends ServiceImpl<YqmOrderMapper, YqmOrder> implements IYqmOrderService {

    @Override
    public QueryWrapper<YqmOrder> getQuery(YqmOrderRequest request) {
        QueryWrapper<YqmOrder> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryWrapper.eq("status", request.getStatus());
        }
        if (Objects.nonNull(request.getOrderStatus())) {
            queryWrapper.eq("order_status", request.getOrderStatus());
        }
        return queryWrapper;
    }
}
