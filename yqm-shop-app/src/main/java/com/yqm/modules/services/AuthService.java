/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.services;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.yqm.api.yqm-shopException;
import com.yqm.common.util.IpUtil;
import com.yqm.constant.ShopConstants;
import com.yqm.enums.AppFromEnum;
import com.yqm.modules.auth.param.LoginParam;
import com.yqm.modules.auth.param.RegParam;
import com.yqm.modules.mp.config.WxMaConfiguration;
import com.yqm.modules.mp.config.WxMpConfiguration;
import com.yqm.modules.shop.domain.YqmSystemAttachment;
import com.yqm.modules.shop.service.YqmSystemAttachmentService;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.user.service.dto.WechatUserDto;
import com.yqm.modules.user.vo.OnlineUser;
import com.yqm.utils.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @ClassName 登陆认证服务类
 * @Author weiximei <610796224@qq.com>
 * @Date 2020/6/14
 **/
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {

    private final YqmUserService userService;
    private final RedisUtils redisUtils;
    private static Integer expiredTimeIn;
    private final YqmSystemAttachmentService systemAttachmentService;

    @Value("${yqm-shop.security.token-expired-in}")
    public void setExpiredTimeIn(Integer expiredTimeIn) {
        AuthService.expiredTimeIn = expiredTimeIn;
    }


    /**
     * 小程序登陆
     *
     * @param loginParam loginParam
     * @param uid
     * @param sessionKey
     * @return long
     */
    @Transactional(rollbackFor = Exception.class)
    public YqmUser loginAuth(LoginParam loginParam,Long uid,String sessionKey) {
//        String code = loginParam.getCode();
        String encryptedData = loginParam.getEncryptedData();
        String iv = loginParam.getIv();
        String spread = loginParam.getSpread();
        //读取redis配置
        String appId = redisUtils.getY(ShopKeyUtils.getWxAppAppId());
        String secret = redisUtils.getY(ShopKeyUtils.getWxAppSecret());
        if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
            throw new yqm-shopException("请先配置小程序");
        }
        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        //WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);

        WxMaUserInfo wxMpUser = wxMaService.getUserService()
                .getUserInfo(sessionKey, encryptedData, iv);

        YqmUser yqmUser = userService.getById(uid);
        //过滤掉表情
        String ip = IpUtil.getRequestIp();

        if(!StringUtils.isNotBlank(yqmUser.getNickname())){
            yqmUser.setNickname(wxMpUser.getNickName());
            yqmUser.setAvatar(wxMpUser.getAvatarUrl());
        }
        yqmUser.setLastIp(ip);
        //构建微信用户
        WechatUserDto wechatUserDTO = yqmUser.getWxProfile();
        wechatUserDTO.setNickname(wxMpUser.getNickName());
        wechatUserDTO.setSex(Integer.valueOf(wxMpUser.getGender()));
        wechatUserDTO.setLanguage(wxMpUser.getLanguage());
        wechatUserDTO.setCity(wxMpUser.getCity());
        wechatUserDTO.setProvince(wxMpUser.getProvince());
        wechatUserDTO.setCountry(wxMpUser.getCountry());
        wechatUserDTO.setHeadimgurl(wxMpUser.getAvatarUrl());
        yqmUser.setWxProfile(wechatUserDTO);
        userService.updateById(yqmUser);
        userService.setSpread(spread, yqmUser.getUid());
        return yqmUser;
    }


    /**
     * 小程序登陆
     *
     * @param loginParam loginParam
     * @return long
     */
    @Transactional(rollbackFor = Exception.class)
    public YqmUser wxappLogin(LoginParam loginParam) {
        String code = loginParam.getCode();
        String encryptedData = loginParam.getEncryptedData();
        String iv = loginParam.getIv();
        String spread = loginParam.getSpread();
        try {
            //读取redis配置
            String appId = redisUtils.getY(ShopKeyUtils.getWxAppAppId());
            String secret = redisUtils.getY(ShopKeyUtils.getWxAppSecret());
            if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
                throw new yqm-shopException("请先配置小程序");
            }
            WxMaService wxMaService = WxMaConfiguration.getWxMaService();
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);

            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService()
                    .getPhoneNoInfo(session.getSessionKey(), encryptedData, iv);

            YqmUser yqmUser = this.userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                    .eq(YqmUser::getPhone, phoneNoInfo.getPhoneNumber()), false);

            if (ObjectUtil.isNull(yqmUser)) {

                //兼容旧系统
                yqmUser = this.userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                        .eq(YqmUser::getUsername, session.getOpenid()), false);

                if (ObjectUtil.isNull(yqmUser)) {
                    //过滤掉表情
                    String ip = IpUtil.getRequestIp();
                    yqmUser = YqmUser.builder()
                            .username(phoneNoInfo.getPhoneNumber())
                            .phone(phoneNoInfo.getPhoneNumber())
                            .addIp(ip)
                            .lastIp(ip)
                            .userType(AppFromEnum.ROUNTINE.getValue())
                            .build();

                    //构建微信用户
                    WechatUserDto wechatUserDTO = WechatUserDto.builder()
                            .routineOpenid(session.getOpenid())
                            .unionId(session.getUnionid())
                            .build();

                    yqmUser.setWxProfile(wechatUserDTO);

                    this.userService.save(yqmUser);
                }else {
                    yqmUser.setUsername(phoneNoInfo.getPhoneNumber());
                    yqmUser.setPhone(phoneNoInfo.getPhoneNumber());
                    this.userService.updateById(yqmUser);
                }

            } else {
                WechatUserDto wechatUser = yqmUser.getWxProfile();
                if (null != wechatUser &&(StrUtil.isBlank(wechatUser.getRoutineOpenid()) && StrUtil.isNotBlank(session.getOpenid()))) {
                    wechatUser.setRoutineOpenid(session.getOpenid());
                    yqmUser.setWxProfile(wechatUser);
                }
                yqmUser.setUserType(AppFromEnum.ROUNTINE.getValue());
                this.userService.updateById(yqmUser);
            }
            this.userService.setSpread(spread, yqmUser.getUid());
            redisUtils.set(ShopConstants.yqm-shop_MINI_SESSION_KET + yqmUser.getUid(), session.getSessionKey());
            return yqmUser;
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new yqm-shopException(e.toString());
        }
    }

    /**
     * 公众号登陆
     *
     * @param code   code码
     * @param spread 上级用户
     * @return uid
     */
    @Transactional(rollbackFor = Exception.class)
    public YqmUser wechatLogin(String code, String spread) {
        try {
            WxMpService wxService = WxMpConfiguration.getWxMpService();
            WxOAuth2AccessToken wxMpOAuth2AccessToken = wxService.getOAuth2Service().getAccessToken(code);
            WxOAuth2UserInfo wxMpUser = wxService.getOAuth2Service().getUserInfo(wxMpOAuth2AccessToken, null);
            String openid = wxMpUser.getOpenid();

            //如果开启了UnionId
            if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                openid = wxMpUser.getUnionId();
            }

            YqmUser yqmUser = userService.getOne(Wrappers.<YqmUser>lambdaQuery()
                    .eq(YqmUser::getUsername, openid), false);

            //long uid = 0;
            YqmUser returnUser = null;
            if (yqmUser == null) {
                //过滤掉表情
                String nickname = wxMpUser.getNickname();
                log.info("昵称：{}", nickname);
                //用户保存
                String ip = IpUtil.getRequestIp();
                YqmUser user = YqmUser.builder()
                        .username(openid)
                        .nickname(nickname)
                        .avatar(wxMpUser.getHeadImgUrl())
                        .addIp(ip)
                        .lastIp(ip)
                        .userType(AppFromEnum.WECHAT.getValue())
                        .build();

                //构建微信用户
                WechatUserDto wechatUserDTO = WechatUserDto.builder()
                        .nickname(nickname)
                        .openid(wxMpUser.getOpenid())
                        .unionId(wxMpUser.getUnionId())
                        .language("")
                        .headimgurl(wxMpUser.getHeadImgUrl())
                        .subscribe(false)
                        .subscribeTime(0L)
                        .build();

                user.setWxProfile(wechatUserDTO);
                userService.save(user);

                returnUser = user;
            } else {
                returnUser = yqmUser;
                WechatUserDto wechatUser = yqmUser.getWxProfile();
                if ((StrUtil.isBlank(wechatUser.getOpenid()) && StrUtil.isNotBlank(wxMpUser.getOpenid()))
                        || (StrUtil.isBlank(wechatUser.getUnionId()) && StrUtil.isNotBlank(wxMpUser.getUnionId()))) {
                    wechatUser.setOpenid(wxMpUser.getOpenid());
                    wechatUser.setUnionId(wxMpUser.getUnionId());

                    yqmUser.setWxProfile(wechatUser);
                }

                yqmUser.setUserType(AppFromEnum.WECHAT.getValue());
                userService.updateById(yqmUser);

            }

            userService.setSpread(spread, returnUser.getUid());

            log.error("spread:{}", spread);

            return returnUser;

        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new yqm-shopException(e.toString());
        }
    }


    /**
     * 注册
     *
     * @param param RegDTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void register(RegParam param) {

        String account = param.getAccount();
        String ip = IpUtil.getRequestIp();
        YqmUser user = YqmUser.builder()
                .username(account)
                .nickname(account)
                .password(SecureUtil.md5(param.getPassword()))
                .phone(account)
                .avatar(ShopConstants.yqm-shop_DEFAULT_AVATAR)
                .addIp(ip)
                .lastIp(ip)
                .userType(AppFromEnum.H5.getValue())
                .build();

        userService.save(user);

        //设置推广关系
        if (StrUtil.isNotBlank(param.getInviteCode())) {
            YqmSystemAttachment systemAttachment = systemAttachmentService.getByCode(param.getInviteCode());
            if (systemAttachment != null) {
                userService.setSpread(String.valueOf(systemAttachment.getUid()),
                        user.getUid());
            }
        }

    }


    /**
     * 保存在线用户信息
     *
     * @param yqmUser  /
     * @param token   /
     * @param request /
     */
    public void save(YqmUser yqmUser, String token, HttpServletRequest request) {
        String job = "yqm-shop开发工程师";
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        String address = StringUtils.getCityInfo(ip);
        OnlineUser onlineUser = null;
        try {
            onlineUser = new OnlineUser(yqmUser.getUsername(), yqmUser.getNickname(), job, browser,
                    ip, address, EncryptUtils.desEncrypt(token), new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        redisUtils.set(ShopConstants.yqm-shop_APP_LOGIN_USER +onlineUser.getUserName() + ":" + token, onlineUser, AuthService.expiredTimeIn);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName 用户名
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<OnlineUser> onlineUsers = this.getAll(userName);
        if (onlineUsers == null || onlineUsers.isEmpty()) {
            return;
        }
        for (OnlineUser onlineUser : onlineUsers) {
            try {
                String token = EncryptUtils.desDecrypt(onlineUser.getKey());
                if (StringUtils.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                    this.kickOut(userName, onlineUser.getKey());
                } else if (StringUtils.isBlank(igoreToken)) {
                    this.kickOut(userName, onlineUser.getKey());
                }
            } catch (Exception e) {
                log.error("checkUser is error", e);
            }
        }
    }

    /**
     * 踢出用户
     *
     * @param key /
     */
    public void kickOut(String userName, String key) throws Exception {
        key = ShopConstants.yqm-shop_APP_LOGIN_USER + userName + ":" + EncryptUtils.desDecrypt(key);
        redisUtils.del(key);
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String userName, String token) {
        String key = ShopConstants.yqm-shop_APP_LOGIN_USER + userName + ":" + token;
        redisUtils.del(key);
    }

    /**
     * 查询全部数据，不分页
     *
     * @param uName /
     * @return /
     */
    private List<OnlineUser> getAll(String uName) {
        List<String> keys = null;
        keys = redisUtils.scan(ShopConstants.yqm-shop_APP_LOGIN_USER + uName + ":" + "*");

        Collections.reverse(keys);
        List<OnlineUser> onlineUsers = new ArrayList<>();
        for (String key : keys) {
            OnlineUser onlineUser = (OnlineUser) redisUtils.get(key);
            onlineUsers.add(onlineUser);
        }
        onlineUsers.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUsers;
    }

    /**
     * 根据手机号查询用户注册状态
     * @param phone 手机号
     * @return /
     */
    public YqmUser authPhone(String phone) {
        return userService.getOne(Wrappers.<YqmUser>lambdaQuery().eq(YqmUser::getPhone, phone));
    }
}
