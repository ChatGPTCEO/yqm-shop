/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.order.domain.YqmExpress;
import com.yqm.modules.order.service.YqmExpressService;
import com.yqm.modules.order.service.dto.YqmExpressQueryCriteria;
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
* @date 2019-12-12
*/
@Api(tags = "商城:快递管理")
@RestController
@RequestMapping("api")
public class ExpressController {


    private final YqmExpressService yqmExpressService;

    public ExpressController(YqmExpressService yqmExpressService) {
        this.yqmExpressService = yqmExpressService;
    }

    @Log("查询快递")
    @ApiOperation(value = "查询快递")
    @GetMapping(value = "/yqmExpress")
    @PreAuthorize("hasAnyRole('admin','YQMEXPRESS_ALL','YQMEXPRESS_SELECT')")
    public ResponseEntity getYqmExpresss(YqmExpressQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmExpressService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增快递")
    @ApiOperation(value = "新增快递")
    @PostMapping(value = "/yqmExpress")
    @PreAuthorize("hasAnyRole('admin','YQMEXPRESS_ALL','YQMEXPRESS_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YqmExpress resources){
        return new ResponseEntity<>(yqmExpressService.save(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @Log("修改快递")
    @ApiOperation(value = "修改快递")
    @PutMapping(value = "/yqmExpress")
    @PreAuthorize("hasAnyRole('admin','YQMEXPRESS_ALL','YQMEXPRESS_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmExpress resources){
        yqmExpressService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除快递")
    @ApiOperation(value = "删除快递")
    @DeleteMapping(value = "/yqmExpress/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMEXPRESS_ALL','YQMEXPRESS_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        yqmExpressService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
