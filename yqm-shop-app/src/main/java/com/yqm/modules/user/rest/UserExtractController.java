/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.rest;


import com.yqm.api.ApiResult;
import com.yqm.logging.aop.log.AppLog;
import com.yqm.common.bean.LocalUser;
import com.yqm.common.interceptor.AuthCheck;
import com.yqm.constant.SystemConfigConstants;
import com.yqm.modules.activity.param.UserExtParam;
import com.yqm.modules.activity.service.YqmUserExtractService;
import com.yqm.modules.shop.service.YqmSystemConfigService;
import com.yqm.modules.user.domain.YqmUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 用户提现 前端控制器
 * </p>
 *
 * @author weiximei
 * @since 2019-11-11
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户提现", tags = "用户:用户提现")
public class UserExtractController {

    private final YqmUserExtractService userExtractService;
    private final YqmSystemConfigService systemConfigService;

    /**
     * 提现参数
     */
    @AuthCheck
    @GetMapping("/extract/bank")
    @ApiOperation(value = "提现参数",notes = "提现参数")
    public ApiResult<Object> bank(){
        YqmUser yqmUser = LocalUser.getUser();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("commissionCount",yqmUser.getBrokeragePrice());
        map.put("minPrice",systemConfigService.getData(SystemConfigConstants.USER_EXTRACT_MIN_PRICE));
        return ApiResult.ok(map);
    }


    /**
    * 用户提现
    */
    @AppLog(value = "用户提现", type = 1)
    @AuthCheck
    @PostMapping("/extract/cash")
    @ApiOperation(value = "用户提现",notes = "用户提现")
    public ApiResult<String> addYqmUserExtract(@Valid @RequestBody UserExtParam param){
        YqmUser yqmUser = LocalUser.getUser();
        userExtractService.userExtract(yqmUser,param);
        return ApiResult.ok("申请提现成功");
    }




}

