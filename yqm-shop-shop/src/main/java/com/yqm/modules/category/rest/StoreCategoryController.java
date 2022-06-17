/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.category.rest;

import cn.hutool.core.util.StrUtil;
import com.yqm.api.YqmShopException;
import com.yqm.constant.ShopConstants;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.category.domain.YqmStoreCategory;
import com.yqm.modules.category.service.YqmStoreCategoryService;
import com.yqm.modules.category.service.dto.YqmStoreCategoryDto;
import com.yqm.modules.category.service.dto.YqmStoreCategoryQueryCriteria;
import com.yqm.modules.product.domain.YqmStoreProduct;
import com.yqm.modules.product.service.YqmStoreProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author weiximei
* @date 2019-10-03
*/
@Api(tags = "商城:商品分类管理")
@RestController
@RequestMapping("api")
public class StoreCategoryController {


    private final YqmStoreCategoryService yqmStoreCategoryService;
    private final YqmStoreProductService yqmStoreProductService;


    public StoreCategoryController(YqmStoreCategoryService yqmStoreCategoryService,
                                   YqmStoreProductService yqmStoreProductService) {
        this.yqmStoreCategoryService = yqmStoreCategoryService;
        this.yqmStoreProductService = yqmStoreProductService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/yqmStoreCategory/download")
    @PreAuthorize("@el.check('admin','YQMSTORECATEGORY_SELECT')")
    public void download(HttpServletResponse response, YqmStoreCategoryQueryCriteria criteria) throws IOException {
        yqmStoreCategoryService.download(yqmStoreCategoryService.queryAll(criteria), response);
    }


    @Log("查询商品分类")
    @ApiOperation(value = "查询商品分类")
    @GetMapping(value = "/yqmStoreCategory")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECATEGORY_ALL','YQMSTORECATEGORY_SELECT')")
    public ResponseEntity getYqmStoreCategorys(YqmStoreCategoryQueryCriteria criteria, Pageable pageable){
        List<YqmStoreCategoryDto> categoryDTOList = yqmStoreCategoryService.queryAll(criteria);
        return new ResponseEntity<>(yqmStoreCategoryService.buildTree(categoryDTOList),HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增商品分类")
    @ApiOperation(value = "新增商品分类")
    @PostMapping(value = "/yqmStoreCategory")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YQMSTORECATEGORY_ALL','YQMSTORECATEGORY_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YqmStoreCategory resources){
        if(resources.getPid() != null && resources.getPid() > 0 && StrUtil.isBlank(resources.getPic())) {
            throw new YqmShopException("子分类图片必传");
        }

        boolean checkResult = yqmStoreCategoryService.checkCategory(resources.getPid());
        if(!checkResult) {
            throw new YqmShopException("分类最多能添加2级哦");
        }

        return new ResponseEntity<>(yqmStoreCategoryService.save(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @Log("修改商品分类")
    @ApiOperation(value = "修改商品分类")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @PutMapping(value = "/yqmStoreCategory")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECATEGORY_ALL','YQMSTORECATEGORY_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmStoreCategory resources){
        if(resources.getPid() != null && resources.getPid() > 0 && StrUtil.isBlank(resources.getPic())) {
            throw new YqmShopException("子分类图片必传");
        }

        if(resources.getId().equals(resources.getPid())){
            throw new YqmShopException("自己不能选择自己哦");
        }

        boolean checkResult = yqmStoreCategoryService.checkCategory(resources.getPid());

        if(!checkResult) {
            throw new YqmShopException("分类最多能添加2级哦");
        }

        yqmStoreCategoryService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除商品分类")
    @ApiOperation(value = "删除商品分类")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY,allEntries = true)
    @DeleteMapping(value = "/yqmStoreCategory/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSTORECATEGORY_ALL','YQMSTORECATEGORY_DELETE')")
    public ResponseEntity delete(@PathVariable String id){
        String[] ids = id.split(",");
        for (String newId: ids) {
            this.delCheck(Integer.valueOf(newId));
            yqmStoreCategoryService.removeById(Integer.valueOf(newId));
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 检测删除分类
     * @param id 分类id
     */
    private void delCheck(Integer id){
        int count = yqmStoreCategoryService.lambdaQuery()
                .eq(YqmStoreCategory::getPid,id)
                .count();
        if(count > 0) {
            throw new YqmShopException("请先删除子分类");
        }

        int countP = yqmStoreProductService.lambdaQuery()
                .eq(YqmStoreProduct::getCateId,id)
                .count();

        if(countP > 0) {
            throw new YqmShopException("当前分类下有商品不可删除");
        }
    }
}
