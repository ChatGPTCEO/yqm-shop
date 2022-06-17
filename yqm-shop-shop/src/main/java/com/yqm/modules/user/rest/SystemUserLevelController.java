/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.user.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.shop.domain.YqmSystemUserLevel;
import com.yqm.modules.user.service.YqmSystemUserLevelService;
import com.yqm.modules.user.service.dto.YqmSystemUserLevelQueryCriteria;
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
* @date 2019-12-04
*/
@Api(tags = "商城:用户等级管理")
@RestController
@RequestMapping("api")
public class SystemUserLevelController {

    private final YqmSystemUserLevelService yqmSystemUserLevelService;

    public SystemUserLevelController(YqmSystemUserLevelService yqmSystemUserLevelService) {
        this.yqmSystemUserLevelService = yqmSystemUserLevelService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmSystemUserLevel")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERLEVEL_ALL','YQMSYSTEMUSERLEVEL_SELECT')")
    public ResponseEntity getYqmSystemUserLevels(YqmSystemUserLevelQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmSystemUserLevelService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/yqmSystemUserLevel")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERLEVEL_ALL','YQMSYSTEMUSERLEVEL_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YqmSystemUserLevel resources){
        return new ResponseEntity<>(yqmSystemUserLevelService.save(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/yqmSystemUserLevel")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERLEVEL_ALL','YQMSYSTEMUSERLEVEL_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmSystemUserLevel resources){
        yqmSystemUserLevelService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yqmSystemUserLevel/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERLEVEL_ALL','YQMSYSTEMUSERLEVEL_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        yqmSystemUserLevelService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
