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

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2022/1/10 20:24
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpIndustryRequest extends BaseRequest {

    /**
     * 行业名称
     */
    private String industryName;

    /**
     * 父编号
     */
    private String pid;

    /**
     * 父编号集合
     */
    private String pids;

    /**
     * 排序
     */
    private Integer sort;

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
     * 状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除
     */
    private String status;


    /**
     * 排序
     */
    private boolean orderSort = true;


    /**
     * 包含的状态
     */
    private List<String> includeStatus;
}
