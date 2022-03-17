package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmStoreType;
import com.yqm.common.mapper.YqmStoreTypeMapper;
import com.yqm.common.request.YqmStoreTypeRequest;
import com.yqm.common.service.IYqmStoreTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品类型 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-13
 */
@Service
public class YqmStoreTypeServiceImpl extends ServiceImpl<YqmStoreTypeMapper, YqmStoreType> implements IYqmStoreTypeService {

    private final YqmStoreTypeMapper storeTypeMapper;

    public YqmStoreTypeServiceImpl(YqmStoreTypeMapper storeTypeMapper) {
        this.storeTypeMapper = storeTypeMapper;
    }

    @Override
    public QueryWrapper<YqmStoreType> getQuery(YqmStoreTypeRequest request) {
        QueryWrapper<YqmStoreType> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("type_name", request.getKeyword());
        }
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        return queryWrapper;
    }


    @Override
    public int getMaxSort() {
        return storeTypeMapper.getMaxSort();
    }

    @Override
    public void topSort(String userId, String id) {
        storeTypeMapper.topSort(userId, id);
    }

    @Override
    public void minusOneSort(String userId) {
        storeTypeMapper.minusOneSort(userId);
    }


    @Override
    public void minusOneAttributeNum(String id) {
        storeTypeMapper.minusOneAttributeNum(id);
    }

    @Override
    public void subtractionOneAttributeNum(String id) {
        storeTypeMapper.subtractionOneAttributeNum(id);
    }
}
