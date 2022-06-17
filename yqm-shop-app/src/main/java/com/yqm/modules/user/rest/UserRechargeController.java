/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.rest;

import cn.hutool.core.bean.BeanUtil;
import com.yqm.api.ApiResult;
import com.yqm.api.yqm-shopException;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.logging.aop.log.AppLog;
import com.yqm.common.bean.LocalUser;
import com.yqm.common.interceptor.AuthCheck;
import com.yqm.constant.ShopConstants;
import com.yqm.enums.AppFromEnum;
import com.yqm.enums.BillDetailEnum;
import com.yqm.modules.shop.domain.YqmSystemGroupData;
import com.yqm.modules.shop.service.YqmSystemGroupDataService;
import com.yqm.modules.shop.service.dto.YqmSystemGroupDataQueryCriteria;
import com.yqm.modules.shop.vo.YqmSystemGroupDataVo;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.param.RechargeParam;
import com.yqm.modules.user.service.YqmUserRechargeService;
import com.yqm.modules.mp.service.WeixinPayService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户充值 前端控制器
 * </p>
 *
 * @author weiximei
 * @since 2020-03-01
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "用户充值", tags = "用户:用户充值")
public class UserRechargeController {

    private final YqmUserRechargeService userRechargeService;
    private final WeixinPayService weixinPayService;
    private final YqmSystemGroupDataService systemGroupDataService;

    /**
     * 充值方案
     */
    @GetMapping("/recharge/index")
    @ApiOperation(value = "充值方案",notes = "充值方案",response = ApiResult.class)
    public ApiResult<Object> getWays(){
        YqmSystemGroupDataQueryCriteria queryCriteria = new YqmSystemGroupDataQueryCriteria();
        queryCriteria.setGroupName(ShopConstants.yqm-shop_RECHARGE_PRICE_WAYS);
        queryCriteria.setStatus(ShopCommonEnum.IS_STATUS_1.getValue());
        List<YqmSystemGroupData> yqmSystemGroupDataList = systemGroupDataService.queryAll(queryCriteria);

        List<YqmSystemGroupDataVo> systemGroupDataVoList = yqmSystemGroupDataList.stream().map(s->{
            YqmSystemGroupDataVo systemGroupDataVo = new YqmSystemGroupDataVo();
            BeanUtil.copyProperties(s,systemGroupDataVo,"value");
            systemGroupDataVo.setValue(JSON.parseObject(s.getValue()));
            return systemGroupDataVo;
        }).collect(Collectors.toList());

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("recharge_price_ways",systemGroupDataVoList);
        return ApiResult.ok(map);
    }

    /**
     * 公众号充值/H5充值
     */
    @AppLog(value = "公众号充值", type = 1)
    @AuthCheck
    @PostMapping("/recharge/wechat")
    @ApiOperation(value = "公众号充值/H5充值",notes = "公众号充值/H5充值",response = ApiResult.class)
    public ApiResult<Map<String,Object>> add(@Valid @RequestBody RechargeParam param){
        YqmUser user = LocalUser.getUser();

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("type",param.getFrom());
        YqmSystemGroupData systemGroupData = systemGroupDataService.getById(param.getRecharId());
        if(systemGroupData == null) {
            throw new yqm-shopException("充值方案不存在");
        }

        JSONObject jsonObject = JSON.parseObject(systemGroupData.getValue());
        String price = jsonObject.getString("price");
        String giveMoney =jsonObject.getString("give_price");
        String orderSn = userRechargeService.addRecharge(user,price,giveMoney);

        if(AppFromEnum.WEIXIN_H5.getValue().equals(param.getFrom())){
            WxPayMwebOrderResult result = (WxPayMwebOrderResult)weixinPayService
                    .unifyPay(orderSn,param.getFrom(), BillDetailEnum.TYPE_1.getValue(),"H5充值");
            map.put("data",result.getMwebUrl());
        }else if(AppFromEnum.ROUNTINE.getValue().equals(param.getFrom())){
            WxPayMpOrderResult wxPayMpOrderResult = (WxPayMpOrderResult)weixinPayService
                    .unifyPay(orderSn,param.getFrom(), BillDetailEnum.TYPE_1.getValue(),"小程序充值");
            Map<String,String> jsConfig = new HashMap<>();
            jsConfig.put("timeStamp",wxPayMpOrderResult.getTimeStamp());
            jsConfig.put("appId",wxPayMpOrderResult.getAppId());
            jsConfig.put("paySign",wxPayMpOrderResult.getPaySign());
            jsConfig.put("nonceStr",wxPayMpOrderResult.getNonceStr());
            jsConfig.put("package",wxPayMpOrderResult.getPackageValue());
            jsConfig.put("signType",wxPayMpOrderResult.getSignType());
            map.put("data",jsConfig);
        }else if(AppFromEnum.APP.getValue().equals(param.getFrom())){
            WxPayAppOrderResult wxPayAppOrderResult = (WxPayAppOrderResult)weixinPayService
                    .unifyPay(orderSn,param.getFrom(), BillDetailEnum.TYPE_1.getValue(),"app充值");
            Map<String,String> jsConfig = new HashMap<>();
            jsConfig.put("partnerid",wxPayAppOrderResult.getPartnerId());
            jsConfig.put("appid",wxPayAppOrderResult.getAppId());
            jsConfig.put("prepayid",wxPayAppOrderResult.getPrepayId());
            jsConfig.put("package",wxPayAppOrderResult.getPackageValue());
            jsConfig.put("noncestr",wxPayAppOrderResult.getNonceStr());
            jsConfig.put("timestamp",wxPayAppOrderResult.getTimeStamp());
            jsConfig.put("sign",wxPayAppOrderResult.getSign());
            map.put("data",jsConfig);
        }else{
            WxPayMpOrderResult result = (WxPayMpOrderResult)weixinPayService
                    .unifyPay(orderSn,param.getFrom(), BillDetailEnum.TYPE_1.getValue(),"公众号充值");
            Map<String,String> config = new HashMap<>();
            config.put("timestamp",result.getTimeStamp());
            config.put("appId",result.getAppId());
            config.put("nonceStr",result.getNonceStr());
            config.put("package",result.getPackageValue());
            config.put("signType",result.getSignType());
            config.put("paySign",result.getPaySign());
            map.put("data",config);
        }




        return ApiResult.ok(map);
    }




}

