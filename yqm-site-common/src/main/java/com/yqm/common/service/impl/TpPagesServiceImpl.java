package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpPages;
import com.yqm.common.mapper.TpPagesMapper;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.service.ITpPagesService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * 页面 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-02
 */
@Service
public class TpPagesServiceImpl extends ServiceImpl<TpPagesMapper, TpPages> implements ITpPagesService {

    private final TpPagesMapper tpPagesMapper;

    public TpPagesServiceImpl(TpPagesMapper tpPagesMapper) {
        this.tpPagesMapper = tpPagesMapper;
    }

    @Override
    public QueryWrapper<TpPages> queryWrapper(TpPagesRequest request) {
        QueryWrapper<TpPages> queryWrapper = new QueryWrapper();
        if (request.isOrderSort()) {
            queryWrapper.orderByAsc("sort");
            queryWrapper.orderByDesc("updated_time");

        } else {
            queryWrapper.orderByDesc(Arrays.asList("sort", "updated_time"));
        }

        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        if (StringUtils.isNotBlank(request.getUserId())) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (Objects.nonNull(request.getSiteId())) {
            queryWrapper.eq("site_id", request.getSiteId());
        }
        if (StringUtils.isNotBlank(request.getPageBelongs())) {
            queryWrapper.eq("page_belongs", request.getPageBelongs());
        }
        if (StringUtils.isNotBlank(request.getPageType())) {
            queryWrapper.eq("page_type", request.getPageType());
        }
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort, String userId) {
        return tpPagesMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpPagesMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpPagesMapper.getMaxSort(userId);
    }
}
