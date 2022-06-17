/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.category.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author weiximei
* @date 2019-10-03
*/
@Data
public class YqmStoreCategorySmallDto implements Serializable {

    // 商品分类表ID
    private Integer id;


    // 分类名称
    private String cateName;



}
