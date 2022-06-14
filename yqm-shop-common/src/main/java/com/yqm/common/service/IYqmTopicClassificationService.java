package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmTopicClassification;
import com.yqm.common.request.YqmTopicClassificationRequest;

/**
 * <p>
 * 话题分类 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
public interface IYqmTopicClassificationService extends IService<YqmTopicClassification> {

    QueryWrapper<YqmTopicClassification> getQuery(YqmTopicClassificationRequest request);

}
