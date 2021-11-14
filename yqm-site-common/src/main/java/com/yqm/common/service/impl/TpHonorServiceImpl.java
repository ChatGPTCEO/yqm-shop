package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpHonor;
import com.yqm.common.mapper.TpHonorMapper;
import com.yqm.common.request.TpHonorRequest;
import com.yqm.common.service.ITpHonorService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 荣誉证书 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpHonorServiceImpl extends ServiceImpl<TpHonorMapper, TpHonor> implements ITpHonorService {

    private final TpHonorMapper tpHonorMapper;

    public TpHonorServiceImpl(TpHonorMapper tpHonorMapper) {
        this.tpHonorMapper = tpHonorMapper;
    }

    @Override
    public QueryWrapper<TpHonor> queryWrapper(TpHonorRequest request) {
        QueryWrapper<TpHonor> queryWrapper = new QueryWrapper<>();
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
        return tpHonorMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpHonorMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpHonorMapper.getMaxSort(userId);
    }
}
