package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.CustomerSysConfig;
import com.yqm.common.request.CustomerSysConfigRequest;

/**
 * <p>
 * 用户站点配置 服务类
 * </p>
 *
 * @author weiximei
 * @since 2021-12-26
 */
public interface ICustomerSysConfigService extends IService<CustomerSysConfig> {

    QueryWrapper<CustomerSysConfig> queryWrapper(CustomerSysConfigRequest request);

}
