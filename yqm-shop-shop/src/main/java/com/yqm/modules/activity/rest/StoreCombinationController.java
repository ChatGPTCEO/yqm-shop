/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.activity.rest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.SpecTypeEnum;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.activity.domain.YqmStoreCombination;
import com.yqm.modules.activity.service.YqmStoreCombinationService;
import com.yqm.modules.activity.service.dto.YqmStoreCombinationDto;
import com.yqm.modules.activity.service.dto.YqmStoreCombinationQueryCriteria;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author weiximei
* @date 2019-11-18
*/
@Api(tags = "商城:拼团管理")
@RestController
@RequestMapping("api")
public class StoreCombinationController {

    private final YqmStoreCombinationService yqmStoreCombinationService;
    private final YqmShippingTemplatesService yqmShippingTemplatesService;
    private final YqmStoreProductRuleService yqmStoreProductRuleService;
    private final YqmStoreProductAttrResultService yqmStoreProductAttrResultService;
    private final  YqmStoreProductAttrValueService storeProductAttrValueService;
    private final IGenerator generator;
    public StoreCombinationController(YqmStoreCombinationService yqmStoreCombinationService, YqmShippingTemplatesService yqmShippingTemplatesService, YqmStoreProductRuleService yqmStoreProductRuleService, YqmStoreProductAttrResultService yqmStoreProductAttrResultService, YqmStoreProductAttrValueService storeProductAttrValueService, IGenerator generator) {
        this.yqmStoreCombinationService = yqmStoreCombinationService;
        this.yqmShippingTemplatesService = yqmShippingTemplatesService;
        this.yqmStoreProductRuleService = yqmStoreProductRuleService;
        this.yqmStoreProductAttrResultService = yqmStoreProductAttrResultService;
        this.storeProductAttrValueService = storeProductAttrValueService;
        this.generator = generator;
    }

    @Log("查询拼团")
    @ApiOperation(value = "查询拼团")
    @GetMapping(value = "/yqmStoreCombination")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECOMBINATION_ALL','YQMSTORECOMBINATION_SELECT')")
    public ResponseEntity getYqmStoreCombinations(YqmStoreCombinationQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmStoreCombinationService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @Log("新增拼团")
    @ApiOperation(value = "新增拼团")
    @PostMapping(value = "/yqmStoreCombination")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECOMBINATION_ALL','YQMSTORECOMBINATION_EDIT')")
    public ResponseEntity add(@Validated @RequestBody YqmStoreCombinationDto resources){
            return new ResponseEntity<>(yqmStoreCombinationService.saveCombination(resources),HttpStatus.CREATED);
    }

    @ApiOperation(value = "获取商品信息")
    @GetMapping(value = "/yqmStoreCombination/info/{id}")
    public ResponseEntity info(@PathVariable Long id){
        Map<String,Object> map = new LinkedHashMap<>(3);

        //运费模板
        List<YqmShippingTemplates> shippingTemplatesList = yqmShippingTemplatesService.list();
        map.put("tempList", shippingTemplatesList);

        //商品规格
        map.put("ruleList",yqmStoreProductRuleService.list());


        if(id == 0){
            return new ResponseEntity<>(map,HttpStatus.OK);
        }

        //处理商品详情
        YqmStoreCombination yqmStoreCombination = yqmStoreCombinationService.getById(id);
        YqmStoreCombinationDto productDto = new YqmStoreCombinationDto();
        BeanUtil.copyProperties(yqmStoreCombination,productDto,"images");
        productDto.setSliderImage(Arrays.asList(yqmStoreCombination.getImages().split(",")));
        YqmStoreProductAttrResult storeProductAttrResult = yqmStoreProductAttrResultService
                .getOne(Wrappers.<YqmStoreProductAttrResult>lambdaQuery()
                        .eq(YqmStoreProductAttrResult::getProductId,yqmStoreCombination.getProductId()).last("limit 1"));
        JSONObject result = JSON.parseObject(storeProductAttrResult.getResult());
        List<YqmStoreProductAttrValue> attrValues = storeProductAttrValueService.list(new LambdaQueryWrapper<YqmStoreProductAttrValue>().eq(YqmStoreProductAttrValue::getProductId, yqmStoreCombination.getProductId()));
        List<ProductFormatDto> productFormatDtos =attrValues.stream().map(i ->{
            ProductFormatDto productFormatDto = new ProductFormatDto();
            BeanUtils.copyProperties(i,productFormatDto);
            productFormatDto.setPic(i.getImage());
            return productFormatDto;
        }).collect(Collectors.toList());
        if(SpecTypeEnum.TYPE_1.getValue().equals(yqmStoreCombination.getSpecType())){
            productDto.setAttr(new ProductFormatDto());
            productDto.setAttrs(productFormatDtos);
            productDto.setItems(result.getObject("attr",ArrayList.class));
        }else{
            productFormat(productDto, result);
        }

        map.put("productInfo",productDto);

        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    /**
     * 获取商品属性
     * @param productDto
     * @param result
     */
    private void productFormat(YqmStoreCombinationDto productDto, JSONObject result) {
        Map<String,Object> mapAttr = (Map<String,Object>) result.getObject("value",ArrayList.class).get(0);
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

    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @Log("修改拼团")
    @ApiOperation(value = "新增/修改拼团")
    @PutMapping(value = "/yqmStoreCombination")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECOMBINATION_ALL','YQMSTORECOMBINATION_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmStoreCombination resources){
        if(ObjectUtil.isNull(resources.getId())){
            return new ResponseEntity<>(yqmStoreCombinationService.save(resources),HttpStatus.CREATED);
        }else{
            yqmStoreCombinationService.saveOrUpdate(resources);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

    }
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @ForbidSubmit
    @ApiOperation(value = "开启关闭")
    @PostMapping(value = "/yqmStoreCombination/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Long id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer status = jsonObject.getInteger("status");
        yqmStoreCombinationService.onSale(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @ForbidSubmit
    @Log("删除拼团")
    @ApiOperation(value = "删除拼团")
    @DeleteMapping(value = "/yqmStoreCombination/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECOMBINATION_ALL','YQMSTORECOMBINATION_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        yqmStoreCombinationService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
