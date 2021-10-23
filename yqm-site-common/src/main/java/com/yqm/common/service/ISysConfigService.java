package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.SysConfig;
import com.yqm.common.request.SysConfigRequest;

/**
* <p>
    * 站点配置 服务类
    * </p>
*
* @author weiximei
* @since 2021-10-23
*/
    public interface ISysConfigService extends IService<SysConfig> {

        QueryWrapper<SysConfig> queryWrapper(SysConfigRequest request);

    }
