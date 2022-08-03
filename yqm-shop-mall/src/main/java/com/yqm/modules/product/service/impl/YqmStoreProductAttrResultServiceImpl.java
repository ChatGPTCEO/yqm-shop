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
import com.yqm.modules.product.domain.YqmStoreProductAttrResult;
import com.yqm.modules.product.service.YqmStoreProductAttrResultService;
import com.yqm.modules.product.service.mapper.StoreProductAttrResultMapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreProductAttrResultServiceImpl extends BaseServiceImpl<StoreProductAttrResultMapper, YqmStoreProductAttrResult> implements YqmStoreProductAttrResultService {

    /**
     * 新增商品属性详情
     * @param map map
     * @param productId 商品id
     */
    @Override
    public void insertYqmStoreProductAttrResult(Map<String, Object> map, Long productId)
    {
        YqmStoreProductAttrResult yqmStoreProductAttrResult = new YqmStoreProductAttrResult();
        yqmStoreProductAttrResult.setProductId(productId);
        yqmStoreProductAttrResult.setResult(JSON.toJSONString(map));
        yqmStoreProductAttrResult.setChangeTime(new Date());

        int count = this.count(Wrappers.<YqmStoreProductAttrResult>lambdaQuery()
                .eq(YqmStoreProductAttrResult::getProductId,productId));
        if(count > 0) {
            this.remove(Wrappers.<YqmStoreProductAttrResult>lambdaQuery()
                    .eq(YqmStoreProductAttrResult::getProductId,productId));
        }

        this.save(yqmStoreProductAttrResult);
    }


}
