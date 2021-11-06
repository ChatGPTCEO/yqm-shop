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

package com.yqm.common.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yqm.common.base.BaseEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: weiximei
 * @Date: 2021/10/24 19:20
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpCompanyRequest extends BaseRequest {

    /**
     * 公司名称
     */
    private String company;

    /**
     * LOGO
     */
    private String logo;

    /**
     * 公司法人
     */
    private String legalRepresentative;

    /**
     * 法人电话
     */
    private String legalRepresentativePhone;

    /**
     * 成立时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate establishedTime;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 传真
     */
    private String fax;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 企业执照
     */
    private String businessLicense;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 业务人员名称
     */
    private String businessPersonnelName;

    /**
     * 业务人员手机
     */
    private String businessPersonnelPhone;

    /**
     * 业务人员邮件
     */
    private String businessPersonnelEmail;

    /**
     * 业务人员性别
     */
    private String businessPersonnelSex;

    /**
     * 业务人员职务
     */
    private String businessPersonnelPost;

    /**
     * 省
     */
    private String provinceId;

    /**
     * 市
     */
    private String cityId;

    /**
     * 区
     */
    private String areaId;


}
