package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmProject;
import com.yqm.common.mapper.YqmProjectMapper;
import com.yqm.common.request.YqmProjectRequest;
import com.yqm.common.service.IYqmProjectService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专题 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-26
 */
@Service
public class YqmProjectServiceImpl extends ServiceImpl<YqmProjectMapper, YqmProject> implements IYqmProjectService {

    @Override
    public QueryWrapper<YqmProject> getQuery(YqmProjectRequest request) {
        QueryWrapper<YqmProject> queryWrapper = new QueryWrapper<>();
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
