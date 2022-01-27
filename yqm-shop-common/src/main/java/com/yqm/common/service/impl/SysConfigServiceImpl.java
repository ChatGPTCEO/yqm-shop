package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.SysConfig;
import com.yqm.common.mapper.SysConfigMapper;
import com.yqm.common.request.SysConfigRequest;
import com.yqm.common.service.ISysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 站点配置 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2021-10-23
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    @Override
    public QueryWrapper<SysConfig> queryWrapper(SysConfigRequest request) {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getConfigName())) {
            queryWrapper.eq("config_name", request.getConfigName());
        }
        return queryWrapper;
    }
}
