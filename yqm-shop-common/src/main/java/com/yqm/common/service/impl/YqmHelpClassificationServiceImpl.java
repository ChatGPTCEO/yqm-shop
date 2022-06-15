package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmHelpClassification;
import com.yqm.common.mapper.YqmHelpClassificationMapper;
import com.yqm.common.request.YqmHelpClassificationRequest;
import com.yqm.common.service.IYqmHelpClassificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 帮助分类 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
@Service
public class YqmHelpClassificationServiceImpl extends ServiceImpl<YqmHelpClassificationMapper, YqmHelpClassification> implements IYqmHelpClassificationService {


    @Override
    public QueryWrapper<YqmHelpClassification> getQuery(YqmHelpClassificationRequest request) {
        QueryWrapper<YqmHelpClassification> queryWrapper = new QueryWrapper<>();
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
