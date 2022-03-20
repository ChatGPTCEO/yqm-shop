package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmStoreSku;
import com.yqm.common.mapper.YqmStoreSkuMapper;
import com.yqm.common.request.YqmStoreSkuRequest;
import com.yqm.common.service.IYqmStoreSkuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品sku 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Service
public class YqmStoreSkuServiceImpl extends ServiceImpl<YqmStoreSkuMapper, YqmStoreSku> implements IYqmStoreSkuService {


    @Override
    public QueryWrapper<YqmStoreSku> getQuery(YqmStoreSkuRequest request) {
        QueryWrapper<YqmStoreSku> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        return queryWrapper;
    }
}
