/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.activity.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.domain.YqmStoreCouponIssue;
import com.yqm.modules.activity.service.YqmStoreCouponIssueService;
import com.yqm.modules.activity.service.dto.YqmStoreCouponIssueQueryCriteria;
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
@Api(tags = "商城:优惠券发布管理")
@RestController
@RequestMapping("api")
public class StoreCouponIssueController {

    private final YqmStoreCouponIssueService yqmStoreCouponIssueService;

    public StoreCouponIssueController(YqmStoreCouponIssueService yqmStoreCouponIssueService) {
        this.yqmStoreCouponIssueService = yqmStoreCouponIssueService;
    }

    @Log("查询已发布")
    @ApiOperation(value = "查询已发布")
    @GetMapping(value = "/yqmStoreCouponIssue")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPONISSUE_ALL','YQMSTORECOUPONISSUE_SELECT')")
    public ResponseEntity getYqmStoreCouponIssues(YqmStoreCouponIssueQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreCouponIssueService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("发布")
    @ApiOperation(value = "发布")
    @PostMapping(value = "/yqmStoreCouponIssue")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPONISSUE_ALL','YQMSTORECOUPONISSUE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YqmStoreCouponIssue resources){
        if(resources.getTotalCount() > 0) {
            resources.setRemainCount(resources.getTotalCount());
        }
        return new ResponseEntity<>(yqmStoreCouponIssueService.save(resources),HttpStatus.CREATED);
    }

    @Log("修改状态")
    @ApiOperation(value = "修改状态")
    @PutMapping(value = "/yqmStoreCouponIssue")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPONISSUE_ALL','YQMSTORECOUPONISSUE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmStoreCouponIssue resources){
        yqmStoreCouponIssueService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yqmStoreCouponIssue/{id}")
    @PreAuthorize("@el.check('admin','YQMSTORECOUPONISSUE_ALL','YQMSTORECOUPONISSUE_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        yqmStoreCouponIssueService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
