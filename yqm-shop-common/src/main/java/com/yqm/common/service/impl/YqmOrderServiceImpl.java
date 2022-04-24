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
        if (CollectionUtils.isNotEmpty(request.getInOrderStatus())) {
            queryWrapper.in("order_status", request.getInOrderStatus());
        }
        if (StringUtils.isNotEmpty(request.getPersonKeyword())) {
            queryWrapper.like("shipping_name", request.getPersonKeyword()).or().like("shipping_phone", request.getPersonKeyword());
        }
        if (StringUtils.isNotEmpty(request.getKeyword())) {
            queryWrapper.like("id", request.getKeyword());
        }
        if (Objects.nonNull(request.getStartDate()) && Objects.nonNull(request.getEndDate())) {
            queryWrapper.between("created_time", request.getStartDate(), request.getEndDate());
        }
        if (CollectionUtils.isNotEmpty(request.getInIdList())) {
            queryWrapper.in("id", request.getInIdList());
        }
        return queryWrapper;
    }
}
