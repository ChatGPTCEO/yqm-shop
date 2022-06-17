/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.template.rest;

import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.exception.BadRequestException;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.product.domain.YqmStoreProduct;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.template.domain.YqmSystemCity;
import com.yqm.modules.template.service.YqmShippingTemplatesService;
import com.yqm.modules.template.service.YqmSystemCityService;
import com.yqm.modules.template.service.dto.ShippingTemplatesDto;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesDto;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesQueryCriteria;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
* @author weiximei
* @date 2020-06-29
*/
@AllArgsConstructor
@Api(tags = "运费模板管理")
@RestController
@RequestMapping("/api/yqmShippingTemplates")
public class ShippingTemplatesController {

    private final YqmShippingTemplatesService yqmShippingTemplatesService;
    private final YqmSystemCityService yqmSystemCityService;
    private final IGenerator generator;
    private final YqmStoreProductService yqmStoreProductService;


    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmShippingTemplates:list')")
    public void download(HttpServletResponse response, YqmShippingTemplatesQueryCriteria criteria) throws IOException {
        yqmShippingTemplatesService.download(generator.convert(yqmShippingTemplatesService.queryAll(criteria), YqmShippingTemplatesDto.class), response);
    }

    @GetMapping
    @Log("查询运费模板")
    @ApiOperation("查询运费模板")
    @PreAuthorize("@el.check('admin','yqmShippingTemplates:list')")
    public ResponseEntity<Object> getYqmShippingTemplatess(YqmShippingTemplatesQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmShippingTemplatesService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @PostMapping("/save/{id}")
    @Log("新增/保存运费模板")
    @ApiOperation("新增/保存运费模板")
    @PreAuthorize("hasAnyRole('admin','yqmShippingTemplates:add','yqmShippingTemplates:edit')")
    public ResponseEntity<Object> create(@PathVariable Integer id,
                                         @Validated @RequestBody ShippingTemplatesDto shippingTemplatesDto){
        yqmShippingTemplatesService.addAndUpdate(id,shippingTemplatesDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @ForbidSubmit
    @Log("删除运费模板")
    @ApiOperation("删除运费模板")
    @PreAuthorize("@el.check('admin','yqmShippingTemplates:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Integer[] ids) {
        List<YqmStoreProduct> productList = yqmStoreProductService.list();
        Arrays.asList(ids).forEach(id->{
            for (YqmStoreProduct yqmStoreProduct : productList) {
                if(id.equals(yqmStoreProduct.getTempId())){
                    throw new BadRequestException("运费模板存在商品关联，请删除对应商品");
                }
            }
            yqmShippingTemplatesService.removeById(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * 获取城市列表
     */
    @Cacheable(cacheNames = ShopConstants.YQM_SHOP_REDIS_SYS_CITY_KEY)
    @GetMapping("/citys")
    public ResponseEntity<Object> cityList()
    {
        List<YqmSystemCity> cityList = yqmSystemCityService.list(Wrappers.<YqmSystemCity>lambdaQuery()
                .eq(YqmSystemCity::getParentId,0));

        for (YqmSystemCity systemCity : cityList){
            List<YqmSystemCity> children = yqmSystemCityService.list(Wrappers
                    .<YqmSystemCity>lambdaQuery()
                    .eq(YqmSystemCity::getParentId,systemCity.getCityId()));

            systemCity.setChildren(children);
        }

        return new ResponseEntity<>(cityList,HttpStatus.OK);
    }

}
