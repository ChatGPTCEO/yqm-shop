package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmTopic;
import com.yqm.common.request.YqmTopicRequest;

/**
 * <p>
 * 话题 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
public interface IYqmTopicService extends IService<YqmTopic> {

    QueryWrapper<YqmTopic> getQuery(YqmTopicRequest request);

}
