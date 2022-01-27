package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpComponent;
import com.yqm.common.mapper.TpComponentMapper;
import com.yqm.common.request.TpComponentRequest;
import com.yqm.common.service.ITpComponentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 组件 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-11
 */
@Service
public class TpComponentServiceImpl extends ServiceImpl<TpComponentMapper, TpComponent> implements ITpComponentService {

    private TpComponentMapper tpComponentMapper;

    public TpComponentServiceImpl(TpComponentMapper tpComponentMapper) {
        this.tpComponentMapper = tpComponentMapper;
    }

    @Override
    public QueryWrapper<TpComponent> queryWrapper(TpComponentRequest request) {

        QueryWrapper<TpComponent> queryWrapper = new QueryWrapper<>();
        if (request.isOrderSort()) {
            queryWrapper.orderByAsc("sort");
            queryWrapper.orderByDesc("updated_time");

        } else {
            queryWrapper.orderByDesc(Arrays.asList("sort", "updated_time"));
        }

        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        if (StringUtils.isNotBlank(request.getModuleId())) {
            queryWrapper.eq("module_id", request.getModuleId());
        }
        if (StringUtils.isNotBlank(request.getTemplateId())) {
            queryWrapper.eq("template_id", request.getTemplateId());
        }
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("component_name", request.getKeyword()).or().like("id", request.getKeyword());
        }
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort) {
        return tpComponentMapper.updateAllSortGal(currentSort);
    }

    @Override
    public int top(String id) {
        return tpComponentMapper.top(id);
    }

    @Override
    public int getMaxSort() {
        return tpComponentMapper.getMaxSort();
    }

}
