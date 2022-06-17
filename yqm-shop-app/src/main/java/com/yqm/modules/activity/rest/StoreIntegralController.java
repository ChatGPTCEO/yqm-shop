package com.yqm.modules.activity.rest;

import com.yqm.api.ApiResult;
import com.yqm.modules.product.param.YqmStoreProductQueryParam;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.vo.YqmStoreProductQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 积分兑换前端控制器
 * @author yqm-shop
 */
@Slf4j
@RestController
@RequestMapping
@Api(value = "积分兑换", tags = "营销:积分兑换")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StoreIntegralController {

    private final YqmStoreProductService storeProductService;

    /**
     * 获取积分产品列表
     */
    @GetMapping("/products/integral")
    @ApiOperation(value = "获取积分产品列表",notes = "获取积分产品列表")
    public ApiResult<List<YqmStoreProductQueryVo>> goodsList(YqmStoreProductQueryParam productQueryParam){
        return ApiResult.ok(storeProductService.getGoodsList(productQueryParam));
    }
}
