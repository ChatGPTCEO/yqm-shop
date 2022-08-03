/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.dto.YqmUserBillQueryCriteria;
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
* @date 2019-11-06
*/
@Api(tags = "商城:用户账单管理")
@RestController
@RequestMapping("api")
public class UserBillController {

    private final YqmUserBillService yqmUserBillService;

    public UserBillController(YqmUserBillService yqmUserBillService) {
        this.yqmUserBillService = yqmUserBillService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmUserBill")
    @PreAuthorize("hasAnyRole('admin','YQMUSERBILL_ALL','YQMUSERBILL_SELECT')")
    public ResponseEntity getYqmUserBills(YqmUserBillQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmUserBillService.queryAll(criteria,pageable),HttpStatus.OK);
    }






}
