package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.entity.YqmStoreProduct;
import com.yqm.common.mapper.YqmStoreProductMapper;
import com.yqm.common.request.YqmStoreProductRequest;
import com.yqm.common.service.IYqmStoreProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Service
public class YqmStoreProductServiceImpl extends ServiceImpl<YqmStoreProductMapper, YqmStoreProduct> implements IYqmStoreProductService {
    private final YqmStoreProductMapper storeProductMapper;

    public YqmStoreProductServiceImpl(YqmStoreProductMapper storeProductMapper) {
        this.storeProductMapper = storeProductMapper;
    }

    @Override
    public QueryWrapper<YqmStoreProduct> getQuery(YqmStoreProductRequest request) {
        QueryWrapper<YqmStoreProduct> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getProductName())) {
            queryWrapper.like("product_name", request.getProductName());
        }
        if (Objects.nonNull(request.getIsSort())) {
            queryWrapper.orderByAsc("sort");
        }
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("product_name", request.getKeyword());
        }
        if (StringUtils.isNotBlank(request.getIsShelves())) {
            queryWrapper.like("is_shelves", request.getIsShelves());
        }
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        return queryWrapper;
    }

    @Override
    public int getMaxSort() {
        return storeProductMapper.getMaxSort();
    }

    @Override
    public void topSort(String userId, String id) {
        storeProductMapper.topSort(userId, id);
    }

    @Override
    public void minusOneSort(String userId) {
        storeProductMapper.minusOneSort(userId);
    }

    @Override
    public int getCount(String userId) {
        return storeProductMapper.getCount(userId, YqmDefine.StatusType.effective.getValue());
    }

    @Override
    public int getShelvesCount(String userId, String shelves) {
        return storeProductMapper.getShelvesCount(userId, shelves);
    }
}
