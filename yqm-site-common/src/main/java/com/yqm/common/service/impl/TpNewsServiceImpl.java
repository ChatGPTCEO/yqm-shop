package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpNews;
import com.yqm.common.entity.TpNewsClassify;
import com.yqm.common.mapper.TpNewsMapper;
import com.yqm.common.request.TpNewsRequest;
import com.yqm.common.service.ITpNewsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 新闻 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-11-28
*/
@Service
@Slf4j
public class TpNewsServiceImpl extends ServiceImpl<TpNewsMapper, TpNews> implements ITpNewsService {

    private final TpNewsMapper tpNewsMapper;

    public TpNewsServiceImpl(TpNewsMapper tpNewsMapper) {
        this.tpNewsMapper = tpNewsMapper;
    }

    @Override
    public QueryWrapper<TpNews> queryWrapper(TpNewsRequest request) {
        QueryWrapper<TpNews> queryWrapper = new QueryWrapper();
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
        if (StringUtils.isNotBlank(request.getNewsTitle())) {
            queryWrapper.like("news_title", request.getNewsTitle());
        }
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort, String userId) {
        return tpNewsMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpNewsMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpNewsMapper.getMaxSort(userId);
    }
}
