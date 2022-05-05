package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmRefundWhy;
import com.yqm.common.mapper.YqmRefundWhyMapper;
import com.yqm.common.request.YqmRefundWhyRequest;
import com.yqm.common.service.IYqmRefundWhyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 退货原因 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
@Service
public class YqmRefundWhyServiceImpl extends ServiceImpl<YqmRefundWhyMapper, YqmRefundWhy> implements IYqmRefundWhyService {

    @Override
    public QueryWrapper<YqmRefundWhy> getQuery(YqmRefundWhyRequest request) {
        QueryWrapper<YqmRefundWhy> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getId())) {
            queryWrapper.eq("id", request.getId());
        }
        if (Objects.nonNull(request.getStartDate()) && Objects.nonNull(request.getEndDate())) {
            queryWrapper.between("created_time", request.getStartDate(), request.getEndDate());
        }
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        return queryWrapper;
    }
}
