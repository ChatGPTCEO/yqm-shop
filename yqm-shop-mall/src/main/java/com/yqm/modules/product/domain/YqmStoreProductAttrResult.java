/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.product.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* @author weiximei
* @date 2020-05-12
*/

@Data
@TableName("yqm_store_product_attr_result")
public class YqmStoreProductAttrResult implements Serializable {

    @TableId
    private Long id;


    /** 商品ID */
    private Long productId;


    /** 商品属性参数 */
    private String result;


    /** 上次修改时间 */
    private Date changeTime;


    public void copy(YqmStoreProductAttrResult source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
