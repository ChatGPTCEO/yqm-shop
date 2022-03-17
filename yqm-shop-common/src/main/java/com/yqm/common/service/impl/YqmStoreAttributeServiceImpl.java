package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmStoreAttribute;
import com.yqm.common.mapper.YqmStoreAttributeMapper;
import com.yqm.common.request.YqmStoreAttributeRequest;
import com.yqm.common.service.IYqmStoreAttributeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-13
 */
@Service
public class YqmStoreAttributeServiceImpl extends ServiceImpl<YqmStoreAttributeMapper, YqmStoreAttribute> implements IYqmStoreAttributeService {

    private final YqmStoreAttributeMapper storeAttributeMapper;

    public YqmStoreAttributeServiceImpl(YqmStoreAttributeMapper storeAttributeMapper) {
        this.storeAttributeMapper = storeAttributeMapper;
    }


    @Override
    public QueryWrapper<YqmStoreAttribute> getQuery(YqmStoreAttributeRequest request) {
        QueryWrapper<YqmStoreAttribute> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("attribute_name", request.getKeyword()).or().like("input_value", request.getKeyword());
        }
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotEmpty(request.getStoreTypeId())) {
            queryWrapper.eq("store_type_id", request.getStoreTypeId());
        }
        return queryWrapper;
    }


    @Override
    public int getMaxSort() {
        return storeAttributeMapper.getMaxSort();
    }

    @Override
    public void topSort(String userId, String id) {
        storeAttributeMapper.topSort(userId, id);
    }

    @Override
    public void minusOneSort(String userId) {
        storeAttributeMapper.minusOneSort(userId);
    }

}
