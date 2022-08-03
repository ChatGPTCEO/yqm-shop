/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.rest;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.ApiResult;
import com.yqm.api.YqmShopException;
import com.yqm.logging.aop.log.AppLog;
import com.yqm.common.aop.NoRepeatSubmit;
import com.yqm.common.bean.LocalUser;
import com.yqm.common.interceptor.AuthCheck;
import com.yqm.constant.SystemConfigConstants;
import com.yqm.modules.activity.domain.YqmStoreBargainUser;
import com.yqm.modules.activity.domain.YqmStoreBargainUserHelp;
import com.yqm.modules.activity.param.BargainShareParam;
import com.yqm.modules.activity.param.YqmStoreBargainUserHelpQueryParam;
import com.yqm.modules.activity.param.YqmStoreBargainUserQueryParam;
import com.yqm.modules.activity.service.YqmStoreBargainService;
import com.yqm.modules.activity.service.YqmStoreBargainUserHelpService;
import com.yqm.modules.activity.service.YqmStoreBargainUserService;
import com.yqm.modules.activity.vo.BargainCountVo;
import com.yqm.modules.activity.vo.BargainVo;
import com.yqm.modules.activity.vo.TopCountVo;
import com.yqm.modules.activity.vo.YqmStoreBargainQueryVo;
import com.yqm.modules.activity.vo.YqmStoreBargainUserHelpQueryVo;
import com.yqm.modules.activity.vo.YqmStoreBargainUserQueryVo;
import com.yqm.modules.services.CreatShareProductService;
import com.yqm.modules.shop.service.YqmSystemConfigService;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 砍价 前端控制器
 * </p>
 *
 * @author weiximei
 * @since 2019-12-21
 */

@RestController
@RequestMapping
@Api(value = "砍价商品", tags = "营销:砍价商品")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("unchecked")
public class StoreBargainController {

    private final YqmStoreBargainService storeBargainService;
    private final YqmStoreBargainUserService storeBargainUserService;
    private final YqmStoreBargainUserHelpService storeBargainUserHelpService;
    private final YqmUserService userService;
    private final YqmSystemConfigService systemConfigService;
    private final CreatShareProductService creatShareProductService;

    @Value("${file.path}")
    private String path;




    /**
     * 砍价产品列表
     */
    @GetMapping("/bargain/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码,默认为1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "页大小,默认为10", paramType = "query", dataType = "int")
    })
    @ApiOperation(value = "砍价产品列表",notes = "砍价产品列表")
    public ApiResult<Object> getYqmStoreBargainPageList(@RequestParam(value = "page",defaultValue = "1") int page,
                                                       @RequestParam(value = "limit",defaultValue = "10") int limit){

        return ApiResult.resultPage(storeBargainService.getList(page, limit),limit);
    }

    /**
    * 砍价详情
    */
    @AppLog(value = "查看砍价产品", type = 1)
    @AuthCheck
    @GetMapping("/bargain/detail/{id}")
    @ApiOperation(value = "砍价详情",notes = "砍价详情",response = YqmStoreBargainQueryVo.class)
    public ApiResult<BargainVo> getYqmStoreBargain(@PathVariable Long id){
        if(ObjectUtil.isNull(id)) {
            throw new YqmShopException("参数错误");
        }
        YqmUser yqmUser = LocalUser.getUser();
        return ApiResult.ok(storeBargainService.getDetail(id,yqmUser));
    }

    /**
     * 砍价详情统计
     */
    @AppLog(value = "砍价详情统计", type = 1)
    @AuthCheck
    @PostMapping("/bargain/help/count")
    @ApiOperation(value = "砍价详情统计",notes = "砍价详情统计")
    public ApiResult<BargainCountVo> helpCount(@Validated @RequestBody YqmStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        Long uid = LocalUser.getUser().getUid();
        return ApiResult.ok(storeBargainService.helpCount(bargainId,bargainUserUid,uid));
    }

    /**
     * 砍价顶部统计
     */
    @PostMapping("/bargain/share")
    @ApiOperation(value = "砍价顶部统计",notes = "砍价顶部统计")
    public ApiResult<TopCountVo> topCount(@Validated @RequestBody BargainShareParam param){
        Long bargainId = null;
        if(NumberUtil.isNumber(param.getBargainId())) {
            bargainId = Long.valueOf(param.getBargainId());
        }
        return ApiResult.ok(storeBargainService.topCount(bargainId));
    }

    /**
     * 参与砍价
     */
    @AppLog(value = "参与砍价", type = 1)
    @NoRepeatSubmit
    @AuthCheck
    @PostMapping("/bargain/start")
    @ApiOperation(value = "参与砍价",notes = "参与砍价")
    public ApiResult<Boolean> start(@Validated @RequestBody BargainShareParam param){
        Long bargainId = null;
        if(NumberUtil.isNumber(param.getBargainId())) {
            bargainId = Long.valueOf(param.getBargainId());
        }
        Long uid = LocalUser.getUser().getUid();
        storeBargainUserService.setBargain(bargainId,uid);
        return ApiResult.ok();
    }

    /**
     * 帮助好友砍价
     */
    @AppLog(value = "帮助好友砍价", type = 1)
    @NoRepeatSubmit
    @AuthCheck
    @PostMapping("/bargain/help")
    @ApiOperation(value = "帮助好友砍价",notes = "帮助好友砍价")
    public ApiResult<Map<String,Object>> help(@Validated @RequestBody YqmStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        Long uid = LocalUser.getUser().getUid();
        Map<String,Object> map = Maps.newHashMap();
        boolean isBargainUserHelp = storeBargainUserService
                .isBargainUserHelp(bargainId,bargainUserUid,uid);
        if(!isBargainUserHelp){
            map.put("status","SUCCESSFUL");
            return ApiResult.ok(map);
        }

        storeBargainService.doHelp(bargainId,bargainUserUid,uid);
        map.put("status","SUCCESS");
        return ApiResult.ok(map);
    }

    /**
     *  获取砍掉金额
     */
    @AuthCheck
    @PostMapping("/bargain/help/price")
    @ApiOperation(value = "获取砍掉金额",notes = "获取砍掉金额")
    public ApiResult<Map<String,Object>> price(@Validated @RequestBody YqmStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        Long uid = LocalUser.getUser().getUid();
        Map<String,Object> map = Maps.newHashMap();
        YqmStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,bargainUserUid);
        if(ObjectUtil.isNull(storeBargainUser)){
            map.put("price",0);
            return ApiResult.ok(map);
        }
        YqmStoreBargainUserHelp storeBargainUserHelp = storeBargainUserHelpService
                .getOne(new LambdaQueryWrapper<YqmStoreBargainUserHelp>()
                .eq(YqmStoreBargainUserHelp::getBargainId,bargainId)
                .eq(YqmStoreBargainUserHelp::getBargainUserId,storeBargainUser.getId())
                .eq(YqmStoreBargainUserHelp::getUid,uid).last("limit 1"));
        if(ObjectUtil.isNull(storeBargainUserHelp)){
            map.put("price",0);
        }else{
            map.put("price",storeBargainUserHelp.getPrice());
        }
        return ApiResult.ok(map);
    }

    /**
     * 好友帮
     */
    @PostMapping("/bargain/help/list")
    @ApiOperation(value = "好友帮",notes = "好友帮")
    public ApiResult<List<YqmStoreBargainUserHelpQueryVo>> helpList(@Validated @RequestBody YqmStoreBargainUserHelpQueryParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        return ApiResult.ok(storeBargainUserHelpService.getList(bargainId,bargainUserUid
                ,param.getPage(),param.getLimit()));
    }

    /**
     * 获取开启砍价用户信息
     */
    @PostMapping("/bargain/start/user")
    @ApiOperation(value = "获取开启砍价用户信息",notes = "获取开启砍价用户信息")
    public ApiResult<Object> startUser(@Validated @RequestBody YqmStoreBargainUserQueryParam param){
        Long bargainUserUid = Long.valueOf(param.getBargainUserUid());
        return ApiResult.ok(userService.getYqmUserById(bargainUserUid));
    }

    /**
     * 砍价海报
     */
    @AuthCheck
    @PostMapping("/bargain/poster")
    @ApiOperation(value = "砍价海报",notes = "砍价海报")
    public ApiResult<Map<String,Object>> poster(@Validated @RequestBody BargainShareParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if(StrUtil.isBlank(siteUrl)){
            throw new YqmShopException("未配置h5地址");
        }
        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if(StrUtil.isBlank(apiUrl)){
            throw new YqmShopException("未配置api地址");
        }
        YqmUser userInfo = LocalUser.getUser();
        Map<String,Object> map = Maps.newHashMap();
        String url = creatShareProductService.getBargainPosterUrl(bargainId,userInfo,siteUrl,apiUrl,path,param.getFrom());
        map.put("url",url);
        return ApiResult.ok(map);
    }


    /**
     * 砍价列表(已参与)
     */
    @AuthCheck
    @GetMapping("/bargain/user/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码,默认为1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "页大小,默认为10", paramType = "query", dataType = "int")
    })
    @ApiOperation(value = "砍价列表(已参与)",notes = "砍价列表(已参与)")
    public ApiResult<List<YqmStoreBargainUserQueryVo>> bargainUserList(
                                             @RequestParam(value = "page",defaultValue = "1") int page,
                                             @RequestParam(value = "limit",defaultValue = "10") int limit){
        Long uid = LocalUser.getUser().getUid();
        List<YqmStoreBargainUserQueryVo> yqmStoreBargainUserQueryVos = storeBargainUserService
                .bargainUserList(uid,page,limit);
        if(yqmStoreBargainUserQueryVos.isEmpty()) {
            throw new YqmShopException("暂无参与砍价");
        }
        return ApiResult.ok(yqmStoreBargainUserQueryVos);
    }

    /**
     * 砍价取消
     */
    @AppLog(value = "取消砍价", type = 1)
    @AuthCheck
    @PostMapping("/bargain/user/cancel")
    @ApiOperation(value = "砍价取消",notes = "砍价取消")
    public ApiResult<Boolean> bargainCancel(@Validated @RequestBody BargainShareParam param){
        Long bargainId = Long.valueOf(param.getBargainId());
        Long uid = LocalUser.getUser().getUid();
        storeBargainUserService.bargainCancel(bargainId,uid);
        return ApiResult.ok();
    }











}

