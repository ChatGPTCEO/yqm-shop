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

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpThirdPartyStatisticsDTO;
import com.yqm.common.entity.CustomerSysConfig;
import com.yqm.common.request.CustomerSysConfigRequest;
import com.yqm.common.request.TpThirdPartyStatisticsRequest;
import com.yqm.common.service.ICustomerSysConfigService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class CustomerConfigService {

    private final ICustomerSysConfigService customerSysConfigService;
    private final Cache<String, String> cacheLocal;

    public CustomerConfigService(ICustomerSysConfigService customerSysConfigService,
            @Qualifier("yqmCache") Cache<String, String> cacheLocal) {
        this.customerSysConfigService = customerSysConfigService;
        this.cacheLocal = cacheLocal;
    }

    public String getCacheValue(String key, String type, String siteId) {
        String keyPrefix = type + siteId + key;
        String value = cacheLocal.getIfPresent(keyPrefix);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        String dbValue = this.getValue(key, siteId);
        if (StringUtils.isNotBlank(dbValue)) {
            cacheLocal.put(keyPrefix, dbValue);
        }
        return dbValue;
    }

    public String getUserCacheValue(String key) {
        return getCacheValue(key, YqmDefine.CacheKeyType.user.getValue(), "");
    }

    public String getUserCacheValueSite(String key, String siteId) {
        return getCacheValue(key, YqmDefine.CacheKeyType.user.getValue(), siteId);
    }

    public String getValue(String key, String siteId) {
        User user = UserInfoService.getUser();
        CustomerSysConfigRequest request = new CustomerSysConfigRequest();
        request.setConfigName(key);
        request.setUserId(user.getId());
        if (StringUtils.isNotBlank(siteId)) {
            request.setSiteId(siteId);
        }
        CustomerSysConfig customerSysConfig = customerSysConfigService
                .getOne(customerSysConfigService.queryWrapper(request));
        if (Objects.isNull(customerSysConfig)) {
            log.error("本地缓存未找到 [key={}]， 请检查数据库!", key);
            return null;
        }
        return customerSysConfig.getConfigValue();
    }

    public TpThirdPartyStatisticsDTO getThirdPartyStatistics(TpThirdPartyStatisticsRequest request) {
        TpThirdPartyStatisticsDTO entity = new TpThirdPartyStatisticsDTO();
        String value = this.getValue(YqmDefine.CustomerSysConfigType.customer.getValue(), request.getSiteId());
        if (StringUtils.isNotBlank(value)) {
            entity = JSONObject.parseObject(value, TpThirdPartyStatisticsDTO.class);
        }
        entity.setSiteId(request.getSiteId());
        return entity;
    }

    public String saveThirdPartyStatistics(TpThirdPartyStatisticsRequest request) {
        User user = UserInfoService.getUser();
        CustomerSysConfigRequest configRequest = new CustomerSysConfigRequest();

        configRequest.setUserId(user.getId());
        configRequest.setSiteId(request.getSiteId());
        configRequest.setConfigName(YqmDefine.CustomerSysConfigType.customer.getValue());
        CustomerSysConfig customerSysConfig = customerSysConfigService
                .getOne(customerSysConfigService.queryWrapper(configRequest));
        if (Objects.isNull(customerSysConfig)) {
            customerSysConfig = new CustomerSysConfig();
            customerSysConfig.setCreateTime(LocalDateTime.now());
            customerSysConfig.setCreateBy(user.getId());
            customerSysConfig.setUpdatedTime(LocalDateTime.now());
            customerSysConfig.setUserId(user.getId());
        }

        String json = JSONObject.toJSONString(request);
        customerSysConfig.setSiteId(request.getSiteId());
        customerSysConfig.setConfigValue(json);
        customerSysConfig.setConfigName(configRequest.getConfigName());
        customerSysConfig.setUserId(configRequest.getUserId());
        customerSysConfig.setConfigDesc(YqmDefine.CustomerSysConfigType.customer.getDesc());
        customerSysConfigService.saveOrUpdate(customerSysConfig);
        return customerSysConfig.getId();
    }

}
