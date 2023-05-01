/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 */
package com.yqm.modules.activity.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.SpecTypeEnum;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.domain.YqmStoreSeckill;
import com.yqm.modules.activity.service.YqmStoreSeckillService;
import com.yqm.modules.activity.service.dto.YqmStoreSeckillDto;
import com.yqm.modules.activity.service.dto.YqmStoreSeckillQueryCriteria;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.product.domain.YqmStoreProductAttrResult;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.service.YqmStoreProductAttrResultService;
import com.yqm.modules.product.service.YqmStoreProductAttrValueService;
import com.yqm.modules.product.service.YqmStoreProductRuleService;
import com.yqm.modules.product.service.dto.ProductFormatDto;
import com.yqm.modules.template.domain.YqmShippingTemplates;
import com.yqm.modules.template.service.YqmShippingTemplatesService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiximei
 * @date 2019-12-14
 */
@Api(tags = "商城:秒杀管理")
@RestController
@RequestMapping("api")
public class StoreSeckillController {

    private final IGenerator generator;
    private final YqmStoreSeckillService yqmStoreSeckillService;
    private final YqmShippingTemplatesService yqmShippingTemplatesService;
    private final YqmStoreProductRuleService yqmStoreProductRuleService;
    private final YqmStoreProductAttrValueService storeProductAttrValueService;
    private final YqmStoreProductAttrResultService yqmStoreProductAttrResultService;

    public StoreSeckillController(IGenerator generator, YqmStoreSeckillService yqmStoreSeckillService, YqmShippingTemplatesService yqmShippingTemplatesService,
                                  YqmStoreProductRuleService yqmStoreProductRuleService, YqmStoreProductAttrValueService storeProductAttrValueService,
                                  YqmStoreProductAttrResultService yqmStoreProductAttrResultService) {
        this.generator = generator;
        this.yqmStoreSeckillService = yqmStoreSeckillService;
        this.yqmShippingTemplatesService = yqmShippingTemplatesService;
        this.yqmStoreProductRuleService = yqmStoreProductRuleService;
        this.storeProductAttrValueService = storeProductAttrValueService;
        this.yqmStoreProductAttrResultService = yqmStoreProductAttrResultService;
    }

    @Log("列表")
    @ApiOperation(value = "列表")
    @GetMapping(value = "/yqmStoreSeckill")
    @PreAuthorize("hasAnyRole('admin','YQMSTORESECKILL_ALL','YQMSTORESECKILL_SELECT')")
    public ResponseEntity getYqmStoreSeckills(YqmStoreSeckillQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(yqmStoreSeckillService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY, allEntries = true)
    @Log("发布")
    @ApiOperation(value = "发布")
    @PutMapping(value = "/yqmStoreSeckill")
    @PreAuthorize("hasAnyRole('admin','YQMSTORESECKILL_ALL','YQMSTORESECKILL_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmStoreSeckill resources){
        if(ObjectUtil.isNull(resources.getId())){
            return new ResponseEntity<>(yqmStoreSeckillService.save(resources),HttpStatus.CREATED);
        }else{
            yqmStoreSeckillService.saveOrUpdate(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY, allEntries = true)
    @ForbidSubmit
    @Log("删除")
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yqmStoreSeckill/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSTORESECKILL_ALL','YQMSTORESECKILL_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id) {
        yqmStoreSeckillService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY, allEntries = true)
    @Log("新增秒杀")
    @ApiOperation(value = "新增秒杀")
    @PostMapping(value = "/yqmStoreSeckill")
    @PreAuthorize("hasAnyRole('admin','YQMSTORESECKILL_ALL','YQMSTORESECKILL_EDIT')")
    public ResponseEntity add(@Validated @RequestBody YqmStoreSeckillDto resources) {
        return new ResponseEntity<>(yqmStoreSeckillService.saveSeckill(resources), HttpStatus.CREATED);
    }

    @ApiOperation(value = "获取商品信息")
    @GetMapping(value = "/yqmStoreSecKill/info/{id}")
    public ResponseEntity info(@PathVariable Long id) {
        Map<String, Object> map = new LinkedHashMap<>(3);

        //运费模板
        List<YqmShippingTemplates> shippingTemplatesList = yqmShippingTemplatesService.list();
        map.put("tempList", shippingTemplatesList);

        //商品规格
        map.put("ruleList", yqmStoreProductRuleService.list());


        if (id == 0) {
            return new ResponseEntity<>(map, HttpStatus.OK);
        }

        //处理商品详情
        YqmStoreSeckill yqmStoreSeckill = yqmStoreSeckillService.getById(id);
        YqmStoreSeckillDto productDto = new YqmStoreSeckillDto();
        BeanUtil.copyProperties(yqmStoreSeckill, productDto, "images");
        productDto.setSliderImage(Arrays.asList(yqmStoreSeckill.getImages().split(",")));
        YqmStoreProductAttrResult storeProductAttrResult = yqmStoreProductAttrResultService
                .getOne(Wrappers.<YqmStoreProductAttrResult>lambdaQuery()
                        .eq(YqmStoreProductAttrResult::getProductId, yqmStoreSeckill.getProductId()).last("limit 1"));
        JSONObject result = JSON.parseObject(storeProductAttrResult.getResult());

        List<YqmStoreProductAttrValue> attrValues = storeProductAttrValueService.list(new LambdaQueryWrapper<YqmStoreProductAttrValue>().eq(YqmStoreProductAttrValue::getProductId, yqmStoreSeckill.getProductId()));
        List<ProductFormatDto> productFormatDtos = attrValues.stream().map(i -> {
            ProductFormatDto productFormatDto = new ProductFormatDto();
            BeanUtils.copyProperties(i, productFormatDto);
            productFormatDto.setPic(i.getImage());
            return productFormatDto;
        }).collect(Collectors.toList());
        if (SpecTypeEnum.TYPE_1.getValue().equals(yqmStoreSeckill.getSpecType())) {
            productDto.setAttr(new ProductFormatDto());
            productDto.setAttrs(productFormatDtos);
            productDto.setItems(result.getObject("attr", ArrayList.class));
        } else {
            productFormat(productDto, result);
        }


        map.put("productInfo", productDto);

        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 获取商品属性
     * @param productDto
     * @param result
     */
    private void productFormat(YqmStoreSeckillDto productDto, JSONObject result) {
        Map<String, Object> mapAttr = (Map<String, Object>) result.getObject("value", ArrayList.class).get(0);
        ProductFormatDto productFormatDto = ProductFormatDto.builder()
                .pic(mapAttr.get("pic").toString())
                .price(Double.valueOf(mapAttr.get("price").toString()))
                .cost(Double.valueOf(mapAttr.get("cost").toString()))
                .otPrice(Double.valueOf(mapAttr.get("otPrice").toString()))
                .stock(Integer.valueOf(mapAttr.get("stock").toString()))
                .barCode(mapAttr.get("barCode").toString())
                .weight(Double.valueOf(mapAttr.get("weight").toString()))
                .volume(Double.valueOf(mapAttr.get("volume").toString()))
                .value1(mapAttr.get("value1").toString())
                .brokerage(Double.valueOf(mapAttr.get("brokerage").toString()))
                .brokerageTwo(Double.valueOf(mapAttr.get("brokerageTwo").toString()))
                .pinkPrice(Double.valueOf(mapAttr.get("pinkPrice").toString()))
                .pinkStock(Integer.valueOf(mapAttr.get("pinkStock").toString()))
                .seckillPrice(Double.valueOf(mapAttr.get("seckillPrice").toString()))
                .seckillStock(Integer.valueOf(mapAttr.get("seckillStock").toString()))
                .build();
        productDto.setAttr(productFormatDto);
    }
}
