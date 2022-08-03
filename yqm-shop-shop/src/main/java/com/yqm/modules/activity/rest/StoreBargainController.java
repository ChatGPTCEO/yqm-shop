/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.rest;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.api.YqmShopException;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.domain.YqmStoreBargain;
import com.yqm.modules.activity.service.YqmStoreBargainService;
import com.yqm.modules.activity.service.dto.YqmStoreBargainQueryCriteria;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author weiximei
* @date 2019-12-22
*/
@Api(tags = "商城:砍价管理")
@RestController
@RequestMapping("api")
public class StoreBargainController {

    private final YqmStoreBargainService yqmStoreBargainService;

    public StoreBargainController(YqmStoreBargainService yqmStoreBargainService) {
        this.yqmStoreBargainService = yqmStoreBargainService;
    }

    @Log("查询砍价")
    @ApiOperation(value = "查询砍价")
    @GetMapping(value = "/yqmStoreBargain")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREBARGAIN_ALL','YQMSTOREBARGAIN_SELECT')")
    public ResponseEntity getYqmStoreBargains(YqmStoreBargainQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreBargainService.queryAll(criteria,pageable),HttpStatus.OK);
    }



    @Log("修改砍价")
    @ApiOperation(value = "修改砍价")
    @PutMapping(value = "/yqmStoreBargain")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREBARGAIN_ALL','YQMSTOREBARGAIN_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmStoreBargain resources){
        if(resources.getBargainMinPrice().compareTo(resources.getBargainMaxPrice()) >= 0){
            throw new YqmShopException("单次砍最低价不能高于单次砍最高价");
        }
        if(resources.getMinPrice().compareTo(resources.getPrice()) >= 0){
            throw new YqmShopException("允许砍到最低价不能高于砍价金额");
        }
        if(ObjectUtil.isNull(resources.getId())){
            return new ResponseEntity<>(yqmStoreBargainService.save(resources),HttpStatus.CREATED);
        }else{
            yqmStoreBargainService.saveOrUpdate(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @ForbidSubmit
    @Log("删除砍价")
    @ApiOperation(value = "删除砍价")
    @DeleteMapping(value = "/yqmStoreBargain/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREBARGAIN_ALL','YQMSTOREBARGAIN_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        yqmStoreBargainService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
