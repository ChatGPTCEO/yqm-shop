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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yqm.common.base.BaseEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: weiximei
 * @Date: 2021/11/6 15:30
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpRecruitmentDTO extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 职务
     */
    private String position;

    /**
     * 招聘人数
     */
    private String recNum;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 职责
     */
    private String responsibility;

    /**
     * 要求
     */
    private String requirements;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 联系人
     */
    private String userName;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedTime;

}
