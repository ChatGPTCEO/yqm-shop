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

package com.yqm.module.client.service;

import com.yqm.common.conversion.TpModuleToDTO;
import com.yqm.common.dto.TpModuleDTO;
import com.yqm.common.entity.TpModule;
import com.yqm.common.request.TpModuleRequest;
import com.yqm.common.service.ITpModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2022/1/11 20:19
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */

@Slf4j
@Service
public class ModuleService {

    private final ITpModuleService iTpModuleService;

    public ModuleService(ITpModuleService iTpModuleService) {
        this.iTpModuleService = iTpModuleService;
    }

    /**
     * 所有模块
     *
     * @param request
     * @return
     */
    public List<TpModuleDTO> getModuleList(TpModuleRequest request) {
        List<TpModule> moduleList = iTpModuleService.list(iTpModuleService.queryWrapper(request));
        return TpModuleToDTO.toTpModuleDTOList(moduleList);
    }

}
