/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.activity.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.service.YqmStorePinkService;
import com.yqm.modules.activity.service.dto.YqmStorePinkQueryCriteria;
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
* @date 2019-11-18
*/
@Api(tags = "商城:拼团记录管理")
@RestController
@RequestMapping("api")
public class StorePinkController {

    private final YqmStorePinkService yqmStorePinkService;

    public StorePinkController(YqmStorePinkService yqmStorePinkService) {
        this.yqmStorePinkService = yqmStorePinkService;
    }

    @Log("查询记录")
    @ApiOperation(value = "查询记录")
    @GetMapping(value = "/yqmStorePink")
    @PreAuthorize("@el.check('admin','YQMSTOREPINK_ALL','YQMSTOREPINK_SELECT')")
    public ResponseEntity getYqmStorePinks(YqmStorePinkQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStorePinkService.queryAll(criteria,pageable),HttpStatus.OK);
    }


}
