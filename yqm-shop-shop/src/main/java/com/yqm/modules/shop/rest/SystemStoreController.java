/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.shop.rest;

import com.yqm.dozer.service.IGenerator;
import com.yqm.exception.BadRequestException;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.shop.domain.YqmSystemStore;
import com.yqm.modules.shop.service.YqmSystemStoreService;
import com.yqm.modules.shop.service.dto.YqmSystemStoreDto;
import com.yqm.modules.shop.service.dto.YqmSystemStoreQueryCriteria;
import com.yqm.utils.location.GetTencentLocationVO;
import com.yqm.utils.location.LocationUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
* @date 2020-03-03
*/
@Api(tags = "门店管理")
@RestController
@RequestMapping("/api/yqmSystemStore")
public class SystemStoreController {

    private final YqmSystemStoreService yqmSystemStoreService;
    private final IGenerator generator;
    public SystemStoreController(YqmSystemStoreService yqmSystemStoreService, IGenerator generator) {
        this.yqmSystemStoreService = yqmSystemStoreService;
        this.generator = generator;
    }


    @Log("所有门店")
    @ApiOperation("所有门店")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('yqmSystemStore:list')")
    public ResponseEntity<Object>  getAll(YqmSystemStoreQueryCriteria criteria) {
        return new ResponseEntity<>(yqmSystemStoreService.queryAll(criteria),HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yqmSystemStore:list')")
    public void download(HttpServletResponse response, YqmSystemStoreQueryCriteria criteria) throws IOException {
        yqmSystemStoreService.download(generator.convert(yqmSystemStoreService.queryAll(criteria), YqmSystemStoreDto.class), response);
    }

    @GetMapping
    @Log("查询门店")
    @ApiOperation("查询门店")
    @PreAuthorize("@el.check('yqmSystemStore:list')")
    public ResponseEntity<Object> getYqmSystemStores(YqmSystemStoreQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmSystemStoreService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/getL")
    @Log("获取经纬度")
    @ApiOperation("获取经纬度")
    @PreAuthorize("@el.check('yqmSystemStore:getl')")
    public ResponseEntity<GetTencentLocationVO> create(@Validated @RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String addr = jsonObject.getString("addr");
        GetTencentLocationVO locationVO = LocationUtils.getLocation(addr);
        if(locationVO.getStatus()!=0){
            throw new BadRequestException(locationVO.getMessage());
        }
        return new ResponseEntity<>(locationVO, HttpStatus.CREATED);
    }

    @ForbidSubmit
    @PostMapping
    @Log("新增门店")
    @ApiOperation("新增门店")
    @PreAuthorize("@el.check('yqmSystemStore:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmSystemStore resources){
        return new ResponseEntity<>(yqmSystemStoreService.save(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @PutMapping
    @Log("修改门店")
    @ApiOperation("修改门店")
    @PreAuthorize("@el.check('yqmSystemStore:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YqmSystemStore resources){
        yqmSystemStoreService.saveOrUpdate(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除门店")
    @ApiOperation("删除门店")
    @PreAuthorize("@el.check('yqmSystemStore:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yqmSystemStoreService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
