/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.product.rest;

import com.yqm.dozer.service.IGenerator;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.product.domain.YqmStoreProductRule;
import com.yqm.modules.product.service.YqmStoreProductRuleService;
import com.yqm.modules.product.service.dto.YqmStoreProductRuleQueryCriteria;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
* @author weiximei
* @date 2020-06-28
*/
@AllArgsConstructor
@Api(tags = "sku规则管理")
@RestController
@RequestMapping("/api/yqmStoreProductRule")
public class StoreProductRuleController {

    private final YqmStoreProductRuleService yqmStoreProductRuleService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmStoreProductRule:list')")
    public void download(HttpServletResponse response, YqmStoreProductRuleQueryCriteria criteria) throws IOException {
        yqmStoreProductRuleService.download(yqmStoreProductRuleService.queryAll(criteria) , response);
    }

    @GetMapping
    @Log("查询sku规则")
    @ApiOperation("查询sku规则")
    @PreAuthorize("@el.check('admin','yqmStoreProductRule:list')")
    public ResponseEntity<Object> getYqmStoreProductRules(YqmStoreProductRuleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreProductRuleService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @PostMapping("/save/{id}")
    @Log("新增/修改sku规则")
    @ApiOperation("新增/修改sku规则")
    @PreAuthorize("hasAnyRole('admin','yqmStoreProductRule:add','yqmStoreProductRule:edit')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmStoreProductRule resources,@PathVariable Integer id){
        if(id != null && id > 0){
            resources.setId(id);
            yqmStoreProductRuleService.updateById(resources);
        }else{
            yqmStoreProductRuleService.save(resources);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ForbidSubmit
    @Log("删除sku规则")
    @ApiOperation("删除sku规则")
    @PreAuthorize("@el.check('admin','yqmStoreProductRule:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yqmStoreProductRuleService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
