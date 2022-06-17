/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.constant.ShopConstants;
import com.yqm.constant.SystemConfigConstants;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.shop.domain.YqmSystemConfig;
import com.yqm.modules.shop.service.YqmSystemConfigService;
import com.yqm.modules.shop.service.dto.YqmSystemConfigQueryCriteria;
import com.yqm.modules.mp.config.WxMpConfiguration;
import com.yqm.modules.mp.config.WxPayConfiguration;
import com.yqm.modules.mp.config.WxMaConfiguration;
import com.yqm.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
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
@Api(tags = "商城:配置管理")
@RestController
@RequestMapping("api")
public class SystemConfigController {

    private final YqmSystemConfigService yqmSystemConfigService;

    public SystemConfigController(YqmSystemConfigService yqmSystemConfigService) {
        this.yqmSystemConfigService = yqmSystemConfigService;
    }

    @Log("查询")
    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmSystemConfig")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMCONFIG_ALL','YQMSYSTEMCONFIG_SELECT')")
    public ResponseEntity getYqmSystemConfigs(YqmSystemConfigQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmSystemConfigService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增或修改")
    @ApiOperation(value = "新增或修改")
    @PostMapping(value = "/yqmSystemConfig")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMCONFIG_ALL','YQMSYSTEMCONFIG_CREATE')")
    public ResponseEntity create(@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        jsonObject.forEach(
                (key,value)->{
                    YqmSystemConfig yqmSystemConfig = yqmSystemConfigService.getOne(new LambdaQueryWrapper<YqmSystemConfig>()
                            .eq(YqmSystemConfig::getMenuName,key));
                    YqmSystemConfig yqmSystemConfigModel = new YqmSystemConfig();
                    yqmSystemConfigModel.setMenuName(key);
                    yqmSystemConfigModel.setValue(value.toString());
                    //重新配置微信相关
                    if(SystemConfigConstants.WECHAT_APPID.equals(key)){
                        WxMpConfiguration.removeWxMpService();
                        WxPayConfiguration.removeWxPayService();
                        WxMaConfiguration.removeWxMaService();
                    }
                    if(SystemConfigConstants.WXPAY_MCHID.equals(key) || SystemConfigConstants.WXAPP_APPID.equals(key)){
                        WxPayConfiguration.removeWxPayService();
                    }
                    if(SystemConfigConstants.EXP_APPID.equals(key)){
                        RedisUtil.del(ShopConstants.YQM_SHOP_EXPRESS_SERVICE);
                    }
                    RedisUtil.set(key,value.toString(),0);
                    if(ObjectUtil.isNull(yqmSystemConfig)){
                        yqmSystemConfigService.save(yqmSystemConfigModel);
                    }else{
                        yqmSystemConfigModel.setId(yqmSystemConfig.getId());
                        yqmSystemConfigService.saveOrUpdate(yqmSystemConfigModel);
                    }
                }
        );

        return new ResponseEntity(HttpStatus.CREATED);
    }



}
