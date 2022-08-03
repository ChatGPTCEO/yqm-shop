/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.shop.rest;

import com.yqm.domain.PageResult;
import com.yqm.dozer.service.IGenerator;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.shop.domain.YqmAppVersion;
import com.yqm.modules.shop.service.YqmAppVersionService;
import com.yqm.modules.shop.service.dto.YqmAppVersionDto;
import com.yqm.modules.shop.service.dto.YqmAppVersionQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
* @author lioncity
* @date 2020-12-09
*/
@AllArgsConstructor
@Api(tags = "app版本控制管理")
@RestController
@RequestMapping("/api/yqmAppVersion")
public class YqmAppVersionController {

    private final YqmAppVersionService yqmAppVersionService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmAppVersion:list')")
    public void download(HttpServletResponse response, YqmAppVersionQueryCriteria criteria) throws IOException {
        yqmAppVersionService.download(generator.convert(yqmAppVersionService.queryAll(criteria), YqmAppVersionDto.class), response);
    }

    @GetMapping
    @Log("查询app版本控制")
    @ApiOperation("查询app版本控制")
    @PreAuthorize("@el.check('admin','yqmAppVersion:list')")
    public ResponseEntity<PageResult<YqmAppVersionDto>> getYqmAppVersions(YqmAppVersionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmAppVersionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @PostMapping
    @Log("新增app版本控制")
    @ApiOperation("新增app版本控制")
    @PreAuthorize("@el.check('admin','yqmAppVersion:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmAppVersion resources){
        return new ResponseEntity<>(yqmAppVersionService.save(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @PutMapping
    @Log("修改app版本控制")
    @ApiOperation("修改app版本控制")
    @PreAuthorize("@el.check('admin','yqmAppVersion:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YqmAppVersion resources){
        yqmAppVersionService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除app版本控制")
    @ApiOperation("删除app版本控制")
    @PreAuthorize("@el.check('admin','yqmAppVersion:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        Arrays.asList(ids).forEach(id->{
            yqmAppVersionService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
