package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmRefundWhy;
import com.yqm.common.request.YqmRefundWhyRequest;

/**
 * <p>
 * 退货原因 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
public interface IYqmRefundWhyService extends IService<YqmRefundWhy> {

    QueryWrapper<YqmRefundWhy> getQuery(YqmRefundWhyRequest request);
}
