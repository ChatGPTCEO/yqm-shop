package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpIndustry;
import com.yqm.common.mapper.TpIndustryMapper;
import com.yqm.common.request.TpIndustryRequest;
import com.yqm.common.service.ITpIndustryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 行业类目 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
@Service
public class TpIndustryServiceImpl extends ServiceImpl<TpIndustryMapper, TpIndustry> implements ITpIndustryService {


    private TpIndustryMapper tpIndustryMapper;

    public TpIndustryServiceImpl(TpIndustryMapper tpIndustryMapper) {
        this.tpIndustryMapper = tpIndustryMapper;
    }

    @Override
    public QueryWrapper<TpIndustry> queryWrapper(TpIndustryRequest request) {

        QueryWrapper<TpIndustry> queryWrapper = new QueryWrapper();
        if (request.isOrderSort()) {
            queryWrapper.orderByAsc("sort");
            queryWrapper.orderByDesc("updated_time");

        } else {
            queryWrapper.orderByDesc(Arrays.asList("sort", "updated_time"));
        }

        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort) {
        return tpIndustryMapper.updateAllSortGal(currentSort);
    }

    @Override
    public int top(String id) {
        return tpIndustryMapper.top(id);
    }

    @Override
    public int getMaxSort() {
        return tpIndustryMapper.getMaxSort();
    }
}
