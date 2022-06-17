/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.category.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yqm.domain.BaseDomain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
* @author weiximei
* @date 2020-05-12
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yqm_store_category")
public class YqmStoreCategory extends BaseDomain {

    /** 商品分类表ID */
    @TableId
    private Integer id;


    /** 父id */
    private Integer pid;


    /** 分类名称 */
    @NotBlank(message = "分类名称必填")
    private String cateName;


    /** 排序 */
    private Integer sort;


    /** 图标 */
    private String pic;


    /** 是否推荐 */
    private Integer isShow;




    public void copy(YqmStoreCategory source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
