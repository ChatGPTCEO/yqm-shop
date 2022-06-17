/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.user.rest;

import com.yqm.dozer.service.IGenerator;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.user.service.YqmUserRechargeService;
import com.yqm.modules.user.service.dto.YqmUserRechargeDto;
import com.yqm.modules.user.service.dto.YqmUserRechargeQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
* @author weiximei
* @date 2020-03-02
*/
@Api(tags = "充值管理管理")
@RestController
@RequestMapping("/api/yqmUserRecharge")
public class UserRechargeController {

    private final YqmUserRechargeService yqmUserRechargeService;
    private final IGenerator generator;
    public UserRechargeController(YqmUserRechargeService yqmUserRechargeService, IGenerator generator) {
        this.yqmUserRechargeService = yqmUserRechargeService;
        this.generator = generator;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('yqmUserRecharge:list')")
    public void download(HttpServletResponse response, YqmUserRechargeQueryCriteria criteria) throws IOException {
        yqmUserRechargeService.download(generator.convert(yqmUserRechargeService.queryAll(criteria), YqmUserRechargeDto.class), response);
    }

    @GetMapping
    @Log("查询充值管理")
    @ApiOperation("查询充值管理")
    @PreAuthorize("@el.check('yqmUserRecharge:list')")
    public ResponseEntity<Object> getYqmUserRecharges(YqmUserRechargeQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmUserRechargeService.queryAll(criteria,pageable),HttpStatus.OK);
    }




    @ForbidSubmit
    @Log("删除充值管理")
    @ApiOperation("删除充值管理")
    @PreAuthorize("@el.check('yqmUserRecharge:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        yqmUserRechargeService.removeByIds(new ArrayList<>(Arrays.asList(ids)));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
