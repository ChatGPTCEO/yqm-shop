package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpTemplate;
import com.yqm.common.mapper.TpTemplateMapper;
import com.yqm.common.request.TpTemplateRequest;
import com.yqm.common.service.ITpTemplateService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 模板 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
@Service
public class TpTemplateServiceImpl extends ServiceImpl<TpTemplateMapper, TpTemplate> implements ITpTemplateService {


    private TpTemplateMapper tpTemplateMapper;

    public TpTemplateServiceImpl(TpTemplateMapper tpTemplateMapper) {
        this.tpTemplateMapper = tpTemplateMapper;
    }

    @Override
    public QueryWrapper<TpTemplate> queryWrapper(TpTemplateRequest request) {

        QueryWrapper<TpTemplate> queryWrapper = new QueryWrapper();
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
        return tpTemplateMapper.updateAllSortGal(currentSort);
    }

    @Override
    public int top(String id) {
        return tpTemplateMapper.top(id);
    }

    @Override
    public int getMaxSort() {
        return tpTemplateMapper.getMaxSort();
    }

}
