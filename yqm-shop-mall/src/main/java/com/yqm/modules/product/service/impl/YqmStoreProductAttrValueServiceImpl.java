/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.service.YqmStoreProductAttrValueService;
import com.yqm.modules.product.service.mapper.StoreProductAttrValueMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreProductAttrValueServiceImpl extends BaseServiceImpl<StoreProductAttrValueMapper, YqmStoreProductAttrValue> implements YqmStoreProductAttrValueService {


}
