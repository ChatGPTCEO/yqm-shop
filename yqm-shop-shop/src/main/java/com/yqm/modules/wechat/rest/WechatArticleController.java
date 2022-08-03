/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.wechat.rest;

import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.services.WechatArticleService;
import com.yqm.modules.mp.domain.YqmArticle;
import com.yqm.modules.mp.service.YqmArticleService;
import com.yqm.modules.mp.service.dto.YqmArticleDto;
import com.yqm.modules.mp.service.dto.YqmArticleQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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

/**
* @author weiximei
* @date 2019-10-07
*/
@Api(tags = "商城:微信图文管理")
@RestController
@RequestMapping("api")
public class WechatArticleController {

    private final YqmArticleService yqmArticleService;
    private final WechatArticleService wechatArticleService;

    public WechatArticleController(YqmArticleService yqmArticleService,WechatArticleService wechatArticleService) {
        this.yqmArticleService = yqmArticleService;
        this.wechatArticleService = wechatArticleService;
    }

    @ApiOperation(value = "查询单条信息")
    @GetMapping(value = "/yqmArticle/info/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMARTICLE_ALL','YQMARTICLE_GET')")
    public ResponseEntity getInfo(@PathVariable Integer id){
        return new ResponseEntity<>(yqmArticleService.getById(id),HttpStatus.OK);
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/yqmArticle")
    @PreAuthorize("hasAnyRole('admin','YQMARTICLE_ALL','YQMARTICLE_SELECT')")
    public ResponseEntity getYqmArticles(YqmArticleQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmArticleService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/yqmArticle")
    @PreAuthorize("hasAnyRole('admin','YQMARTICLE_ALL','YQMARTICLE_CREATE')")
    public ResponseEntity create(@Validated @RequestBody YqmArticle resources){
        return new ResponseEntity<>(yqmArticleService.save(resources),HttpStatus.CREATED);
    }


    @ApiOperation(value = "修改")
    @PutMapping(value = "/yqmArticle")
    @PreAuthorize("hasAnyRole('admin','YQMARTICLE_ALL','YQMARTICLE_EDIT')")
    public ResponseEntity update(@Validated @RequestBody YqmArticle resources){
        yqmArticleService.saveOrUpdate(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/yqmArticle/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMARTICLE_ALL','YQMARTICLE_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        yqmArticleService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ForbidSubmit
    @ApiOperation(value = "发布文章")
    @GetMapping(value = "/yqmArticle/publish/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMARTICLE_ALL','YQMARTICLE_DELETE')")
    public ResponseEntity publish(@PathVariable Integer id)  throws Exception{
        YqmArticleDto yqmArticleDTO= new YqmArticleDto();
        YqmArticle yqmArticle = yqmArticleService.getById(id);
        BeanUtils.copyProperties(yqmArticle,yqmArticleDTO);
        wechatArticleService.publish(yqmArticleDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

}
