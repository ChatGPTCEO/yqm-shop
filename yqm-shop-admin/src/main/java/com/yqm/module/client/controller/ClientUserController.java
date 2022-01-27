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

import com.yqm.common.response.ResponseBean;
import com.yqm.security.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * 客户端用户
 *
 * @Author: weiximei
 * @Date: 2021/10/16 22:07
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/api/client/user")
@RestController
public class ClientUserController {

    /**
     * 获取用户信息-客户端
     *
     * @return
     */
    @GetMapping("/getUserInfo")
    public ResponseBean userInfo() {
        return ResponseBean.success(UserInfoService.getUser());
    }

    /**
     * 获取用户路由信息
     *
     * @return
     */
    @GetMapping("/routes")
    public ResponseBean routes() {
        return ResponseBean.success(new ArrayList<>());
    }


}
