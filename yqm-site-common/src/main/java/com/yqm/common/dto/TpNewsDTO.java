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

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: weiximei
 * @Date: 2021/11/28 16:23
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TpNewsDTO  extends BaseEntity {

    /**
     * 用户id;用户id
     */
    private String userId;

    /**
     * 公司id;公司id
     */
    private String companyId;

    /**
     * 新闻标题
     */
    private String newsTitle;

    /**
     * 短标题
     */
    private String shortTitle;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 摘要
     */
    private String abstractText;

    /**
     * 分类
     */
    private String newsClassifyId;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否置顶;0 否 1 是
     */
    private String isTop;

    /**
     * 封面
     */
    private String newsImg;

    /**
     * 是否显示封面;0 否 1 是
     */
    private String showNewsImg;

    /**
     * 外链地址
     */
    private String url;

    /**
     * 英文标识
     */
    private String englishId;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签
     */
    private String tags;

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
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

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

}
