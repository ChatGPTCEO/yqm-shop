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

import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.client.service.ClientPagesService;
import org.springframework.web.bind.annotation.*;

/**
 * 页面
 *
 * @Author: weiximei
 * @Date: 2022/1/14 21:17
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/client/pages")
@RestController
public class ClientPagesController {

    private final ClientPagesService clientPagesService;

    public ClientPagesController(ClientPagesService clientPagesService) {
        this.clientPagesService = clientPagesService;
    }

    /**
     * 查询 导航集合
     *
     * @return
     */
    @GetMapping("/navigation/list")
    public ResponseBean listNavigation(TpPagesRequest request) {
        return ResponseBean.success(clientPagesService.listNavigation(request));
    }

    /**
     * 查询 导航集合 梯子型
     *
     * @return
     */
    @GetMapping("/navigation/children")
    public ResponseBean listNavigationChildren() {
        return ResponseBean.success(clientPagesService.listNavigationChildren());
    }


    /**
     * 查询一条导航数据
     *
     * @return
     */
    @GetMapping("/navigation/{id}/{siteId}")
    public ResponseBean navigationInfo(@PathVariable String id, @PathVariable String siteId) {
        return ResponseBean.success(clientPagesService.navigationInfo(id, siteId));
    }

    /**
     * 保存/修改 导航数据
     *
     * @return
     */
    @PostMapping("/navigation")
    public ResponseBean saveNavigation(@RequestBody TpPagesRequest request) {
        return ResponseBean.success(clientPagesService.saveNavigation(request));
    }

}
