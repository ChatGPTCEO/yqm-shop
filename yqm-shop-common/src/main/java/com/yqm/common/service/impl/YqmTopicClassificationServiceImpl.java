package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmTopicClassification;
import com.yqm.common.mapper.YqmTopicClassificationMapper;
import com.yqm.common.request.YqmTopicClassificationRequest;
import com.yqm.common.service.IYqmTopicClassificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 话题分类 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
@Service
public class YqmTopicClassificationServiceImpl extends ServiceImpl<YqmTopicClassificationMapper, YqmTopicClassification> implements IYqmTopicClassificationService {


    @Override
    public QueryWrapper<YqmTopicClassification> getQuery(YqmTopicClassificationRequest request) {
        QueryWrapper<YqmTopicClassification> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("classification_name", request.getKeyword());
        }
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (CollectionUtils.isNotEmpty(request.getInIdList())) {
            queryWrapper.in("id", request.getInIdList());
        }
        return queryWrapper;
    }
}
