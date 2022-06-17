/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.product.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author weiximei
* @date 2020-05-12
*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("yqm_store_product_attr")
public class YqmStoreProductAttr implements Serializable {

    @TableId
    private Long id;


    /** 商品ID */
    private Long productId;


    /** 属性名 */
    private String attrName;


    /** 属性值 */
    private String attrValues;


    public void copy(YqmStoreProductAttr source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
