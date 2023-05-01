/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.wechat.rest;


import com.yqm.constant.ShopConstants;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.mp.domain.YqmWechatMenu;
import com.yqm.modules.mp.service.YqmWechatMenuService;
import com.yqm.modules.mp.config.WxMpConfiguration;
import com.yqm.utils.OrderUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
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
* @date 2019-10-06
*/
@Api(tags = "商城:微信菜單")
@RestController
@RequestMapping("api")
@SuppressWarnings("unchecked")
public class WechatMenuController {

    private final YqmWechatMenuService YqmWechatMenuService;

    public WechatMenuController(YqmWechatMenuService YqmWechatMenuService) {
        this.YqmWechatMenuService = YqmWechatMenuService;
    }

    @ApiOperation(value = "查询菜单")
    @GetMapping(value = "/yqmWechatMenu")
    @PreAuthorize("hasAnyRole('admin','YqmWechatMenu_ALL','YqmWechatMenu_SELECT')")
    public ResponseEntity getYqmWechatMenus(){
        return new ResponseEntity(YqmWechatMenuService.getOne(new LambdaQueryWrapper<YqmWechatMenu>()
                .eq(YqmWechatMenu::getKey,ShopConstants.WECHAT_MENUS)),HttpStatus.OK);
    }

    @ForbidSubmit
    @ApiOperation(value = "创建菜单")
    @PostMapping(value = "/yqmWechatMenu")
    @PreAuthorize("hasAnyRole('admin','YqmWechatMenu_ALL','YqmWechatMenu_CREATE')")
    public ResponseEntity create( @RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String jsonButton = jsonObject.get("buttons").toString();
        YqmWechatMenu YqmWechatMenu = new YqmWechatMenu();
        Boolean isExist = YqmWechatMenuService.isExist(ShopConstants.WECHAT_MENUS);
        WxMenu menu = JSONObject.parseObject(jsonStr,WxMenu.class);

        WxMpService wxService = WxMpConfiguration.getWxMpService();
        if(isExist){
            YqmWechatMenu.setKey(ShopConstants.WECHAT_MENUS);
            YqmWechatMenu.setResult(jsonButton);
            YqmWechatMenuService.saveOrUpdate(YqmWechatMenu);
        }else {
            YqmWechatMenu.setKey(ShopConstants.WECHAT_MENUS);
            YqmWechatMenu.setResult(jsonButton);
            YqmWechatMenu.setAddTime(OrderUtil.getSecondTimestampTwo());
            YqmWechatMenuService.save(YqmWechatMenu);
        }


        //创建菜单
        try {
            wxService.getMenuService().menuDelete();
            wxService.getMenuService().menuCreate(menu);
        } catch (WxErrorException e) {
            throw new BadRequestException(e.getMessage());
           // e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.OK);
    }




}
