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

import com.yqm.common.define.YqmDefine;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.common.service.SysConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: weiximei
 * @Date: 2022/1/8 12:08
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/sys")
@RestController
public class SysController {

    private final SysConfigService sysConfigService;

    public SysController(SysConfigService sysConfigService) {
        this.sysConfigService = sysConfigService;
    }

    @GetMapping("")
    public ResponseBean getSysConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put(YqmDefine.SysConfigType.sys_phone.getValue(), sysConfigService.getSysCacheValue(YqmDefine.SysConfigType.sys_phone.getValue()));
        return ResponseBean.success(map);
    }

}
