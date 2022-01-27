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

package com.yqm.security.app;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yqm.common.response.ResponseBean;
import com.yqm.security.core.properties.SecurityProperties;
import com.yqm.security.event.LoginFailureEvent;
import com.yqm.security.event.LoginSuccessEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登陆成功之后的处理
 * 		App登陆，需要在这个里面，处理生成Token逻辑
 *
 * @Author: weiximei
 * @Date: 2021/10/17 19:32
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Component("yqmAppAuthenticationSuccessHandler")
public class YqmAppAuthenticationSuccessHandler  extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * spring boot 默认使用的 JACKJSON
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 注入spring security 提供的 ClientDetailsService 的实现
     * 用来读取 clientId的值
     */
    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 构建 Token 值
     */
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info("登陆成功");


        // 获取头部 Authorization
        String header = request.getHeader("Authorization");

        // 检查是否存在 Basic 字样
        if (header == null || !header.startsWith("Basic ")) {
            throw new UnapprovedClientAuthenticationException("请求头中无clientId信息");
        }

        String[] tokens = extractAndDecodeHeader(header, request);
        assert tokens.length == 2;

        // 可以当做是 账号和密码。因为 Basic 模式本身就是账号密码
        // String username = tokens[0];
        // String password = tokens[1];

        // 这里是请求头中的clientId
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        // 根据 clientId 读取 本地库里面的用户信息
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        String encodePass = passwordEncoder.encode(clientSecret);

        // 校验 clientDetails
        if(null == clientDetails) {
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在："+clientId);
        }else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配, clientId值为："+clientId);
        }

        // 构建 TokenRequest
        // 第一个参数：是请求携带的参数，在这里我们自定义的，没有参数！
        // 第二个参数：clientId
        // 第三个参数：scope
        // 第四个参数：grantType，它的值是四种授权模式中的一种，但是在这里我们自定义的，可以给个“custom”表示自定义的（随便取）
        TokenRequest tokenRequest = new TokenRequest(MapUtil.empty(), clientId, clientDetails.getScope(), "custom");

        // 构建 OAuth2Request
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        // 构建 OAuth2Authentication
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        // 创建 Token值
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        response.setContentType("application/json;charset=UTF-8");

        // Authentication 接口默认把登陆成功后信息给包装起来了
        // 把 Authentication 包装好的登陆信息返回成JSON给前台
        response.getWriter().write(objectMapper.writeValueAsString(ResponseBean.success(token)));

        // 发送登录的成功事件
        applicationContext.publishEvent(new LoginSuccessEvent(token));
    }

    /**
     * 获取请求头中的账号密码
     * @param header
     * @param request
     * @return
     * @throws IOException
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
            throws IOException {

        // 开始截取，把 ‘Basic ’ 这部分去掉，留下剩下的一串 Token值，这个值是 client+secret 进行Base64编码之后的值
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            // 进行Base64解码
            decoded = Base64.decode(base64Token);
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        // 把解码之后的值，转换成 String
        // 账户名:密码
        String token = new String(decoded, "UTF-8");

        // 寻找冒号的位置
        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        // 开始截取
        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }
}
