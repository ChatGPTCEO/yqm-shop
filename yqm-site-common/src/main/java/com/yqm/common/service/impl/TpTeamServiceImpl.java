package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpLink;
import com.yqm.common.entity.TpTeam;
import com.yqm.common.mapper.TpLinkMapper;
import com.yqm.common.mapper.TpTeamMapper;
import com.yqm.common.request.TpTeamRequest;
import com.yqm.common.service.ITpTeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 团队 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpTeamServiceImpl extends ServiceImpl<TpTeamMapper, TpTeam> implements ITpTeamService {

    private TpTeamMapper tpTeamMapper;

    public TpTeamServiceImpl(TpTeamMapper tpTeamMapper) {
        this.tpTeamMapper = tpTeamMapper;
    }


    @Override
    public QueryWrapper<TpTeam> queryWrapper(TpTeamRequest request) {
        QueryWrapper<TpTeam> queryWrapper = new QueryWrapper();
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
    public int updateAllSortGal(Integer currentSort, String userId) {
        return tpTeamMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpTeamMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpTeamMapper.getMaxSort(userId);
    }
}
