/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.wechat.rest;

import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.mp.domain.YqmWechatTemplate;
import com.yqm.modules.mp.service.YqmWechatTemplateService;
import com.yqm.modules.mp.service.dto.YqmWechatTemplateDto;
import com.yqm.modules.mp.service.dto.YqmWechatTemplateQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @author weiximei
* @date 2019-12-10
*/
@Api(tags = "商城:微信模板管理")
@RestController
@RequestMapping("/api/yqmWechatTemplate")
@AllArgsConstructor
public class WechatTemplateController {


    private final YqmWechatTemplateService yqmWechatTemplateService;
    private final IGenerator generator;


    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmWechatTemplate:list')")
    public void download(HttpServletResponse response, YqmWechatTemplateQueryCriteria criteria) throws IOException {
        yqmWechatTemplateService.download(generator.convert(yqmWechatTemplateService.queryAll(criteria), YqmWechatTemplateDto.class), response);
    }

    @GetMapping
    @ApiOperation("查询微信模板消息")
    @PreAuthorize("@el.check('admin','yqmWechatTemplate:list')")
    public ResponseEntity<Object> getYqmWechatTemplates(YqmWechatTemplateQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmWechatTemplateService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @PostMapping
    @ApiOperation("新增微信模板消息")
    @PreAuthorize("@el.check('admin','yqmWechatTemplate:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmWechatTemplate resources){
        return new ResponseEntity<>(yqmWechatTemplateService.save(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @PutMapping
    @ApiOperation("修改微信模板消息")
    @PreAuthorize("@el.check('admin','yqmWechatTemplate:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YqmWechatTemplate resources){
        yqmWechatTemplateService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @ApiOperation("删除微信模板消息")
    @PreAuthorize("@el.check('admin','yqmWechatTemplate:del')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAll(@PathVariable Integer id) {
        yqmWechatTemplateService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
