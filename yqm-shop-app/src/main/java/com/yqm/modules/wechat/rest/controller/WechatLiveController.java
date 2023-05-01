/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.wechat.rest.controller;

import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import com.yqm.api.ApiResult;
import com.yqm.modules.mp.service.YqmWechatLiveService;
import com.yqm.modules.mp.service.dto.YqmWechatLiveQueryCriteria;
import com.yqm.modules.mp.vo.WechatLiveVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @author weiximei
* @date 2020-08-10
*/
@AllArgsConstructor
@Api(tags = "wxlive管理")
@RestController
@RequestMapping
public class WechatLiveController {

    private final YqmWechatLiveService yqmWechatLiveService;


    @GetMapping("yqmWechatLive")
    @ApiOperation("查询所有直播间")
    public ApiResult<WechatLiveVo> getYqmWechatLives(YqmWechatLiveQueryCriteria criteria, Pageable pageable){
        return ApiResult.ok(yqmWechatLiveService.queryAll(criteria,pageable));
    }
    @GetMapping("yqmWechatLive/getLiveReplay/{id}")
    @ApiOperation("获取直播回放")
    public ApiResult<List<WxMaLiveResult.LiveReplay>>  getLiveReplay(@PathVariable Integer id){
        return ApiResult.ok(yqmWechatLiveService.getLiveReplay(id));
    }
}
