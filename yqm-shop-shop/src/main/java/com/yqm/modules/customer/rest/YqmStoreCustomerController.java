/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.customer.rest;
import java.util.Arrays;
import com.yqm.dozer.service.IGenerator;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.aop.ForbidSubmit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.customer.domain.YqmStoreCustomer;
import com.yqm.modules.customer.service.YqmStoreCustomerService;
import com.yqm.modules.customer.service.dto.YqmStoreCustomerQueryCriteria;
import com.yqm.modules.customer.service.dto.YqmStoreCustomerDto;
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
* @author Bug
* @date 2020-12-10
*/
@AllArgsConstructor
@Api(tags = "customer管理")
@RestController
@RequestMapping("/api/yqmStoreCustomer")
public class YqmStoreCustomerController {

    private final YqmStoreCustomerService yqmStoreCustomerService;
    private final IGenerator generator;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmStoreCustomer:list')")
    public void download(HttpServletResponse response, YqmStoreCustomerQueryCriteria criteria) throws IOException {
        yqmStoreCustomerService.download(generator.convert(yqmStoreCustomerService.queryAll(criteria), YqmStoreCustomerDto.class), response);
    }

    @GetMapping
    @Log("查询customer")
    @ApiOperation("查询customer")
    @PreAuthorize("@el.check('admin','yqmStoreCustomer:list')")
    public ResponseEntity<PageResult<YqmStoreCustomerDto>> getYqmStoreCustomers(YqmStoreCustomerQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreCustomerService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @PostMapping
    @Log("新增customer")
    @ApiOperation("新增customer")
    @PreAuthorize("@el.check('admin','yqmStoreCustomer:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmStoreCustomer resources){
        int count = yqmStoreCustomerService.count(new LambdaQueryWrapper<YqmStoreCustomer>().eq(YqmStoreCustomer::getOpenId, resources.getOpenId()));
        if (count > 0) {
            throw new BadRequestException("当前用户已存在，请勿重复提交");
        }
        return new ResponseEntity<>(yqmStoreCustomerService.save(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @PutMapping
    @Log("修改customer")
    @ApiOperation("修改customer")
    @PreAuthorize("@el.check('admin','yqmStoreCustomer:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YqmStoreCustomer resources){
        yqmStoreCustomerService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除customer")
    @ApiOperation("删除customer")
    @PreAuthorize("@el.check('admin','yqmStoreCustomer:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
            yqmStoreCustomerService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
