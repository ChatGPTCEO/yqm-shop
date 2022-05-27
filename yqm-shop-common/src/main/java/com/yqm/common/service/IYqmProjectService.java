package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmProject;
import com.yqm.common.request.YqmProjectRequest;

/**
 * <p>
 * 专题 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-26
 */
public interface IYqmProjectService extends IService<YqmProject> {

    QueryWrapper<YqmProject> getQuery(YqmProjectRequest request);
}
