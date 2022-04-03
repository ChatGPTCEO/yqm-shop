package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmOrderLog;
import com.yqm.common.request.YqmOrderLogRequest;

/**
 * <p>
 * 订单日志 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
public interface IYqmOrderLogService extends IService<YqmOrderLog> {

    QueryWrapper<YqmOrderLog> getQuery(YqmOrderLogRequest request);
}
