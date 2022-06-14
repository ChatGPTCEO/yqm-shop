package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmTopic;
import com.yqm.common.mapper.YqmTopicMapper;
import com.yqm.common.request.YqmTopicRequest;
import com.yqm.common.service.IYqmTopicService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 话题 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
@Service
public class YqmTopicServiceImpl extends ServiceImpl<YqmTopicMapper, YqmTopic> implements IYqmTopicService {

    @Override
    public QueryWrapper<YqmTopic> getQuery(YqmTopicRequest request) {
        QueryWrapper<YqmTopic> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("title", request.getKeyword());
        }
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotBlank(request.getClassificationId())) {
            queryWrapper.eq("classification_id", request.getClassificationId());
        }
        return queryWrapper;
    }

}
