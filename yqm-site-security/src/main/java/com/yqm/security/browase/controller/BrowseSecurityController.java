///*
// * Copyright 2021 Wei xi mei
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// *
// * you may not use this file except in compliance with the License.
// *
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// *
// * distributed under the License is distributed on an "AS IS" BASIS,
// *
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *
// * See the License for the specific language governing permissions and
// *
// * limitations under the License.
// */
//
//package com.yqm.security.browase.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yqm.common.define.Http;
//import com.yqm.security.UserInfoService;
//import com.yqm.security.core.properties.LoginType;
//import com.yqm.security.core.properties.SecurityProperties;
//import com.yqm.security.event.LoginSuccessEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @Author: weiximei
// * @Date: 2021/9/12 12:20
// * @微信: wxm907147608
// * @QQ: 907147608
// * @Email: 907147608@qq.com
// */
//@Slf4j
////@RestController
//public class BrowseSecurityController {
//
//    //@Autowired
//    private ApplicationContext applicationContext;
//
//    /**
//     * JACKSON
//     */
//    //@Autowired
//    private ObjectMapper objectMapper;
//
//    //@Autowired
//    private SecurityProperties securityProperties;
//
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
//
//    /**
//     * 当需要身份认证的时候，跳转到这里
//     * 		返回 401 状态码和错误信息。
//     * @param request
//     * @param response
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping("/authentication/require")
//    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
//    public String requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        return "访问的服务器需要身份认证，请引导用户进入登陆页";
//    }
//
//    /**
//     * 登录成功
//     *
//     * @param request
//     * @param response
//     * @return
//     * @throws IOException
//     */
//    @RequestMapping("/login/success")
//    @ResponseStatus(code = HttpStatus.OK)
//    public void loginSuccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        log.info("登陆成功");
//
//        if(LoginType.JSON.getValue().equals(securityProperties.getBrowse().getLoginType())) {
//
//            response.setContentType(Http.ContentType.APPLICATION_JSON.getValue());
//
//            // Authentication 接口默认把登陆成功后信息给包装起来了
//            // 把 Authentication 包装好的登陆信息返回成JSON给前台
//            response.getWriter().write(objectMapper.writeValueAsString(UserInfoService.getUser()));
//        }else {
//            // 调用父类的方法，进行页面跳转
//            redirectStrategy.sendRedirect(request, response, "/");
//        }
//
//        // 发送登录的成功事件
//        applicationContext.publishEvent(new LoginSuccessEvent(UserInfoService.getUser()));
//    }
//
//}
