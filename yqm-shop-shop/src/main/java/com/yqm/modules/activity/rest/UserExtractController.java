/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.domain.YqmUserExtract;
import com.yqm.modules.activity.service.YqmUserExtractService;
import com.yqm.modules.activity.service.dto.YqmUserExtractQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author weiximei
* @date 2019-11-14
*/
@Api(tags = "商城:提现管理")
@RestController
@RequestMapping("api")
public class UserExtractController {

    private final YqmUserExtractService yqmUserExtractService;


    public UserExtractController(YqmUserExtractService yqmUserExtractService) {
        this.yqmUserExtractService = yqmUserExtractService;

    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmUserExtract")
    @PreAuthorize("hasAnyRole('admin','YQMUSEREXTRACT_ALL','YQMUSEREXTRACT_SELECT')")
    public ResponseEntity getYqmUserExtracts(YqmUserExtractQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmUserExtractService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改审核")
    @ApiOperation(value = "修改审核")
    @PutMapping(value = "/yqmUserExtract")
    @PreAuthorize("hasAnyRole('admin','YQMUSEREXTRACT_ALL','YQMUSEREXTRACT_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmUserExtract resources){
        yqmUserExtractService.doExtract(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
