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
import com.yqm.module.client.service.PagesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class PagesController {

    private final PagesService pagesService;

    public PagesController(PagesService pagesService) {
        this.pagesService = pagesService;
    }

    /**
     * 查询 导航集合
     *
     * @return
     */
    @GetMapping("/navigation")
    public ResponseBean listNavigation(TpPagesRequest request) {
        return ResponseBean.success(pagesService.listNavigation(request));
    }

}
