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

package com.yqm.module.admin.service;

import com.yqm.common.conversion.TpUserToDTO;
import com.yqm.common.dto.TpUserDTO;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理端用户
 *
 * @Author: weiximei
 * @Date: 2021/10/18 19:33
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminUserService {

    /**
     * 获取用户信息
     *
     * @return
     */
    public TpUserDTO getUserInfo() {
        User user = UserInfoService.getUser();
        return TpUserToDTO.toTpUserDTO(user);
    }

}