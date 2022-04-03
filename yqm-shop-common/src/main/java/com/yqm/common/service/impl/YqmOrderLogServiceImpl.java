package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmOrderLog;
import com.yqm.common.mapper.YqmOrderLogMapper;
import com.yqm.common.request.YqmOrderLogRequest;
import com.yqm.common.service.IYqmOrderLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 订单日志 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
@Service
public class YqmOrderLogServiceImpl extends ServiceImpl<YqmOrderLogMapper, YqmOrderLog> implements IYqmOrderLogService {

    @Override
    public QueryWrapper<YqmOrderLog> getQuery(YqmOrderLogRequest request) {
        QueryWrapper<YqmOrderLog> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryWrapper.eq("status", request.getStatus());
        }
        if (Objects.nonNull(request.getOrderStatus())) {
            queryWrapper.eq("order_status", request.getOrderStatus());
        }
        if (StringUtils.isNotBlank(request.getOrderId())) {
            queryWrapper.eq("order_id", request.getOrderId());
        }
        if (Objects.nonNull(request.getIsSort()) && Boolean.TRUE.equals(request.getIsSort())) {
            queryWrapper.orderByDesc("created_time");
        }
        return queryWrapper;
    }
}
