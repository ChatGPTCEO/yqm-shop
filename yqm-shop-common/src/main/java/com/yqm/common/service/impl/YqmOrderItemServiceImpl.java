package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmOrderItem;
import com.yqm.common.mapper.YqmOrderItemMapper;
import com.yqm.common.service.IYqmOrderItemService;
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

}
