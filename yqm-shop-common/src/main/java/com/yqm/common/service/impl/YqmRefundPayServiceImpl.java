package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmRefundPay;
import com.yqm.common.mapper.YqmRefundPayMapper;
import com.yqm.common.request.YqmRefundPayRequest;
import com.yqm.common.service.IYqmRefundPayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 退款 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
@Service
public class YqmRefundPayServiceImpl extends ServiceImpl<YqmRefundPayMapper, YqmRefundPay> implements IYqmRefundPayService {


    @Override
    public QueryWrapper<YqmRefundPay> getQuery(YqmRefundPayRequest request) {
        QueryWrapper<YqmRefundPay> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getId())) {
            queryWrapper.eq("id", request.getId());
        }
        if (Objects.nonNull(request.getRefundStatus())) {
            queryWrapper.eq("refund_status", request.getRefundStatus());
        }
        if (Objects.nonNull(request.getStartDate()) && Objects.nonNull(request.getEndDate())) {
            queryWrapper.between("created_time", request.getStartDate(), request.getEndDate());
        }
        if (Objects.nonNull(request.getProcessingStartDate()) && Objects.nonNull(request.getProcessingEndDate())) {
            queryWrapper.between("processing_time", request.getProcessingStartDate(), request.getProcessingEndDate());
        }
        return queryWrapper;
    }
}
