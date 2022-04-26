package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmRefundPay;
import com.yqm.common.request.YqmRefundPayRequest;

/**
 * <p>
 * 退款 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
public interface IYqmRefundPayService extends IService<YqmRefundPay> {

    QueryWrapper<YqmRefundPay> getQuery(YqmRefundPayRequest request);

}
