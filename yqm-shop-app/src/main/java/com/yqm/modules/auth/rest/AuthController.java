/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.auth.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.yqm.api.ApiResult;
import com.yqm.api.YqmShopException;
import com.yqm.common.bean.LocalUser;
import com.yqm.common.enums.SmsTypeEnum;
import com.yqm.common.interceptor.AuthCheck;
import com.yqm.common.util.JwtToken;
import com.yqm.common.util.SmsUtils;
import com.yqm.constant.ShopConstants;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.auth.param.*;
import com.yqm.modules.services.AuthService;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.utils.RedisUtil;
import com.yqm.utils.RedisUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName 认证服务
 * @Author weiximei <610796224@qq.com>
 * @Date 2020/4/30
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "认证模块", tags = "商城:认证")
public class AuthController {

    private final YqmUserService userService;
    private final RedisUtils redisUtil;
    private final AuthService authService;

    @Value("${single.login}")
    private Boolean singleLogin;



    /**
     * 小程序登陆接口
     */
    @PostMapping("/wxapp/auth")
    @ApiOperation(value = "小程序登陆", notes = "小程序登陆")
    public ApiResult<Map<String, Object>> login(@Validated @RequestBody LoginParam loginParam,
                                                HttpServletRequest request) {

        YqmUser yqmUser = authService.wxappLogin(loginParam);
        String token = JwtToken.makeToken(yqmUser.getUid(), yqmUser.getUsername());
        String expiresTimeStr = JwtToken.getExpireTime(token);

        // 返回 token
        Map<String, Object> map = new LinkedHashMap<>(2);

        map.put("token", token);
        map.put("expires_time", expiresTimeStr);

        // 保存在线信息
        authService.save(yqmUser, token, request);
        if (singleLogin) {
            authService.checkLoginOnUser(yqmUser.getUsername(), token);
        }


        return ApiResult.ok(map).setMsg("登陆成功");

    }

    /**
     * 根据手机号查询用户状态
     */
    @GetMapping("/wxapp/authPhone/{phone}")
    @ApiOperation(value = "根据手机号查询用户状态", notes = "根据手机号查询用户状态")
    public ApiResult<YqmUser> authPhone(@PathVariable String phone) {
        return ApiResult.ok(authService.authPhone(phone)).setMsg("获取成功");
    }

    @AuthCheck
    @PostMapping("/wxapp/loginAuth")
    @ApiOperation(value = "小程序获取用户信息", notes = "小程序获取用户信息")
    public ApiResult<YqmUser> loginAuth(@Validated @RequestBody LoginParam loginParam) {
        Long uid = LocalUser.getUidByToken();
        String sessionKey = RedisUtil.get(ShopConstants.YQM_SHOP_MINI_SESSION_KET+ uid).toString();
        YqmUser yqmUser = authService.loginAuth(loginParam, uid, sessionKey);
        return ApiResult.ok(yqmUser).setMsg("获取成功");

    }
    /**
     * 微信公众号授权
     */
    @GetMapping("/wechat/auth")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信授权code", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "spread", value = "分销绑定关系的ID", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "微信公众号授权", notes = "微信公众号授权")
    public ApiResult<Map<String, Object>> authLogin(@RequestParam(value = "code") String code,
                                                    @RequestParam(value = "spread") String spread,
                                                    HttpServletRequest request) {

            YqmUser yqmUser = authService.wechatLogin(code,spread);
            String token =  JwtToken.makeToken(yqmUser.getUid(),yqmUser.getUsername());
            String expiresTimeStr = JwtToken.getExpireTime(token);

            // 返回 token
            Map<String, Object> map = new HashMap<String, Object>(2) {{
                put("token", token);
                put("expires_time", expiresTimeStr);
            }};

            // 保存在线信息
            authService.save(yqmUser, token, request);
            if(singleLogin){
                authService.checkLoginOnUser(yqmUser.getUsername(),token);
            }

            return ApiResult.ok(map).setMsg("登陆成功");


    }


    @ApiOperation("H5登录授权")
    @PostMapping(value = "/login")
    public ApiResult<Map<String, Object>> login(@Validated @RequestBody HLoginParam loginDTO,HttpServletRequest request) {
        YqmUser yqmUser = userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getUsername,loginDTO.getUsername())
                .eq(YqmUser::getPassword,SecureUtil.md5(loginDTO.getPassword())),false);

        if(yqmUser == null) {
            throw new YqmShopException("账号或者密码不正确");
        }

        String token =  JwtToken.makeToken(yqmUser.getUid(),yqmUser.getUsername());
        String expiresTimeStr = JwtToken.getExpireTime(token);

        // 保存在线信息
        authService.save(yqmUser, token, request);
        // 返回 token
        Map<String, Object> map = new HashMap<String, Object>(2) {{
            put("token", token);
            put("expires_time", expiresTimeStr);
        }};

        userService.setSpread(loginDTO.getSpread(),yqmUser.getUid());

        if(singleLogin){
            //踢掉之前已经登录的token
            authService.checkLoginOnUser(yqmUser.getUsername(),token);
        }

        return ApiResult.ok(map).setMsg("登陆成功");
    }

    @ApiOperation("H5验证码登录授权")
    @PostMapping(value = "/login/mobile")
    public ApiResult<Map<String, Object>> loginVerify(@Validated @RequestBody LoginVerifyParam loginVerifyParam,HttpServletRequest request) {
        Object codeObj = redisUtil.get("code_" + loginVerifyParam.getAccount());
        if(codeObj == null){
            throw new YqmShopException("请先获取验证码");
        }
        String code = codeObj.toString();
        if (!StrUtil.equals(code, loginVerifyParam.getCaptcha())) {
            throw new YqmShopException("验证码错误");
        }
        YqmUser yqmUser = userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getUsername,loginVerifyParam.getAccount()));

        if(yqmUser == null) {
            throw new YqmShopException("账号不存在");
        }

        String token =  JwtToken.makeToken(yqmUser.getUid(),yqmUser.getUsername());
        String expiresTimeStr = JwtToken.getExpireTime(token);

        // 保存在线信息
        authService.save(yqmUser, token, request);
        // 返回 token
        Map<String, Object> map = new HashMap<String, Object>(2) {{
            put("token", token);
            put("expires_time", expiresTimeStr);
        }};

        userService.setSpread(loginVerifyParam.getSpread(),yqmUser.getUid());

        if(singleLogin){
            //踢掉之前已经登录的token
            authService.checkLoginOnUser(yqmUser.getUsername(),token);
        }

        return ApiResult.ok(map).setMsg("登陆成功");
    }

    @AuthCheck
    @ApiOperation("修改密码")
    @PostMapping(value = "/register/reset")
    public ApiResult<Boolean> updatePassword(@Validated @RequestBody UpdatePasswordParam updatePasswordParam,HttpServletRequest request) {
        Object codeObj = redisUtil.get("code_" + updatePasswordParam.getAccount());
        if(codeObj == null){
            throw new YqmShopException("请先获取验证码");
        }
        String code = codeObj.toString();
        if (!StrUtil.equals(code, updatePasswordParam.getCaptcha())) {
            throw new YqmShopException("验证码错误");
        }
        YqmUser yqmUser = userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getUsername,updatePasswordParam.getAccount()));

        if(yqmUser == null) {
            throw new YqmShopException("账号不存在,数据错误");
        }
        yqmUser.setPassword(SecureUtil.md5(updatePasswordParam.getPassword()));
        boolean b = userService.updateById(yqmUser);
        if (!b) {
            throw new YqmShopException("修改失败");
        }
        String bearerToken = request.getHeader("Authorization");
        String[] tokens = bearerToken.split(" ");
        String token = tokens[1];
        authService.logout(LocalUser.getUser().getUsername(), token);

        return ApiResult.ok(true).setMsg("修改成功");
    }


    @PostMapping("/register")
    @ApiOperation(value = "H5/APP注册新用户", notes = "H5/APP注册新用户")
    public ApiResult<String> register(@Validated @RequestBody RegParam param) {
        Object codeObj = redisUtil.get("code_" + param.getAccount());
        if(codeObj == null){
            return ApiResult.fail("请先获取验证码");
        }
        String code = codeObj.toString();
        if (!StrUtil.equals(code, param.getCaptcha())) {
            return ApiResult.fail("验证码错误");
        }
        YqmUser yqmUser = userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getPhone,param.getAccount()),false);
        if (ObjectUtil.isNotNull(yqmUser)) {
            return ApiResult.fail("用户已存在");
        }

        authService.register(param);
        return ApiResult.ok("","注册成功");
    }


    @PostMapping("/register/verify")
    @ApiOperation(value = "短信验证码发送", notes = "短信验证码发送")
    public ApiResult<String> verify(@Validated @RequestBody VerityParam param) {
        YqmUser yqmUser = userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                .eq(YqmUser::getPhone,param.getPhone()),false);
        if (SmsTypeEnum.REGISTER.getValue().equals(param.getType()) && ObjectUtil.isNotNull(yqmUser)) {
            return ApiResult.fail("手机号已注册");
        }
        if (SmsTypeEnum.LOGIN.getValue().equals(param.getType()) && ObjectUtil.isNull(yqmUser)) {
            return ApiResult.fail("账号不存在");
        }
        String codeKey = "code_" + param.getPhone();
        if (ObjectUtil.isNotNull(redisUtil.get(codeKey))) {
            return ApiResult.fail("10分钟内有效:" + redisUtil.get(codeKey).toString());
        }
        String code = RandomUtil.randomNumbers(ShopConstants.YQM_SHOP_SMS_SIZE);

        //redis存储
        redisUtil.set(codeKey, code, ShopConstants.YQM_SHOP_SMS_REDIS_TIME);

        String enable = redisUtil.getY("sms_enable");
        if (ShopCommonEnum.ENABLE_2.getValue().toString().equals(enable)) {
            return ApiResult.fail("测试阶段验证码:" + code);
        }

        //发送阿里云短信
        JSONObject json = new JSONObject();
        json.put("code",code);
        try {
            SmsUtils.sendSms(param.getPhone(),json.toJSONString());
        } catch (ClientException e) {
            redisUtil.del(codeKey);
            e.printStackTrace();
            return ApiResult.ok("发送失败："+e.getErrMsg());
       }
        return ApiResult.ok("发送成功，请注意查收");


    }

    @AuthCheck
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping(value = "/auth/logout")
    public ApiResult<String> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String[] tokens = bearerToken.split(" ");
        String token = tokens[1];
        authService.logout(LocalUser.getUser().getUsername(), token);
        return ApiResult.ok("退出成功");
    }

}
