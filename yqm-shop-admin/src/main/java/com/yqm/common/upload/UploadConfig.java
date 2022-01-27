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

package com.yqm.common.upload;

import lombok.Data;

/**
 * 上传实体
 *
 * @Author: weiximei
 * @Date: 2021/10/23 18:24
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class UploadConfig {

    /**
     * 上传 key
     */
    private String accessKey;

    /**
     * 上传 密匙
     */
    private String secretKey;

    /**
     * 空间名称
     */
    private String bucket;

    /**
     * 域名
     */
    private String host;

}
