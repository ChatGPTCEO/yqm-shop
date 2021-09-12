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
import com.yqm.security.core.properties.LoginType;
import com.yqm.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
public class YqmAuthenticationFailureHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * JACKSON
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("登陆成功");

        if(LoginType.JSON.getValue().equals(securityProperties.getBrowse().getLoginType())) {

            response.setContentType(Http.ContentType.APPLICATION_JSON.getValue());

            // Authentication 接口默认把登陆成功后信息给包装起来了
            // 把 Authentication 包装好的登陆信息返回成JSON给前台
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        }else {
            // 调用父类的方法，进行页面跳转
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
