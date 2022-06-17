/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.common.bean;


import com.yqm.api.ApiCode;
import com.yqm.api.UnAuthenticatedException;
import com.yqm.common.util.JwtToken;
import com.yqm.common.util.RequestUtils;
import com.yqm.modules.user.domain.YqmUser;
import com.auth0.jwt.interfaces.Claim;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 全局user
 * @author weiximei
 * @date 2020-04-30
 */
public class LocalUser {
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    public static void set(YqmUser user, Integer scope) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("scope", scope);
        LocalUser.threadLocal.set(map);
    }

    public static void clear() {
        LocalUser.threadLocal.remove();
    }

    public static YqmUser getUser() {
        Map<String, Object> map = LocalUser.threadLocal.get();
        YqmUser user = (YqmUser)map.get("user");
        return user;
    }

    public static Integer getScope() {
        Map<String, Object> map = LocalUser.threadLocal.get();
        Integer scope = (Integer)map.get("scope");
        return scope;
    }

    public static Long getUidByToken(){
        String bearerToken =  RequestUtils.getRequest().getHeader("Authorization");
        if (StringUtils.isEmpty(bearerToken)) {
            return 0L;
        }

        if (!bearerToken.startsWith("Bearer")) {
            return 0L;
        }
        String[] tokens = bearerToken.split(" ");
        if (!(tokens.length == 2)) {
            return 0L;
        }
        String token = tokens[1];

        Optional<Map<String, Claim>> optionalMap = JwtToken.getClaims(token);
        Map<String, Claim> map = optionalMap
                .orElseThrow(() -> new UnAuthenticatedException(ApiCode.UNAUTHORIZED));

        return  map.get("uid").asLong();
    }
}
