/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.mp.handler;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.modules.mp.domain.YqmWechatReply;
import com.yqm.modules.mp.service.YqmWechatReplyService;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class SubscribeHandler extends AbstractHandler {

    @Autowired
    private YqmWechatReplyService yqmWechatReplyService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {


        String str = "你好，欢迎关注yqm-shop!";
        YqmWechatReply wechatReply = yqmWechatReplyService.isExist("subscribe");
        if(!ObjectUtil.isNull(wechatReply)){
            str = JSONObject.parseObject(wechatReply.getData()).getString("content");
        }

        try {
            WxMpXmlOutMessage msg= WxMpXmlOutMessage.TEXT()
                    .content(str)
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
            return msg;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }



        return null;
    }



}
