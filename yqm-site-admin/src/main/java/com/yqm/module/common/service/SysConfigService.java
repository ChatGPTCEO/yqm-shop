/*
 * Copyright 2021 Wei xi mei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 *
 * limitations under the License.
 */

package com.yqm.module.common.service;

import com.google.common.cache.Cache;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.entity.SysConfig;
import com.yqm.common.request.SysConfigRequest;
import com.yqm.common.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 配置
 *
 * @Author: weiximei
 * @Date: 2021/12/26 19:22
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SysConfigService {

    private final ISysConfigService iSysConfigService;
    private final Cache<String, String> cacheLocal;

    public SysConfigService(ISysConfigService iSysConfigService, @Qualifier("yqmCache") Cache<String, String> cacheLocal) {
        this.iSysConfigService = iSysConfigService;
        this.cacheLocal = cacheLocal;
    }

    public String getCacheValue(String key, String type) {
        String keyPrefix = type + key;
        String value = cacheLocal.getIfPresent(keyPrefix);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        String dbValue = this.getValue(key);
        if (StringUtils.isNotBlank(dbValue)) {
            cacheLocal.put(keyPrefix, dbValue);
        }
        return dbValue;
    }

    public String getSysCacheValue(String key) {
        return getCacheValue(key, YqmDefine.CacheKeyType.system.getValue());
    }

    public String getValue(String key) {
        SysConfigRequest request = new SysConfigRequest();
        request.setConfigName(key);
        SysConfig sysConfig = iSysConfigService.getOne(iSysConfigService.queryWrapper(request));
        if (Objects.isNull(sysConfig)) {
            log.error("本地缓存未找到 [key={}]， 请检查数据库!", key);
            return null;
        }
        return sysConfig.getConfigValue();
    }

}
