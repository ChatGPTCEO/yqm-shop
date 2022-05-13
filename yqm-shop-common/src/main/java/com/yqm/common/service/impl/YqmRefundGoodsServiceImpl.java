package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmRefundGoods;
import com.yqm.common.mapper.YqmRefundGoodsMapper;
import com.yqm.common.request.YqmRefundGoodsRequest;
import com.yqm.common.service.IYqmRefundGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 退货 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
@Service
public class YqmRefundGoodsServiceImpl extends ServiceImpl<YqmRefundGoodsMapper, YqmRefundGoods> implements IYqmRefundGoodsService {


    @Override
    public QueryWrapper<YqmRefundGoods> getQuery(YqmRefundGoodsRequest request) {
        QueryWrapper<YqmRefundGoods> queryWrapper = new QueryWrapper<>();
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
