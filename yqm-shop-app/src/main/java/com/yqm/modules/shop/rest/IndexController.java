/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.rest;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import com.yqm.api.ApiResult;
import com.yqm.api.yqm-shopException;
import com.yqm.constant.ShopConstants;
import com.yqm.enums.ProductEnum;
import com.yqm.modules.activity.service.YqmStoreCombinationService;
import com.yqm.modules.activity.service.YqmStoreSeckillService;
import com.yqm.modules.canvas.domain.StoreCanvas;
import com.yqm.modules.canvas.service.StoreCanvasService;
import com.yqm.modules.mp.service.YqmWechatLiveService;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.vo.YqmSystemStoreQueryVo;
import com.yqm.modules.shop.domain.YqmAppVersion;
import com.yqm.modules.shop.param.YqmSystemStoreQueryParam;
import com.yqm.modules.shop.service.YqmAppVersionService;
import com.yqm.modules.shop.service.YqmSystemGroupDataService;
import com.yqm.modules.shop.service.YqmSystemStoreService;
import com.yqm.modules.shop.vo.AppCheckVersion;
import com.yqm.modules.shop.vo.IndexVo;
import com.yqm.modules.shop.vo.YqmAppVersionVo;
import com.yqm.utils.FileUtil;
import com.yqm.utils.RedisUtil;
import com.yqm.utils.ShopKeyUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/10/19
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "首页模块", tags = "商城:首页模块")
public class IndexController {
    private final YqmAppVersionService appVersionService;
    private final YqmSystemGroupDataService systemGroupDataService;
    private final YqmStoreProductService storeProductService;
    private final YqmSystemStoreService systemStoreService;
    private final YqmStoreCombinationService storeCombinationService;
    private final YqmStoreSeckillService storeSeckillService;
    private final YqmWechatLiveService wechatLiveService;

    private final StoreCanvasService storeCanvasService;

    @GetMapping("/getCanvas")
    @ApiOperation(value = "读取画布数据")
    public ResponseEntity<StoreCanvas> getCanvas(StoreCanvas storeCanvas){
        StoreCanvas canvas = storeCanvasService.getOne(new LambdaQueryWrapper<StoreCanvas>()
                .eq(StoreCanvas::getTerminal, storeCanvas.getTerminal())
                .orderByDesc(StoreCanvas::getCanvasId).last("limit 1"));
        return new ResponseEntity<>(canvas, HttpStatus.OK);
    }

    @Cacheable(cacheNames = ShopConstants.yqm-shop_REDIS_INDEX_KEY)
    @GetMapping("/index")
    @ApiOperation(value = "首页数据",notes = "首页数据")
    public ApiResult<IndexVo> index(){
        IndexVo indexVo = IndexVo.builder()
                .banner(systemGroupDataService.getDatas(ShopConstants.yqm-shop_HOME_BANNER))
                .bastList(storeProductService.getList(1,6, ProductEnum.TYPE_1.getValue()))
                .benefit(storeProductService.getList(1,10,ProductEnum.TYPE_4.getValue()))
                .combinationList(storeCombinationService.getList(1,8).getStoreCombinationQueryVos())
                .firstList(storeProductService.getList(1,6,ProductEnum.TYPE_3.getValue()))
                .likeInfo(storeProductService.getList(1,8,ProductEnum.TYPE_2.getValue()))
                .mapKey(RedisUtil.get(ShopKeyUtils.getTengXunMapKey()))
                .menus(systemGroupDataService.getDatas(ShopConstants.yqm-shop_HOME_MENUS))
                .roll(systemGroupDataService.getDatas(ShopConstants.yqm-shop_HOME_ROLL_NEWS))
                .seckillList(storeSeckillService.getList(1, 4))
                .liveList(wechatLiveService.getList(1,4,0))
                .build();
        return ApiResult.ok(indexVo);
    }

    @GetMapping("/search/keyword")
    @ApiOperation(value = "热门搜索关键字获取",notes = "热门搜索关键字获取")
    public ApiResult<List<String>> search(){
        List<JSONObject> list = systemGroupDataService.getDatas(ShopConstants.yqm-shop_HOT_SEARCH);
        List<String>  stringList = new ArrayList<>();
        for (JSONObject object : list) {
            stringList.add(object.getString("title"));
        }
        return ApiResult.ok(stringList);
    }


    @PostMapping("/image_base64")
    @ApiOperation(value = "获取图片base64",notes = "获取图片base64")
    @Deprecated
    public ApiResult<List<String>> imageBase64(){
        return ApiResult.ok(null);
    }


    @GetMapping("/citys")
    @ApiOperation(value = "获取城市json",notes = "获取城市json")
    public ApiResult<JSONObject> cityJson(){
        String path = "city.json";
        String name = "city.json";
        try {
            File file = FileUtil.inputStreamToFile(new ClassPathResource(path).getStream(), name);
            FileReader fileReader = new FileReader(file,"UTF-8");
            String string = fileReader.readString();
            JSONObject jsonObject = JSON.parseObject(string);
            return ApiResult.ok(jsonObject);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new yqm-shopException("无数据");
        }

    }


    @GetMapping("/store_list")
    @ApiOperation(value = "获取门店列表",notes = "获取门店列表")
    public ApiResult<Map<String,Object>> storeList( YqmSystemStoreQueryParam param){
        Map<String,Object> map = new LinkedHashMap<>();
        List<YqmSystemStoreQueryVo> lists = systemStoreService.getStoreList(
                param.getLatitude(),
                param.getLongitude(),
                param.getPage(),param.getLimit());
        map.put("list",lists);
        return ApiResult.ok(map);
    }


    @GetMapping("/version")
    @ApiOperation(value = "获取app版本信息",notes = "获取app版本信息")
    public ApiResult<YqmAppVersionVo> storeList(AppCheckVersion param){
        YqmAppVersion appVersion= appVersionService.lambdaQuery().orderByDesc(YqmAppVersion::getCreateTime).one();
        YqmAppVersionVo appVersionVo=new  YqmAppVersionVo();
        appVersionVo.setVersionCode(appVersion.getVersionCode());
        appVersionVo.setVersionInfo(appVersion.getVersionInfo());
        appVersionVo.setVersionName(appVersion.getVersionName());
        appVersionVo.setDownloadUrl("1101".equals(param.getType())?appVersion.getAndroidUrl():appVersion.getIosUrl());
        appVersionVo.setForceUpdate(appVersion.getForceUpdate()==0);
        if (!param.getVersionName().equals(appVersion.getVersionName())){
            return ApiResult.ok(appVersionVo);
        }
        return ApiResult.ok(new YqmAppVersionVo());
    }

}
