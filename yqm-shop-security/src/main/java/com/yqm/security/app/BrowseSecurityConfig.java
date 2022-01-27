package com.yqm.security.app;///*
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

import com.yqm.security.core.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 *
 * security 适配器
 *
 * @Author: weiximei
 * @Date: 2021/9/12 10:34
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Configuration
public class BrowseSecurityConfig extends WebSecurityConfigurerAdapter {



    /**
     * 配置 Http请求安全策略
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests() // 对请求做一个授权，意思就是下面的请求都要一个授权
                .anyRequest() //任何请求，意思就是所有的请求
                .authenticated()  // 需要身份认证，意思就是请求都要一个身份认证
                .and()
                .csrf().disable(); // 把CSRF跨站攻击安全，关闭;

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
