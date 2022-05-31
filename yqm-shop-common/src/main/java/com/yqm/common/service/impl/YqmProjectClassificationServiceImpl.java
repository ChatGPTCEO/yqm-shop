package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmProjectClassification;
import com.yqm.common.mapper.YqmProjectClassificationMapper;
import com.yqm.common.request.YqmProjectClassificationRequest;
import com.yqm.common.service.IYqmProjectClassificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专题分类 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-26
 */
@Service
public class YqmProjectClassificationServiceImpl extends ServiceImpl<YqmProjectClassificationMapper, YqmProjectClassification> implements IYqmProjectClassificationService {


    @Override
    public QueryWrapper<YqmProjectClassification> getQuery(YqmProjectClassificationRequest request) {
        QueryWrapper<YqmProjectClassification> queryWrapper = new QueryWrapper<>();
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
