/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.order.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author weiximei
* @date 2020-05-12
*/
@Data
public class YqmStoreOrderCartInfoDto implements Serializable {

    private Integer id;

    /** 订单id */
    private Integer oid;

    /**
     * 订单号
     */
    private String orderId;

    /** 购物车id */
    private Integer cartId;

    /** 商品ID */
    private Integer productId;

    /** 购买东西的详细信息 */
    private String cartInfo;

    /** 唯一id */
    private String unique;
}
