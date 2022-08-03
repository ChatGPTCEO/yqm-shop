/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.service.YqmStoreCouponUserService;
import com.yqm.modules.activity.service.dto.YqmStoreCouponUserQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author weiximei
* @date 2019-11-10
*/
@Api(tags = "商城:优惠券发放记录管理")
@RestController
@RequestMapping("api")
public class StoreCouponUserController {

    private final YqmStoreCouponUserService yqmStoreCouponUserService;

    public StoreCouponUserController(YqmStoreCouponUserService yqmStoreCouponUserService) {
        this.yqmStoreCouponUserService = yqmStoreCouponUserService;
    }

    @Log("查询Y")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmStoreCouponUser")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECOUPONUSER_ALL','YQMSTORECOUPONUSER_SELECT')")
    public ResponseEntity getYqmStoreCouponUsers(YqmStoreCouponUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity(yqmStoreCouponUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }


}
