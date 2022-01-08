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

package com.yqm.module.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpThirdPartyStatisticsDTO;
import com.yqm.common.request.TpThirdPartyStatisticsRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.common.service.CustomerConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: weiximei
 * @Date: 2022/1/8 12:08
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/customer/sys")
@RestController
public class CustomerSysController {

    private final CustomerConfigService customerConfigService;

    public CustomerSysController(CustomerConfigService customerConfigService) {
        this.customerConfigService = customerConfigService;
    }

    @GetMapping("")
    public ResponseBean getCustomerSysConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put(YqmDefine.CustomerSysConfigType.customer.getValue(), new TpThirdPartyStatisticsDTO());
        String value = customerConfigService.getUserCacheValue(YqmDefine.CustomerSysConfigType.customer.getValue());
        if (StringUtils.isNotBlank(value)) {
            TpThirdPartyStatisticsDTO entity = JSONObject.parseObject(value, TpThirdPartyStatisticsDTO.class);
            map.put(YqmDefine.CustomerSysConfigType.customer.getValue(), entity);
        }
        return ResponseBean.success(map);
    }


    @PutMapping("")
    public ResponseBean saveCustomerSysConfig(@RequestBody TpThirdPartyStatisticsRequest request) {
        String id = customerConfigService.saveThirdPartyStatistics(request);
        return ResponseBean.success(id);
    }


}
