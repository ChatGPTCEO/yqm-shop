/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.wechat.rest;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.mp.domain.YqmWechatReply;
import com.yqm.modules.mp.service.YqmWechatReplyService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @author weiximei
* @date 2019-10-10
*/
@Api(tags = "商城:微信回復管理")
@RestController
@RequestMapping("api")
public class WechatReplyController {

    private final YqmWechatReplyService yqmWechatReplyService;

    public WechatReplyController(YqmWechatReplyService yqmWechatReplyService) {
        this.yqmWechatReplyService = yqmWechatReplyService;
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmWechatReply")
    @PreAuthorize("@el.check('admin','YQMWECHATREPLY_ALL','YQMWECHATREPLY_SELECT')")
    public ResponseEntity getYqmWechatReplys(){
        return new ResponseEntity<>(yqmWechatReplyService.isExist("subscribe"),HttpStatus.OK);
    }


    @ForbidSubmit
    @ApiOperation(value = "新增自动回复")
    @PostMapping(value = "/yqmWechatReply")
    @PreAuthorize("@el.check('admin','YQMWECHATREPLY_ALL','YQMWECHATREPLY_CREATE')")
    public ResponseEntity create(@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        YqmWechatReply yqmWechatReply = new YqmWechatReply();
        YqmWechatReply isExist = yqmWechatReplyService.isExist(jsonObject.get("key").toString());
        yqmWechatReply.setKey(jsonObject.get("key").toString());
        yqmWechatReply.setStatus(Integer.valueOf(jsonObject.get("status").toString()));
        yqmWechatReply.setData(jsonObject.get("data").toString());
        yqmWechatReply.setType(jsonObject.get("type").toString());
        if(ObjectUtil.isNull(isExist)){
            yqmWechatReplyService.create(yqmWechatReply);
        }else{
            yqmWechatReply.setId(isExist.getId());
            yqmWechatReplyService.upDate(yqmWechatReply);
        }

        return new ResponseEntity(HttpStatus.CREATED);
    }





}
