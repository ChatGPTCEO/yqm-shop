package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpSite;
import com.yqm.common.mapper.TpSiteMapper;
import com.yqm.common.request.TpSiteRequest;
import com.yqm.common.service.ITpSiteService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 * 站点 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2021-12-26
 */
@Service
public class TpSiteServiceImpl extends ServiceImpl<TpSiteMapper, TpSite> implements ITpSiteService {


    private final TpSiteMapper tpSiteMapper;

    public TpSiteServiceImpl(TpSiteMapper tpSiteMapper) {
        this.tpSiteMapper = tpSiteMapper;
    }

    @Override
    public QueryWrapper<TpSite> queryWrapper(TpSiteRequest request) {

        QueryWrapper<TpSite> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("due_time");
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
        if (Objects.nonNull(request.getIsNullDomain())) {
            if (request.getIsNullDomain() == Boolean.TRUE) {
                queryWrapper.isNotNull("domain");
            } else {
                queryWrapper.isNull("domain");
            }
        }
        if (StringUtils.isNotBlank(request.getId())) {
            queryWrapper.eq("id", request.getId());
        }
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort, String userId) {
        return tpSiteMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpSiteMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpSiteMapper.getMaxSort(userId);
    }

}
