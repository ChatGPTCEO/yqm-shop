/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.rest;
import java.util.Arrays;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.aop.ForbidSubmit;
import lombok.AllArgsConstructor;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.product.domain.YqmStoreProductRelation;
import com.yqm.modules.product.service.YqmStoreProductRelationService;
import com.yqm.modules.product.service.dto.YqmStoreProductRelationQueryCriteria;
import com.yqm.modules.product.service.dto.YqmStoreProductRelationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.yqm.domain.PageResult;
/**
 * @author weiximei
 * @date 2020-09-03
 */
@AllArgsConstructor
@Api(tags = "ProductRelation管理")
@RestController
@RequestMapping("/api/yqmStoreProductRelation")
public class YqmStoreProductRelationController {

    private final YqmStoreProductRelationService yqmStoreProductRelationService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmStoreProductRelation:list')")
    public void download(HttpServletResponse response, YqmStoreProductRelationQueryCriteria criteria) throws IOException {
        yqmStoreProductRelationService.download(generator.convert(yqmStoreProductRelationService.queryAll(criteria), YqmStoreProductRelationDto.class), response);
    }

    @GetMapping
    @Log("查询ProductRelation")
    @ApiOperation("查询ProductRelation")
    @PreAuthorize("@el.check('admin','yqmStoreProductRelation:list')")
    public ResponseEntity<PageResult<YqmStoreProductRelationDto>> getYqmStoreProductRelations(YqmStoreProductRelationQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreProductRelationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增ProductRelation")
    @ApiOperation("新增ProductRelation")
    @PreAuthorize("@el.check('admin','yqmStoreProductRelation:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmStoreProductRelation resources){
        return new ResponseEntity<>(yqmStoreProductRelationService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改ProductRelation")
    @ApiOperation("修改ProductRelation")
    @PreAuthorize("@el.check('admin','yqmStoreProductRelation:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YqmStoreProductRelation resources){
        yqmStoreProductRelationService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除ProductRelation")
    @ApiOperation("删除ProductRelation")
    @PreAuthorize("@el.check('admin','yqmStoreProductRelation:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            yqmStoreProductRelationService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
