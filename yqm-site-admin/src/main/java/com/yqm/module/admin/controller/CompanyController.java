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

import com.yqm.common.request.TpCompanyRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 添加操作
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean addCompany(@RequestBody TpCompanyRequest request) {
       return ResponseBean.success(companyService.addCompany(request));
    }

    /**
     * 修改操作
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean updateCompany(@RequestBody TpCompanyRequest request) {
        return ResponseBean.success(companyService.updateCompany(request));
    }

    /**
     * 获取用户绑定的公司
     * @return
     */
    @GetMapping("/getUserBingCompany")
    public ResponseBean getUserBingCompany() {
        return ResponseBean.success(companyService.getUserBingCompany());
    }


    /**
     * 修改操作
     * @param request
     * @return
     */
    @PutMapping("/introduce")
    public ResponseBean updateIntroduce(@RequestBody TpCompanyRequest request) {
        return ResponseBean.success(companyService.updateIntroduce(request));
    }

}
