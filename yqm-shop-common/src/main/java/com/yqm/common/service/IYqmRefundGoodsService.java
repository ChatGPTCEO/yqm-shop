package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmRefundGoods;
import com.yqm.common.request.YqmRefundGoodsRequest;

/**
 * <p>
 * 退货 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
public interface IYqmRefundGoodsService extends IService<YqmRefundGoods> {

    QueryWrapper<YqmRefundGoods> getQuery(YqmRefundGoodsRequest request);
}
