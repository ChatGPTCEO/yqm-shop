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

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2021/11/21 11:54
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpNewsClassifyDTO   extends BaseEntity {

    /**
     * 父编号
     */
    private String pid;

    /**
     * 名称
     */
    private String newsClassifyName;

    /**
     * 图片
     */
    private String newsClassifyImg;

    /**
     * 描述
     */
    private String content;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示;0 不显示 1显示
     */
    private String show;

    /**
     * SEO标题
     */
    private String seoTitle;

    /**
     * SEO关键字
     */
    private String seoKeyword;

    /**
     * SEO描述
     */
    private String seoContent;

    /**
     * 插件代码
     */
    private String plugCode;

    /**
     * 插件代码位置
     */
    private String plugLocation;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 用户id
     */
    private String userId;

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
     * 子集
     */
    private List<TpNewsClassifyDTO> children;
}
