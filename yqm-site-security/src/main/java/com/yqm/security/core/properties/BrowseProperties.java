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

package com.yqm.security.core.properties;

import lombok.Data;

/**
 * @Author: weiximei
 * @Date: 2021/9/12 10:39
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class BrowseProperties {

    /**
     * 登录入口
     */
    private String loginUrl = "/authentication/login";

    /**
     * 当需要身份认证的时候，跳转到这里
     */
    private String loginPage = "/authentication/require";

    /**
     * 登录成功跳转路径
     */
    private String loginSuccess = "/login/success";

    /**
     * 数据类型
     */
    private String loginType = "json";

    /**
     * 登录提交表单的 账号 参数名
     */
    private String usernameParameter = "username";

    /**
     * 登录提交表单的 密码 参数名
     */
    private String passwordParameter = "password";



}
