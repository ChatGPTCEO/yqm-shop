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
 * @Date: 2021/10/17 20:42
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class OAuth2Client {

    /**
     * 服务器 clientId
     */
    private String clientId;

    /**
     * 服务器 secret
     */
    private String clientSecret;

    /**
     * 过期时间
     */
    private int expireIn = 7200;
}
