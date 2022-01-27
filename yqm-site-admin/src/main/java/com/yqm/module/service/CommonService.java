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

package com.yqm.module.service;

import com.yqm.common.conversion.TpCompanyToDTO;
import com.yqm.common.dto.TpCompanyDTO;
import com.yqm.common.entity.TpCompany;
import com.yqm.common.service.ITpCompanyService;
import com.yqm.security.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 公共
 * 
 * @Author: weiximei
 * @Date: 2021/11/6 15:04
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
public class CommonService {

    private final ITpCompanyService iTpCompanyService;

    public CommonService(ITpCompanyService iTpCompanyService) {
        this.iTpCompanyService = iTpCompanyService;
    }

    /**
     * 检查是否存在 公司
     * 
     * @return
     */
    public boolean checkUserBindingCompany() {
        TpCompany company = iTpCompanyService.getByUserId(UserInfoService.getUser().getId());
        return Objects.nonNull(company);
    }

    /**
     * 检查公司 是否 在自己名下
     * 
     * @return
     */
    public boolean checkUserByCompany(String companyId) {
        String currentUserId = UserInfoService.getUser().getId();
        TpCompany company = iTpCompanyService.getById(companyId);
        if (null != company && StringUtils.equals(currentUserId, company.getUserId())) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户绑定的公司
     * 
     * @return
     */
    public TpCompanyDTO getUserBingCompany() {
        TpCompany company = iTpCompanyService.getByUserId(UserInfoService.getUser().getId());
        return TpCompanyToDTO.toTpCompanyDTO(company);
    }

}
