package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmClassification;
import com.yqm.common.mapper.YqmClassificationMapper;
import com.yqm.common.request.YqmClassificationRequest;
import com.yqm.common.service.IYqmClassificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Service
public class YqmClassificationServiceImpl extends ServiceImpl<YqmClassificationMapper, YqmClassification> implements IYqmClassificationService {

    @Override
    public QueryWrapper<YqmClassification> getQuery(YqmClassificationRequest request) {
        QueryWrapper<YqmClassification> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("classify_name", request.getKeyword());
        }
        if (CollectionUtils.isNotEmpty(request.getStatusList())) {
            queryWrapper.in("status", request.getStatusList());
        }
        if (StringUtils.isNotBlank(request.getPid())) {
            queryWrapper.eq("pid", request.getPid());
        }
        return queryWrapper;
    }
}
