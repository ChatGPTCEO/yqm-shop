package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmProjectClassification;
import com.yqm.common.request.YqmProjectClassificationRequest;

/**
 * <p>
 * 专题分类 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-26
 */
public interface IYqmProjectClassificationService extends IService<YqmProjectClassification> {

    QueryWrapper<YqmProjectClassification> getQuery(YqmProjectClassificationRequest request);
}
