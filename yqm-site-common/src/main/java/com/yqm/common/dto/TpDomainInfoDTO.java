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

import com.yqm.common.base.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2022/1/6 20:06
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpDomainInfoDTO extends BaseEntity {

    /**
     * 用户域名
     */
    private String domain;

    /**
     * 机房 china 中国  hong_kong 香港
     */
    private String computerRoom;

    /**
     * 公信备案
     */
    private String icp;

    /**
     * 公安备案
     */
    private String securityIcp;

    /**
     * 公安备案跳转地址
     */
    private String securityIcpUrl;

    /**
     * 额外字段
     **/

    /**
     * 联系方式
     */
    private String sysPhone;

    private List<TpDomainDnsDTO> domainToDnsList = new ArrayList<>();

}
