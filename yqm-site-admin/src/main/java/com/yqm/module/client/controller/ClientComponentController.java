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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yqm.common.dto.TpComponentDTO;
import com.yqm.common.request.TpComponentRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.client.service.ClientComponentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 组件
 *
 * @Author: weiximei
 * @Date: 2022/1/11 21:00
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/client/component")
@RestController
public class ClientComponentController {

    private final ClientComponentService clientComponentService;

    public ClientComponentController(ClientComponentService clientComponentService) {
        this.clientComponentService = clientComponentService;
    }

    /**
     * 查询组件
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public ResponseBean getComponentList(TpComponentRequest request) {
        List<TpComponentDTO> componentList = clientComponentService.getComponentList(request);
        return ResponseBean.success(componentList);
    }

    /**
     * 查询组件 分页
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean getComponentPage(TpComponentRequest request) {
        IPage<TpComponentDTO> componentList = clientComponentService.getComponentPage(request);
        return ResponseBean.success(componentList);
    }
}
