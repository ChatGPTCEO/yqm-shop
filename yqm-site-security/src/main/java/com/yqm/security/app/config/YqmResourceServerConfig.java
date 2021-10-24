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

package com.yqm.security.app.config;

import com.yqm.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * OAuth2 资源服务配置
 *
 * @Author: weiximei
 * @Date: 2021/10/17 19:18
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Configuration
@EnableResourceServer // 启动资源服务配置
public class YqmResourceServerConfig  extends ResourceServerConfigurerAdapter {

    /**
     * 引入自定义的登陆成功后的处理方式
     */
    @Autowired
    @Qualifier("yqmAppAuthenticationSuccessHandler")
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 引入自定义的登陆失败的处理方式
     */
    @Autowired
    @Qualifier("yqmAppAuthenticationFailureHandler")
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                //.apply(mySocicalSecurityConfig) // 添加 Configurer 的配置
                //.and()
                //.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) // 把自定义的拦截器放在 UsernamePasswordAuthenticationFilter 的前面执行
                .cors()
                .and()
                .formLogin() //表单登陆
                .loginPage(securityProperties.getBrowse().getLoginPage()) // 指定用户登陆的页面
                .loginProcessingUrl(securityProperties.getBrowse().getLoginUrl()) // 表示表单提交登陆的地址
                .successHandler(authenticationSuccessHandler) // 使用我们自定义的登陆成功后的处理方式
                .failureHandler(authenticationFailureHandler) // 使用我们自定义的登陆失败后的处理方式
                .and()
                .authorizeRequests() // 对请求做一个授权，意思就是下面的请求都要一个授权
                .antMatchers(
                        securityProperties.getBrowse().getLoginPage()
                        ,"/oauth/token"
                        ,"/favicon.ico"
                        ,"/code/image","/session/invalid"
                ).permitAll() // 表示对请求，不做认证拦截
                .anyRequest() //任何请求，意思就是所有的请求
                .authenticated()  // 需要身份认证，意思就是请求都要一个身份认证
                .and()
                .csrf().disable(); // 把CSRF跨站攻击安全，关闭

    }


}
