package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.CustomerSysConfig;
import com.yqm.common.mapper.CustomerSysConfigMapper;
import com.yqm.common.request.CustomerSysConfigRequest;
import com.yqm.common.service.ICustomerSysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户站点配置 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2021-12-26
 */
@Service
public class CustomerSysConfigServiceImpl extends ServiceImpl<CustomerSysConfigMapper, CustomerSysConfig> implements ICustomerSysConfigService {

    @Override
    public QueryWrapper<CustomerSysConfig> queryWrapper(CustomerSysConfigRequest request) {
        QueryWrapper<CustomerSysConfig> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getConfigName())) {
            queryWrapper.eq("config_name", request.getConfigName());
        }
        if (StringUtils.isNotBlank(request.getUserId())) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (StringUtils.isNotBlank(request.getSiteId())) {
            queryWrapper.eq("site_id", request.getSiteId());
        }
        return queryWrapper;
    }
}
