/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.product.service.dto;

import lombok.Data;

import java.io.Serializable;


/**
* @author weiximei
* @date 2019-10-04
*/
@Data
public class YqmStoreProductSmallDto implements Serializable {

    // 商品id
    private Integer id;

    // 商品图片
    private String image;


    // 商品名称
    private String storeName;


}
