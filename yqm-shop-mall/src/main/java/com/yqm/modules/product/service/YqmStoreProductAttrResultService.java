/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.product.domain.YqmStoreProductAttrResult;

import java.util.Map;


/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmStoreProductAttrResultService  extends BaseService<YqmStoreProductAttrResult>{

    /**
     * 新增商品属性详情
     * @param map map
     * @param productId 商品id
     */
    void insertYqmStoreProductAttrResult(Map<String, Object> map, Long productId);
}
