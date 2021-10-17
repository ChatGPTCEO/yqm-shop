/*
 * Copyright 2021 Wei xi mei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 *
 * limitations under the License.
 */

package com.yqm.security.browase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yqm.common.define.Http;
import com.yqm.common.response.ResponseBean;
import com.yqm.security.core.properties.LoginType;
import com.yqm.security.core.properties.SecurityProperties;
import com.yqm.security.event.LoginFailureEvent;
import com.yqm.security.event.LoginSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: weiximei
 * @Date: 2021/9/12 11:25
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Component("yqmAuthenticationFailureHandler")
public class YqmAuthenticationFailureHandler extends ExceptionMappingAuthenticationFailureHandler {

    @Autowired
    private ApplicationContext applicationContext;


    /**
     * JACKSON
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("登陆失败");
        // 发送登录的成功事件
        applicationContext.publishEvent(new LoginFailureEvent(exception));

        if(LoginType.JSON.getValue().equals(securityProperties.getBrowse().getLoginType())) {
            // 返回状态码改成 500
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(Http.ContentType.APPLICATION_JSON.getValue());
            // AuthenticationException 包含了登陆失败的所有信息
            // 登陆失败后，把失败的信息返回成JSON给前台
            log.info("错误:{}", ResponseBean.error(exception.getMessage()));
            response.getWriter().write(objectMapper.writeValueAsString(ResponseBean.error(exception.getMessage())));
        }else {

            // 使用父类的处理方式
            super.onAuthenticationFailure(request, response, exception);
        }

    }
}
