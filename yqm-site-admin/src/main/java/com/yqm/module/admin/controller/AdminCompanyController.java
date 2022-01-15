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

import com.yqm.common.dto.TpCompanyDTO;
import com.yqm.common.request.TpCompanyRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminCompanyService;
import com.yqm.module.service.CommonService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-公司
 *
 * @Author: weiximei
 * @Date: 2021/10/24 19:07
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/company")
@RestController
public class AdminCompanyController {

    private final AdminCompanyService adminCompanyService;
    private final CommonService commonService;

    public AdminCompanyController(AdminCompanyService adminCompanyService, CommonService commonService) {
        this.adminCompanyService = adminCompanyService;
        this.commonService = commonService;
    }

    /**
     * 添加操作
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpCompanyDTO> addCompany(@RequestBody TpCompanyRequest request) {
        return ResponseBean.success(adminCompanyService.addCompany(request));
    }

    /**
     * 修改操作
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpCompanyDTO> updateCompany(@RequestBody TpCompanyRequest request) {
        return ResponseBean.success(adminCompanyService.updateCompany(request));
    }

    /**
     * 获取用户绑定的公司
     *
     * @return
     */
    @GetMapping("/getUserBingCompany")
    public ResponseBean<TpCompanyDTO> getUserBingCompany() {
        return ResponseBean.success(commonService.getUserBingCompany());
    }

    /**
     * 修改公司简介操作
     *
     * @param request
     * @return
     */
    @PutMapping("/introduce")
    public ResponseBean<TpCompanyDTO> updateIntroduce(@RequestBody TpCompanyRequest request) {
        return ResponseBean.success(adminCompanyService.updateIntroduce(request));
    }

}
