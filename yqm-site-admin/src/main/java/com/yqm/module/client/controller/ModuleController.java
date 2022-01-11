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

package com.yqm.module.client.controller;

import com.yqm.common.dto.TpModuleDTO;
import com.yqm.common.request.TpModuleRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.client.service.ModuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模块
 *
 * @Author: weiximei
 * @Date: 2022/1/11 20:24
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/client/module")
@RestController
public class ModuleController {


    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * 所有模块
     *
     * @param request
     * @return
     */
    @GetMapping()
    public ResponseBean getModuleList(TpModuleRequest request) {
        List<TpModuleDTO> moduleList = moduleService.getModuleList(request);
        return ResponseBean.success(moduleList);
    }
}
