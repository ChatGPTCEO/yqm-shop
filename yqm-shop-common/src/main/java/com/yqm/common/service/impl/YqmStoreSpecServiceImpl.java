package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmStoreSpec;
import com.yqm.common.mapper.YqmStoreSpecMapper;
import com.yqm.common.request.YqmStoreSpecRequest;
import com.yqm.common.service.IYqmStoreSpecService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 规格属性 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-19
 */
@Service
public class YqmStoreSpecServiceImpl extends ServiceImpl<YqmStoreSpecMapper, YqmStoreSpec> implements IYqmStoreSpecService {

    @Override
    public QueryWrapper<YqmStoreSpec> getQuery(YqmStoreSpecRequest request) {
        QueryWrapper<YqmStoreSpec> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotEmpty(request.getPid())) {
            queryWrapper.eq("pid", request.getPid());
        }
        if (StringUtils.isNotEmpty(request.getProductId())) {
            queryWrapper.eq("product_id", request.getProductId());
        }
        return queryWrapper;
    }
}
