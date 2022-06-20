/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.rest;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.ApiResult;
import com.yqm.api.YqmShopException;
import com.yqm.logging.aop.log.AppLog;
import com.yqm.common.bean.LocalUser;
import com.yqm.common.interceptor.AuthCheck;
import com.yqm.constant.SystemConfigConstants;
import com.yqm.enums.BillDetailEnum;
import com.yqm.modules.activity.service.YqmUserExtractService;
import com.yqm.modules.services.CreatShareProductService;
import com.yqm.modules.shop.service.YqmSystemConfigService;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.param.PromParam;
import com.yqm.modules.user.param.YqmUserBillQueryParam;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.modules.user.vo.YqmUserBillQueryVo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * @ClassName UserBillController
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/11/10
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户分销", tags = "用户:用户分销")
public class UserBillController {

    private final YqmUserBillService userBillService;
    private final YqmUserExtractService extractService;
    private final YqmSystemConfigService systemConfigService;
    private final YqmUserService yqmUserService;
    private final CreatShareProductService creatShareProductService;

    @Value("${file.path}")
    private String path;

    /**
     * 推广数据    昨天的佣金   累计提现金额  当前佣金
     */
    @AppLog(value = "查看推广数据", type = 1)
    @AuthCheck
    @GetMapping("/commission")
    @ApiOperation(value = "推广数据",notes = "推广数据")
    public ApiResult<Map<String,Object>> commission(){
        YqmUser yqmUser = LocalUser.getUser();

        //昨天的佣金
        double lastDayCount = userBillService.yesterdayCommissionSum(yqmUser.getUid());
        //累计提现金额
        double extractCount = extractService.extractSum(yqmUser.getUid());

        Map<String,Object> map = Maps.newHashMap();
        map.put("lastDayCount",lastDayCount);
        map.put("extractCount",extractCount);
        map.put("commissionCount",yqmUser.getBrokeragePrice());

        return ApiResult.ok(map);
    }

    /**
     * 积分记录
     */
    @AppLog(value = "查看积分记录", type = 1)
    @AuthCheck
    @GetMapping("/integral/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码,默认为1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "页大小,默认为10", paramType = "query", dataType = "int")
    })
    @ApiOperation(value = "积分记录",notes = "积分记录")
    public ApiResult<List<YqmUserBillQueryVo>> userInfo(@RequestParam(value = "page",defaultValue = "1") int page,
                                                       @RequestParam(value = "limit",defaultValue = "10") int limit){
        Long uid = LocalUser.getUser().getUid();
        return ApiResult.ok(userBillService.userBillList(uid, BillDetailEnum.CATEGORY_2.getValue()
                ,page, limit));
    }


    /**
     * 分销二维码海报生成
     */
    @AppLog(value = "分销二维码海报生成", type = 1)
    @AuthCheck
    @GetMapping("/spread/banner")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "form", value = "来源", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "分销二维码海报生成",notes = "分销二维码海报生成")
    public ApiResult<List<Map<String,Object>>> spreadBanner(@RequestParam(value = "",required=false) String from){
        YqmUser yqmUser = LocalUser.getUser();
        String siteUrl = systemConfigService.getData(SystemConfigConstants.SITE_URL);
        if(StrUtil.isEmpty(siteUrl)){
            throw new YqmShopException("未配置h5地址!");
        }
        String apiUrl = systemConfigService.getData(SystemConfigConstants.API_URL);
        if(StrUtil.isEmpty(apiUrl)){
            throw new YqmShopException("未配置api地址!");
        }

        String spreadUrl = creatShareProductService.getSpreadUrl(from,yqmUser,siteUrl,apiUrl,path);

        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> map = Maps.newHashMap();
        map.put("id",1);
        map.put("pic","");
        map.put("title","分享海报");
        map.put("wap_poster",spreadUrl);
        list.add(map);
        return ApiResult.ok(list);
    }


    /**
     *  推广人统计
     */
    @AppLog(value = "查看推广人统计", type = 1)
    @AuthCheck
    @PostMapping("/spread/people")
    @ApiOperation(value = "推广人统计",notes = "推广人统计")
    public ApiResult<Map<String,Object>> spreadPeople(@Valid @RequestBody PromParam param){
        Long uid = LocalUser.getUser().getUid();
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("list",yqmUserService.getUserSpreadGrade(uid,param.getPage(),param.getLimit()
                ,param.getGrade(),param.getKeyword(),param.getSort()));

        Map<String,Integer> countMap = yqmUserService.getSpreadCount(uid);
        map.put("total",countMap.get("first"));
        map.put("totalLevel",countMap.get("second"));
        return ApiResult.ok(map);
    }

    /**
     * 推广佣金明细
     * type  0 全部  1 消费  2 充值  3 返佣  4 提现
     * @return mixed
     */
    @AppLog(value = "查看推广佣金明细", type = 1)
    @AuthCheck
    @GetMapping("/spread/commission/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码,默认为1", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "页大小,默认为10", paramType = "query", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型 0所有 1消费 2充值 3返佣 4提现 5签到积分 6退款 7系统增加 8系统减少", paramType = "query", dataType = "int")
    })
    @ApiOperation(value = "推广佣金明细",notes = "推广佣金明细")
    public ApiResult<Object> spreadCommission(@RequestParam(value = "page",defaultValue = "1") int page,
                                              @RequestParam(value = "limit",defaultValue = "10") int limit,
                                              @PathVariable String type){
        int newType = 0;
        if(NumberUtil.isNumber(type)) {
            newType = Integer.valueOf(type);
        }
        Long uid = LocalUser.getUser().getUid();
        Map<String, Object> map = userBillService.getUserBillList(page,limit,uid,newType);
        Long total = (Long)map.get("total");
        Long totalPage = (Long)map.get("totalPage");
        return ApiResult.resultPage(total.intValue(),totalPage.intValue(),map.get("list"));
      //  return ApiResult.resultPage(Collections.singletonList(userBillService.getUserBillList(page,limit,uid,newType)),limit);
    }


    /**
     * 推广订单
     */
    @AppLog(value = "查看推广订单", type = 1)
    @AuthCheck
    @PostMapping("/spread/order")
    @ApiOperation(value = "推广订单",notes = "推广订单")
    public ApiResult<Object> spreadOrder(@RequestBody YqmUserBillQueryParam param){
        Long uid = LocalUser.getUser().getUid();
        Map<String, Object> map = userBillService.spreadOrder(uid,param.getPage(),param.getLimit());
        return ApiResult.ok(map);
    }



}
