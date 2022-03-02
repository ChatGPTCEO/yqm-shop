package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmBrand;
import com.yqm.common.mapper.YqmBrandMapper;
import com.yqm.common.request.YqmBrandRequest;
import com.yqm.common.service.IYqmBrandService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 品牌 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-29
 */
@Service
public class YqmBrandServiceImpl extends ServiceImpl<YqmBrandMapper, YqmBrand> implements IYqmBrandService {

    private final YqmBrandMapper brandMapper;

    public YqmBrandServiceImpl(YqmBrandMapper brandMapper) {
        this.brandMapper = brandMapper;
    }

    @Override
    public QueryWrapper<YqmBrand> getQuery(YqmBrandRequest request) {
        QueryWrapper<YqmBrand> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getBrandName())) {
            queryWrapper.like("brand_name", request.getBrandName());
        }
        if (Objects.nonNull(request.getIsSort())) {
            queryWrapper.orderByAsc("sort");
        }
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("brand_name", request.getKeyword());
        }
        return queryWrapper;
    }

    @Override
    public int getMaxSort() {
        return brandMapper.getMaxSort();
    }

    @Override
    public void topSort(String userId, String id) {
        brandMapper.topSort(userId, id);
    }

    @Override
    public void minusOneSort(String userId) {
        brandMapper.minusOneSort(userId);
    }
}
