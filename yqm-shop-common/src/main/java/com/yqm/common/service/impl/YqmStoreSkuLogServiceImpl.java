package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmStoreSkuLog;
import com.yqm.common.mapper.YqmStoreSkuLogMapper;
import com.yqm.common.request.YqmStoreSkuLogRequest;
import com.yqm.common.service.IYqmStoreSkuLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 商品sku日志 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-17
 */
@Service
public class YqmStoreSkuLogServiceImpl extends ServiceImpl<YqmStoreSkuLogMapper, YqmStoreSkuLog> implements IYqmStoreSkuLogService {

    @Override
    public QueryWrapper<YqmStoreSkuLog> getQuery(YqmStoreSkuLogRequest request) {
        QueryWrapper<YqmStoreSkuLog> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryWrapper.eq("status", request.getStatus());
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
