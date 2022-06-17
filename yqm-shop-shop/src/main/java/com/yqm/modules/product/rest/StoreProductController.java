/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.rest;

import cn.hutool.core.bean.BeanUtil;
import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.enums.SpecTypeEnum;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.category.domain.YqmStoreCategory;
import com.yqm.modules.category.service.YqmStoreCategoryService;
import com.yqm.modules.product.domain.YqmStoreProduct;
import com.yqm.modules.product.domain.YqmStoreProductAttrResult;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.service.YqmStoreProductAttrResultService;
import com.yqm.modules.product.service.YqmStoreProductAttrValueService;
import com.yqm.modules.product.service.YqmStoreProductRuleService;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.service.dto.ProductDto;
import com.yqm.modules.product.service.dto.ProductFormatDto;
import com.yqm.modules.product.service.dto.StoreProductDto;
import com.yqm.modules.product.service.dto.YqmStoreProductQueryCriteria;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author weiximei
 * @date 2019-10-04
 */
@Api(tags = "商城:商品管理")
@RestController
@RequestMapping("api")
public class StoreProductController {

    private final YqmStoreProductService yqmStoreProductService;
    private final YqmStoreCategoryService yqmStoreCategoryService;
    private final YqmShippingTemplatesService yqmShippingTemplatesService;
    private final YqmStoreProductRuleService yqmStoreProductRuleService;
    private final YqmStoreProductAttrResultService yqmStoreProductAttrResultService;
    private final YqmStoreProductAttrValueService storeProductAttrValueService;
    private final IGenerator generator;
    public StoreProductController(YqmStoreProductService yqmStoreProductService,
                                  YqmStoreCategoryService yqmStoreCategoryService,
                                  YqmShippingTemplatesService yqmShippingTemplatesService,
                                  YqmStoreProductRuleService yqmStoreProductRuleService,
                                  YqmStoreProductAttrResultService yqmStoreProductAttrResultService, YqmStoreProductAttrValueService storeProductAttrValueService, IGenerator generator) {
        this.yqmStoreProductService = yqmStoreProductService;
        this.yqmStoreCategoryService = yqmStoreCategoryService;
        this.yqmShippingTemplatesService = yqmShippingTemplatesService;
        this.yqmStoreProductRuleService = yqmStoreProductRuleService;
        this.yqmStoreProductAttrResultService = yqmStoreProductAttrResultService;
        this.storeProductAttrValueService = storeProductAttrValueService;
        this.generator = generator;
    }

    @Log("查询商品")
    @ApiOperation(value = "查询商品")
    @GetMapping(value = "/yqmStoreProduct")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREPRODUCT_ALL','YQMSTOREPRODUCT_SELECT')")
    public ResponseEntity getYqmStoreProducts(YqmStoreProductQueryCriteria criteria, Pageable pageable){
        //商品分类
        List<YqmStoreCategory> storeCategories = yqmStoreCategoryService.lambdaQuery()
                .eq(YqmStoreCategory::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .orderByAsc(YqmStoreCategory::getPid)
                .list();
        List<Map<String,Object>> cateList = new ArrayList<>();
        Map<String, Object> queryAll = yqmStoreProductService.queryAll(criteria, pageable);
        queryAll.put("cateList", this.makeCate(storeCategories,cateList,0,1));
        return new ResponseEntity<>(queryAll,HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增/修改商品")
    @ApiOperation(value = "新增/修改商品")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yqmStoreProduct/addOrSave")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREPRODUCT_ALL','YQMSTOREPRODUCT_CREATE')")
    public ResponseEntity create(@Validated @RequestBody StoreProductDto storeProductDto){
        yqmStoreProductService.insertAndEditYqmStoreProduct(storeProductDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ForbidSubmit
    @Log("删除商品")
    @ApiOperation(value = "删除商品")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/yqmStoreProduct/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSTOREPRODUCT_ALL','YQMSTOREPRODUCT_DELETE')")
    public ResponseEntity delete(@PathVariable Long id){
        yqmStoreProductService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }



    @ApiOperation(value = "商品上架/下架")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/yqmStoreProduct/onsale/{id}")
    public ResponseEntity onSale(@PathVariable Long id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer status = jsonObject.getInteger("status");
        yqmStoreProductService.onSale(id,status);
        return new ResponseEntity(HttpStatus.OK);
    }
    @ApiOperation(value = "生成属性（添加活动产品专用）")
    @PostMapping(value = "/yqmStoreProduct/isFormatAttrForActivity/{id}")
    public ResponseEntity isFormatAttrForActivity(@PathVariable Long id,@RequestBody String jsonStr){
        return new ResponseEntity<>(yqmStoreProductService.getFormatAttr(id,jsonStr,true),HttpStatus.OK);
    }

    @ApiOperation(value = "生成属性")
    @PostMapping(value = "/yqmStoreProduct/isFormatAttr/{id}")
    public ResponseEntity isFormatAttr(@PathVariable Long id,@RequestBody String jsonStr){
        return new ResponseEntity<>(yqmStoreProductService.getFormatAttr(id,jsonStr,false),HttpStatus.OK);
    }



    @ApiOperation(value = "获取商品信息")
    @GetMapping(value = "/yqmStoreProduct/info/{id}")
    public ResponseEntity info(@PathVariable Long id){
        Map<String,Object> map = new LinkedHashMap<>(3);

        //运费模板
        List<YqmShippingTemplates> shippingTemplatesList = yqmShippingTemplatesService.list();
        map.put("tempList", shippingTemplatesList);

        //商品分类
        List<YqmStoreCategory> storeCategories = yqmStoreCategoryService.lambdaQuery()
                .eq(YqmStoreCategory::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .orderByAsc(YqmStoreCategory::getPid)
                .list();

        List<Map<String,Object>> cateList = new ArrayList<>();
        map.put("cateList", this.makeCate(storeCategories,cateList,0,1));

        //商品规格
        map.put("ruleList",yqmStoreProductRuleService.list());


        if(id == 0){
            return new ResponseEntity<>(map,HttpStatus.OK);
        }

        //处理商品详情
        YqmStoreProduct yqmStoreProduct = yqmStoreProductService.getById(id);
        ProductDto productDto = new ProductDto();
        BeanUtil.copyProperties(yqmStoreProduct,productDto,"sliderImage");
        productDto.setSliderImage(Arrays.asList(yqmStoreProduct.getSliderImage().split(",")));
        YqmStoreProductAttrResult storeProductAttrResult = yqmStoreProductAttrResultService
                .getOne(Wrappers.<YqmStoreProductAttrResult>lambdaQuery()
                        .eq(YqmStoreProductAttrResult::getProductId,id).last("limit 1"));
        JSONObject result = JSON.parseObject(storeProductAttrResult.getResult());
        List<YqmStoreProductAttrValue> attrValues = storeProductAttrValueService.list(new LambdaQueryWrapper<YqmStoreProductAttrValue>().eq(YqmStoreProductAttrValue::getProductId, yqmStoreProduct.getId()));
        List<ProductFormatDto> productFormatDtos =attrValues.stream().map(i ->{
            ProductFormatDto productFormatDto = new ProductFormatDto();
            BeanUtils.copyProperties(i,productFormatDto);
            productFormatDto.setPic(i.getImage());
            return productFormatDto;
        }).collect(Collectors.toList());
        if(SpecTypeEnum.TYPE_1.getValue().equals(yqmStoreProduct.getSpecType())){
            productDto.setAttr(new ProductFormatDto());
            productDto.setAttrs(productFormatDtos);
            productDto.setItems(result.getObject("attr",ArrayList.class));
        }else{

            productFromat(productDto, result);
        }

        map.put("productInfo",productDto);

        return new ResponseEntity<>(map,HttpStatus.OK);
    }

    /**
     * 获取商品属性
     * @param productDto
     * @param result
     */
    private void productFromat(ProductDto productDto, JSONObject result) {
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
                .integral(mapAttr.get("integral") !=null ? Integer.valueOf(mapAttr.get("integral").toString()) : 0)
                .brokerage(Double.valueOf(mapAttr.get("brokerage").toString()))
                .brokerageTwo(Double.valueOf(mapAttr.get("brokerageTwo").toString()))
                .pinkPrice(Double.valueOf(mapAttr.get("pinkPrice").toString()))
                .pinkStock(Integer.valueOf(mapAttr.get("pinkStock").toString()))
                .seckillPrice(Double.valueOf(mapAttr.get("seckillPrice").toString()))
                .seckillStock(Integer.valueOf(mapAttr.get("seckillStock").toString()))
                .build();
        productDto.setAttr(productFormatDto);
    }


    /**
     *  分类递归
     * @param data 分类列表
     * @param pid 附件id
     * @param level d等级
     * @return list
     */
    private List<Map<String,Object>> makeCate(List<YqmStoreCategory> data,List<Map<String,Object>> cateList,int pid, int level)
    {
        String html = "|-----";
        String newHtml = "";
        List<YqmStoreCategory> storeCategories = yqmStoreCategoryService.lambdaQuery().eq(YqmStoreCategory::getPid, 0).list();

        for (int i = 0; i < data.size(); i++) {
            YqmStoreCategory storeCategory = data.get(i);
            int catePid =  storeCategory.getPid();
            Map<String,Object> map = new HashMap<>();
            if(catePid == pid){
                newHtml = String.join("", Collections.nCopies(level,html));
                map.put("value",storeCategory.getId());
                map.put("label",newHtml + storeCategory.getCateName());
                if(storeCategory.getPid() == 0){
                    map.put("disabled",0);
                }else{
                    map.put("disabled",1);
                }
                cateList.add(map);
                data.remove(i);

                i--;
                if(storeCategory.getPid() > 0){
                    this.makeCate(data,cateList,storeCategory.getPid(),level);
                }else{
                    this.makeCate(data,cateList,storeCategory.getId(),level + 1);
                }

            }
        }


        return cateList;
    }

}
