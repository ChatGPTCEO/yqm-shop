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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: weiximei
 * @Date: 2021/12/26 16:42
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpSiteDTO extends BaseEntity implements Serializable {

    /**
     * 语言版本:zh_cn 中文 us_en 英文
     */
    private String language;

    /**
     * 站点名称
     */
    private String siteName;

    /**
     * 用户域名
     */
    private String domain;

    /**
     * 系统域名
     */
    private String systemDomain;

    /**
     * 系统版本
     */
    private String systemVersion;

    /**
     * 到期时间
     */
    private LocalDateTime dueTime;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

    /**
     * 排序
     */
    private String sort;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 机房 china 中国  hong_kong 香港
     */
    private String computerRoom;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 模板
     */
    private String customizeTemplate;

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
     * 付费状态
     * 0 未付费 1 已付费
     */
    private Integer whetherPay;


    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 额外字段
     **/

    /**
     * 联系方式
     */
    private String sysPhone;
}
