/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.rest;

import com.yqm.enums.ShopCommonEnum;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.product.domain.YqmStoreProductReply;
import com.yqm.modules.product.service.YqmStoreProductReplyService;
import com.yqm.modules.product.service.dto.YqmStoreProductReplyQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
* @author weiximei
* @date 2019-11-03
*/
@Api(tags = "商城:评论管理")
@RestController
@RequestMapping("api")
public class StoreProductReplyController {


    private final YqmStoreProductReplyService yqmStoreProductReplyService;

    public StoreProductReplyController(YqmStoreProductReplyService yqmStoreProductReplyService) {
        this.yqmStoreProductReplyService = yqmStoreProductReplyService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmStoreProductReply")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREPRODUCTREPLY_ALL','YQMSTOREPRODUCTREPLY_SELECT')")
    public ResponseEntity getYqmStoreProductReplys(YqmStoreProductReplyQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreProductReplyService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/yqmStoreProductReply")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREPRODUCTREPLY_ALL','YQMSTOREPRODUCTREPLY_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmStoreProductReply resources){
        resources.setMerchantReplyTime(new Date());
        resources.setIsReply(ShopCommonEnum.REPLY_1.getValue());
        yqmStoreProductReplyService.updateById(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yqmStoreProductReply/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREPRODUCTREPLY_ALL','YQMSTOREPRODUCTREPLY_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        yqmStoreProductReplyService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
