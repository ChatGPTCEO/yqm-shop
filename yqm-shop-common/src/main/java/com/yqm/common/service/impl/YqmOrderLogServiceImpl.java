package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmOrderLog;
import com.yqm.common.mapper.YqmOrderLogMapper;
import com.yqm.common.service.IYqmOrderLogService;
import org.springframework.stereotype.Service;

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

}
