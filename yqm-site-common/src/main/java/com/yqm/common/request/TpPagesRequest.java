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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2022/1/2 18:06
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TpPagesRequest extends BaseRequest {

    /**
     * 站点编号
     */
    private String siteId;

    /**
     * 页面路径
     */
    private String pageUrl;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 页面类型 navigation 导航
     */
    private String pageType;

    /**
     * 页面归属;system 系统 user 用户
     */
    private String pageBelongs;

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
     * 插件代码位置;head 在head结束标签前 body 在body结束标签前
     */
    private String plugLocation;

    /**
     * 父id
     */
    private String pid;

    /**
     * 父id集合
     */
    private String pids;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 公司id
     */
    private String companyId;

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
     * 是否显示;show 显示 hidden 隐藏
     */
    private String pageShow;

    /**
     * 链接类型;default 默认 custom 自定义
     */
    private String linkType;

    /**
     * 链接地址
     */
    private String linkUrl;

    /**
     * 打开类型;new 新窗口 current 当前
     */
    private String openType;


    /** 额外 **/


    /**
     * 排序
     */
    private boolean orderSort = true;


    /**
     * 包含的状态
     */
    private List<String> includeStatus;

    /**
     * 查询延时
     */
    private Boolean delay;

    /**
     * 层级
     */
    private Integer level;

}
