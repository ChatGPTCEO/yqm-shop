package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmFreightTemplate;
import com.yqm.common.mapper.YqmFreightTemplateMapper;
import com.yqm.common.request.YqmFreightTemplateRequest;
import com.yqm.common.service.IYqmFreightTemplateService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 运费模板 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-06
 */
@Service
public class YqmFreightTemplateServiceImpl extends ServiceImpl<YqmFreightTemplateMapper, YqmFreightTemplate> implements IYqmFreightTemplateService {

    @Override
    public QueryWrapper<YqmFreightTemplate> getQuery(YqmFreightTemplateRequest request) {
        QueryWrapper<YqmFreightTemplate> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("template_name", request.getKeyword());
        }
        if (CollectionUtils.isNotEmpty(request.getStatusList())) {
            queryWrapper.in("status", request.getStatusList());
        }
        return queryWrapper;
    }
}
