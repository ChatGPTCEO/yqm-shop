/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.wechat.rest.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.ApiResult;
import com.yqm.api.YqmShopException;
import com.yqm.constant.ShopConstants;
import com.yqm.logging.aop.log.AppLog;
import com.yqm.common.bean.LocalUser;
import com.yqm.common.interceptor.AuthCheck;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.wechat.rest.param.BindPhoneParam;
import com.yqm.modules.wechat.rest.param.WxPhoneParam;
import com.yqm.modules.mp.config.WxMaConfiguration;
import com.yqm.utils.RedisUtil;
import com.yqm.utils.RedisUtils;
import com.yqm.utils.ShopKeyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author weiximei
 * @date 2020/02/07
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "微信其他", tags = "微信:微信其他")
public class WxMaUserController {

    private final YqmUserService userService;
    private final RedisUtils redisUtils;

    @AppLog(value = "公众号绑定手机号", type = 1)
    @AuthCheck
    @PostMapping("/binding")
    @ApiOperation(value = "公众号绑定手机号", notes = "公众号绑定手机号")
    public ApiResult<String> verify(@Validated @RequestBody BindPhoneParam param) {
        Object codeObj = redisUtils.get("code_" + param.getPhone());
        if(codeObj == null){
            return ApiResult.fail("请先获取验证码");
        }
        String code = codeObj.toString();

        if (!StrUtil.equals(code, param.getCaptcha())) {
            return ApiResult.fail("验证码错误");
        }
        YqmUser user = LocalUser.getUser();
        if(StrUtil.isNotBlank(user.getPhone())){
            return ApiResult.fail("您的账号已经绑定过手机号码");
        }

        user.setPhone(param.getPhone());
        userService.updateById(user);

        return ApiResult.ok("绑定成功");

    }

    @AppLog(value = "小程序绑定手机号", type = 1)
    @AuthCheck
    @PostMapping("/wxapp/binding")
    @ApiOperation(value = "小程序绑定手机号", notes = "小程序绑定手机号")
    public ApiResult<Map<String,Object>> phone(@Validated @RequestBody WxPhoneParam param) {
        YqmUser user = LocalUser.getUser();
        if(StrUtil.isNotBlank(user.getPhone())){
            throw new YqmShopException("您的账号已经绑定过手机号码");
        }

        //读取redis配置
        String appId = redisUtils.getY(ShopKeyUtils.getWxAppAppId());
        String secret = redisUtils.getY(ShopKeyUtils.getWxAppSecret());
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
            throw new YqmShopException("请先配置小程序");
        }
        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        String phone = "";
        try {
            String sessionKey = RedisUtil.get(ShopConstants.YQM_SHOP_MINI_SESSION_KET+ user.getUid()).toString();
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService()
                    .getPhoneNoInfo(sessionKey, param.getEncryptedData(), param.getIv());
            phone = phoneNoInfo.getPhoneNumber();
            user.setPhone(phone);
            userService.updateById(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new YqmShopException("绑定失败");
        }
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("phone",phone);

        return ApiResult.ok(map,"绑定成功");
    }



}
