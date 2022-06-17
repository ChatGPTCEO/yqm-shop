/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.activity.rest;

import cn.hutool.core.util.StrUtil;
import com.yqm.api.YqmShopException;
import com.yqm.enums.CouponEnum;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.domain.YqmStoreCoupon;
import com.yqm.modules.activity.service.YqmStoreCouponService;
import com.yqm.modules.activity.service.dto.YqmStoreCouponQueryCriteria;
import com.yqm.modules.aop.ForbidSubmit;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author weiximei
* @date 2019-11-09
*/
@Api(tags = "商城:优惠券管理")
@RestController
@RequestMapping("api")
public class StoreCouponController {

    private final YqmStoreCouponService yqmStoreCouponService;

    public StoreCouponController(YqmStoreCouponService yqmStoreCouponService) {
        this.yqmStoreCouponService = yqmStoreCouponService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmStoreCoupon")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPON_ALL','YQMSTORECOUPON_SELECT')")
    public ResponseEntity getYqmStoreCoupons(YqmStoreCouponQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreCouponService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/yqmStoreCoupon")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPON_ALL','YQMSTORECOUPON_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YqmStoreCoupon resources){
        if(CouponEnum.TYPE_1.getValue().equals(resources.getType())
                && StrUtil.isEmpty(resources.getProductId())){
            throw new YqmShopException("请选择商品");
        }
        if(resources.getCouponPrice().compareTo(resources.getUseMinPrice()) >= 0) {
            throw new YqmShopException("优惠券金额不能高于最低消费金额");
        }
        return new ResponseEntity<>(yqmStoreCouponService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/yqmStoreCoupon")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPON_ALL','YQMSTORECOUPON_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmStoreCoupon resources){
        if(CouponEnum.TYPE_1.getValue().equals(resources.getType())
                && StrUtil.isEmpty(resources.getProductId())){
            throw new YqmShopException("请选择商品");
        }
        if(resources.getCouponPrice().compareTo(resources.getUseMinPrice()) >= 0) {
            throw new YqmShopException("优惠券金额不能高于最低消费金额");
        }
        yqmStoreCouponService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yqmStoreCoupon/{id}")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPON_ALL','YQMSTORECOUPON_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        yqmStoreCouponService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
