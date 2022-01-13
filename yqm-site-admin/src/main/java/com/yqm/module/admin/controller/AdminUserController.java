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

import com.yqm.common.dto.TpUserDTO;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理端用户
 * 
 * @Author: weiximei
 * @Date: 2021/10/18 21:30
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */

@RequestMapping("/admin/user")
@RestController
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 获取用户信息
     * 
     * @return
     */
    @GetMapping("/getUserInfo")
    public ResponseBean<TpUserDTO> getUserInfo() {
        return ResponseBean.success(adminUserService.getUserInfo());
    }

    /**
     * 获取用户路由信息
     * 
     * @return
     */
    @GetMapping("/routes")
    public ResponseBean<List<String>> routes() {
        return ResponseBean.success(new ArrayList<>());
    }

}
