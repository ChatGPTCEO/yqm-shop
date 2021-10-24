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

package com.yqm.common.dto;

import lombok.Data;

/**
 * @Author: weiximei
 * @Date: 2021/10/24 22:27
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpRegionDTO {

    /**
     * 地区代码
     */
    private Integer code;

    /**
     * 上级地区代码
     */
    private Integer pCode;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 等级 1 省 2 市 3 区
     */
    private Integer level;
}
