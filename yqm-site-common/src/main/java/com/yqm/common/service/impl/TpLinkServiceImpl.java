package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpLink;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.mapper.TpLinkMapper;
import com.yqm.common.mapper.TpRecruitmentMapper;
import com.yqm.common.request.TpLinkRequest;
import com.yqm.common.service.ITpLinkService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 友情链接 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpLinkServiceImpl extends ServiceImpl<TpLinkMapper, TpLink> implements ITpLinkService {

    private TpLinkMapper tpLinkMapper;

    public TpLinkServiceImpl(TpLinkMapper tpLinkMapper) {
        this.tpLinkMapper = tpLinkMapper;
    }

    @Override
    public QueryWrapper<TpLink> queryWrapper(TpLinkRequest request) {

        QueryWrapper<TpLink> queryWrapper = new QueryWrapper();
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
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort, String userId) {
        return tpLinkMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpLinkMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpLinkMapper.getMaxSort(userId);
    }
}
