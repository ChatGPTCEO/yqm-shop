/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.rest;

import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.user.service.dto.UserMoneyDto;
import com.yqm.modules.user.service.dto.YqmUserQueryCriteria;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author weiximei
* @date 2019-10-06
*/
@Api(tags = "商城:会员管理")
@RestController
@RequestMapping("api")
public class MemberController {

    private final YqmUserService yqmUserService;

    public MemberController(YqmUserService yqmUserService) {
        this.yqmUserService = yqmUserService;
    }

    @Log("查看下级")
    @ApiOperation(value = "查看下级")
    @PostMapping(value = "/yqmUser/spread")
    @PreAuthorize("hasAnyRole('admin','YQMUSER_ALL','YQMUSER_EDIT')")
    public ResponseEntity getSpread(@RequestBody YqmUserQueryCriteria criteria){
        return new ResponseEntity<>(yqmUserService.querySpread(criteria.getUid(),criteria.getGrade()),
                HttpStatus.OK);
    }

    @Log("查询用户")
    @ApiOperation(value = "查询用户")
    @GetMapping(value = "/yqmUser")
    @PreAuthorize("hasAnyRole('admin','YQMUSER_ALL','YQMUSER_SELECT')")
    public ResponseEntity getYqmUsers(YqmUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @Log("修改用户")
    @ApiOperation(value = "修改用户")
    @PutMapping(value = "/yqmUser")
    @PreAuthorize("hasAnyRole('admin','YQMUSER_ALL','YQMUSER_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmUser resources){
        yqmUserService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除用户")
    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/yqmUser/{uid}")
    @PreAuthorize("hasAnyRole('admin','YQMUSER_ALL','YQMUSER_DELETE')")
    public ResponseEntity delete(@PathVariable Integer uid){
        yqmUserService.removeById(uid);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ForbidSubmit
    @ApiOperation(value = "用户禁用启用")
    @PostMapping(value = "/yqmUser/onStatus/{id}")
    public ResponseEntity onStatus(@PathVariable Long id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer status = jsonObject.getInteger("status");
        yqmUserService.onStatus(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "修改余额")
    @PostMapping(value = "/yqmUser/money")
    @PreAuthorize("hasAnyRole('admin','YQMUSER_ALL','YQMUSER_EDIT')")
    public ResponseEntity updatePrice(@Validated @RequestBody UserMoneyDto param){
        yqmUserService.updateMoney(param);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
