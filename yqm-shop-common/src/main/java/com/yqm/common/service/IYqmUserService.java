package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmUser;
import com.yqm.common.request.YqmUserRequest;

/**
 * <p>
 * 普通用户表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-22
 */
public interface IYqmUserService extends IService<YqmUser> {

    QueryWrapper<YqmUser> getQuery(YqmUserRequest request);
}
