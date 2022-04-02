package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmOrderItem;
import com.yqm.common.mapper.YqmOrderItemMapper;
import com.yqm.common.request.YqmOrderItemRequest;
import com.yqm.common.service.IYqmOrderItemService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单子表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
@Service
public class YqmOrderItemServiceImpl extends ServiceImpl<YqmOrderItemMapper, YqmOrderItem> implements IYqmOrderItemService {


    @Override
    public QueryWrapper<YqmOrderItem> getQuery(YqmOrderItemRequest request) {
        QueryWrapper<YqmOrderItem> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryWrapper.eq("status", request.getStatus());
        }
        return queryWrapper;
    }
}
