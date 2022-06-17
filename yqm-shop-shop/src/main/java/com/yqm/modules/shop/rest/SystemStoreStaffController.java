/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.rest;

import com.yqm.dozer.service.IGenerator;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.shop.domain.YqmSystemStore;
import com.yqm.modules.shop.domain.YqmSystemStoreStaff;
import com.yqm.modules.shop.service.YqmSystemStoreService;
import com.yqm.modules.shop.service.YqmSystemStoreStaffService;
import com.yqm.modules.shop.service.dto.YqmSystemStoreStaffDto;
import com.yqm.modules.shop.service.dto.YqmSystemStoreStaffQueryCriteria;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
* @author weiximei
* @date 2020-03-22
*/
@Api(tags = "门店店员管理")
@RestController
@RequestMapping("/api/yqmSystemStoreStaff")
public class SystemStoreStaffController {

    private final YqmSystemStoreStaffService yqmSystemStoreStaffService;
    private final YqmSystemStoreService yqmSystemStoreService;

    private final IGenerator generator;

    public SystemStoreStaffController(YqmSystemStoreService yqmSystemStoreService,YqmSystemStoreStaffService yqmSystemStoreStaffService, IGenerator generator) {
        this.yqmSystemStoreService = yqmSystemStoreService;
        this.yqmSystemStoreStaffService = yqmSystemStoreStaffService;
        this.generator = generator;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yqmSystemStoreStaff:list')")
    public void download(HttpServletResponse response, YqmSystemStoreStaffQueryCriteria criteria) throws IOException {
        yqmSystemStoreStaffService.download(generator.convert(yqmSystemStoreStaffService.queryAll(criteria), YqmSystemStoreStaffDto.class), response);
    }

    @GetMapping
    @Log("查询门店店员")
    @ApiOperation("查询门店店员")
    @PreAuthorize("@el.check('yqmSystemStoreStaff:list')")
    public ResponseEntity<Object> getYqmSystemStoreStaffs(YqmSystemStoreStaffQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmSystemStoreStaffService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增门店店员")
    @ApiOperation("新增门店店员")
    @PreAuthorize("@el.check('yqmSystemStoreStaff:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmSystemStoreStaff resources){
        YqmSystemStore systemStore = yqmSystemStoreService.getOne(Wrappers.<YqmSystemStore>lambdaQuery()
                .eq(YqmSystemStore::getId,resources.getStoreId()));
        resources.setStoreName(systemStore.getName());
        return new ResponseEntity<>(yqmSystemStoreStaffService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改门店店员")
    @ApiOperation("修改门店店员")
    @PreAuthorize("@el.check('yqmSystemStoreStaff:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YqmSystemStoreStaff resources){
        YqmSystemStore systemStore = yqmSystemStoreService.getOne(Wrappers.<YqmSystemStore>lambdaQuery()
                .eq(YqmSystemStore::getId,resources.getStoreId()));
        resources.setStoreName(systemStore.getName());
        yqmSystemStoreStaffService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除门店店员")
    @ApiOperation("删除门店店员")
    @PreAuthorize("@el.check('yqmSystemStoreStaff:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yqmSystemStoreStaffService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
