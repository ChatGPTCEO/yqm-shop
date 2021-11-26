package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpLink;
import com.yqm.common.entity.TpNewsClassify;
import com.yqm.common.mapper.TpNewsClassifyMapper;
import com.yqm.common.request.TpNewsClassifyRequest;
import com.yqm.common.service.ITpNewsClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 新闻分类 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-11-21
*/
@Service
public class TpNewsClassifyServiceImpl extends ServiceImpl<TpNewsClassifyMapper, TpNewsClassify> implements ITpNewsClassifyService {

    private final TpNewsClassifyMapper tpNewsClassifyMapper;

    public TpNewsClassifyServiceImpl(TpNewsClassifyMapper tpNewsClassifyMapper) {
        this.tpNewsClassifyMapper = tpNewsClassifyMapper;
    }

    @Override
    public QueryWrapper<TpNewsClassify> queryWrapper(TpNewsClassifyRequest request) {

        QueryWrapper<TpNewsClassify> queryWrapper = new QueryWrapper();
        if (request.isOrderSort()) {
            queryWrapper.orderByAsc("sort");
            queryWrapper.orderByDesc("updated_time");

        } else {
            queryWrapper.orderByDesc(Arrays.asList("sort", "updated_time"));
        }

        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        if (StringUtils.isNotBlank(request.getPid())) {
            queryWrapper.eq("pid", request.getPid());
        }
        if (StringUtils.isNotBlank(request.getUserId())) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort, String userId) {
        return tpNewsClassifyMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpNewsClassifyMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpNewsClassifyMapper.getMaxSort(userId);
    }
}
