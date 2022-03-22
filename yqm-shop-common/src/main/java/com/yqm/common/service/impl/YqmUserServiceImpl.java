package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmUser;
import com.yqm.common.mapper.YqmUserMapper;
import com.yqm.common.request.YqmUserRequest;
import com.yqm.common.service.IYqmUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 普通用户表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-22
 */
@Service
public class YqmUserServiceImpl extends ServiceImpl<YqmUserMapper, YqmUser> implements IYqmUserService {

    @Override
    public QueryWrapper<YqmUser> getQuery(YqmUserRequest request) {
        QueryWrapper<YqmUser> queryWrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotBlank(request.getAccount())) {
            queryWrapper.like("account", request.getAccount());
        }
        if (StringUtils.isNotBlank(request.getUserName())) {
            queryWrapper.like("user_name", request.getUserName());
        }
        if (Objects.nonNull(request.getCreatedTime())) {
            queryWrapper.eq("created_time", request.getCreatedTime());
        }
        return queryWrapper;
    }
}
