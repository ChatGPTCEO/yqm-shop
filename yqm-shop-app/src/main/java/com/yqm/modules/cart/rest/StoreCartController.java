/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.cart.rest;

import com.yqm.annotation.Limit;
import com.yqm.api.ApiResult;
import com.yqm.logging.aop.log.AppLog;
import com.yqm.common.aop.NoRepeatSubmit;
import com.yqm.common.bean.LocalUser;
import com.yqm.common.interceptor.AuthCheck;
import com.yqm.modules.cart.param.CartIdsParm;
import com.yqm.modules.cart.param.CartNumParam;
import com.yqm.modules.cart.param.CartParam;
import com.yqm.modules.cart.service.YqmStoreCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 购物车控制器
 * </p>
 *
 * @author weiximei
 * @since 2019-10-25
 */
@Slf4j
@RestController
@Api(value = "购物车", tags = "商城:购物车")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreCartController {

    private final YqmStoreCartService storeCartService;

    /**
     * 购物车 获取数量
     */
    @AuthCheck
    @GetMapping("/cart/count")
    @ApiOperation(value = "获取数量",notes = "获取数量")
    public ApiResult<Map<String,Object>> count(){
        Map<String,Object> map = new LinkedHashMap<>();
        Long uid = LocalUser.getUser().getUid();
        map.put("count",storeCartService.getUserCartNum(uid));
        return ApiResult.ok(map);
    }

    /**
     * 购物车 添加
     */
    @AppLog(value = "购物车 添加", type = 1)
    @NoRepeatSubmit
    @AuthCheck
    @PostMapping("/cart/add")
    @ApiOperation(value = "添加购物车",notes = "添加购物车")
    @Limit(key = "cart_limit", period = 60, count = 30, name = "cartLimit", prefix = "yqm-shop")
    public ApiResult<Map<String,Object>> add(@Validated @RequestBody CartParam cartParam){
        Map<String,Object> map = new LinkedHashMap<>();
        Long uid = LocalUser.getUser().getUid();
        map.put("cartId",storeCartService.addCart(uid,cartParam.getProductId(),cartParam.getCartNum(),
                cartParam.getUniqueId(),cartParam.getIsNew(),cartParam.getCombinationId(),
                cartParam.getSecKillId(),cartParam.getBargainId()));
        return ApiResult.ok(map);
    }


    /**
     * 购物车列表
     */
    @AppLog(value = "查看购物车列表", type = 1)
    @AuthCheck
    @GetMapping("/cart/list")
    @ApiOperation(value = "购物车列表",notes = "购物车列表")
    public ApiResult<Map<String,Object>> getList(){
        Long uid = LocalUser.getUser().getUid();
        return ApiResult.ok(storeCartService.getUserProductCartList(uid,"",null));
    }

    /**
     * 修改产品数量
     */
    @AppLog(value = "修改购物车产品数量", type = 1)
    @AuthCheck
    @PostMapping("/cart/num")
    @ApiOperation(value = "修改产品数量",notes = "修改产品数量")
    public ApiResult<Boolean> cartNum(@Validated @RequestBody CartNumParam param){
        Long uid = LocalUser.getUser().getUid();
        storeCartService.changeUserCartNum(param.getId(), param.getNumber(),uid);
        return ApiResult.ok();
    }

    /**
     * 购物车删除产品
     */
    @AppLog(value = "购物车删除产品", type = 1)
    @NoRepeatSubmit
    @AuthCheck
    @PostMapping("/cart/del")
    @ApiOperation(value = "购物车删除产品",notes = "购物车删除产品")
    public ApiResult<Boolean> cartDel(@Validated @RequestBody CartIdsParm parm){
        Long uid = LocalUser.getUser().getUid();
        storeCartService.removeUserCart(uid, parm.getIds());
        return ApiResult.ok();
    }





}

