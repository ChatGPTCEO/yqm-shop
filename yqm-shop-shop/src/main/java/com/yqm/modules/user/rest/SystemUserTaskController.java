/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.user.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.user.domain.YqmSystemUserTask;
import com.yqm.modules.user.service.YqmSystemUserTaskService;
import com.yqm.modules.user.service.dto.YqmSystemUserTaskQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
@Api(tags = "商城:用户任务管理")
@RestController
@RequestMapping("api")
public class SystemUserTaskController {

    private final YqmSystemUserTaskService yqmSystemUserTaskService;

    public SystemUserTaskController(YqmSystemUserTaskService yqmSystemUserTaskService) {
        this.yqmSystemUserTaskService = yqmSystemUserTaskService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmSystemUserTask")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERTASK_ALL','YQMSYSTEMUSERTASK_SELECT')")
    public ResponseEntity getYqmSystemUserTasks(YqmSystemUserTaskQueryCriteria criteria,
                                               Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.ASC, "level_id");
        Pageable pageableT = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);
        return new ResponseEntity(yqmSystemUserTaskService.queryAll(criteria,pageableT),
                HttpStatus.OK);
    }

    @Log("新增")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/yqmSystemUserTask")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERTASK_ALL','YQMSYSTEMUSERTASK_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YqmSystemUserTask resources){
        return new ResponseEntity(yqmSystemUserTaskService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改")
    @ApiOperation(value = "修改")
    @PutMapping(value = "/yqmSystemUserTask")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERTASK_ALL','YQMSYSTEMUSERTASK_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmSystemUserTask resources){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yqmSystemUserTaskService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yqmSystemUserTask/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMUSERTASK_ALL','YQMSYSTEMUSERTASK_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yqmSystemUserTaskService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
