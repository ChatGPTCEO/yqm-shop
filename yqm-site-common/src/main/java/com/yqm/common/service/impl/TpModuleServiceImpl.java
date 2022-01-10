package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpModule;
import com.yqm.common.mapper.TpModuleMapper;
import com.yqm.common.request.TpModuleRequest;
import com.yqm.common.service.ITpModuleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 模块 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
@Service
public class TpModuleServiceImpl extends ServiceImpl<TpModuleMapper, TpModule> implements ITpModuleService {


    private TpModuleMapper tpModuleMapper;

    public TpModuleServiceImpl(TpModuleMapper tpModuleMapper) {
        this.tpModuleMapper = tpModuleMapper;
    }

    @Override
    public QueryWrapper<TpModule> queryWrapper(TpModuleRequest request) {

        QueryWrapper<TpModule> queryWrapper = new QueryWrapper();
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
        return tpModuleMapper.updateAllSortGal(currentSort);
    }

    @Override
    public int top(String id) {
        return tpModuleMapper.top(id);
    }

    @Override
    public int getMaxSort() {
        return tpModuleMapper.getMaxSort();
    }

}
