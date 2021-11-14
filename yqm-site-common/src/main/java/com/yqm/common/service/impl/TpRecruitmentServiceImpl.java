package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpRecruitment;
import com.yqm.common.mapper.TpRecruitmentMapper;
import com.yqm.common.request.TpRecruitmentRequest;
import com.yqm.common.service.ITpRecruitmentService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 招聘 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpRecruitmentServiceImpl extends ServiceImpl<TpRecruitmentMapper, TpRecruitment> implements ITpRecruitmentService {

    private TpRecruitmentMapper recruitmentMapper;

    public TpRecruitmentServiceImpl(TpRecruitmentMapper recruitmentMapper) {
        this.recruitmentMapper = recruitmentMapper;
    }

    @Override
    public QueryWrapper<TpRecruitment> queryWrapper(TpRecruitmentRequest request) {
        QueryWrapper<TpRecruitment> queryWrapper = new QueryWrapper();
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
        return recruitmentMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return recruitmentMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return recruitmentMapper.getMaxSort(userId);
    }
}
