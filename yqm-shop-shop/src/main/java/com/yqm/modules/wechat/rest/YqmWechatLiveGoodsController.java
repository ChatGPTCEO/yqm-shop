/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.wechat.rest;

import com.yqm.dozer.service.IGenerator;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.mp.domain.YqmWechatLiveGoods;
import com.yqm.modules.mp.service.YqmWechatLiveGoodsService;
import com.yqm.modules.mp.service.dto.YqmWechatLiveGoodsDto;
import com.yqm.modules.mp.service.dto.YqmWechatLiveGoodsQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
* @author weiximei
* @date 2020-08-11
*/
@AllArgsConstructor
@Api(tags = "yqmWechatLiveGoods管理")
@RestController
@RequestMapping("/api/yqmWechatLiveGoods")
public class YqmWechatLiveGoodsController {

    private final YqmWechatLiveGoodsService yqmWechatLiveGoodsService;
    private final IGenerator generator;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmWechatLiveGoods:list')")
    public void download(HttpServletResponse response, YqmWechatLiveGoodsQueryCriteria criteria) throws IOException {
        yqmWechatLiveGoodsService.download(generator.convert(yqmWechatLiveGoodsService.queryAll(criteria), YqmWechatLiveGoodsDto.class), response);
    }

    @GetMapping
    @Log("查询yqmWechatLiveGoods")
    @ApiOperation("查询yqmWechatLiveGoods")
    @PreAuthorize("@el.check('admin','yqmWechatLiveGoods:list')")
    public ResponseEntity<Object> getYqmWechatLiveGoodss(YqmWechatLiveGoodsQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(yqmWechatLiveGoodsService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ForbidSubmit
    @PostMapping
    @Log("新增yqmWechatLiveGoods")
    @ApiOperation("新增yqmWechatLiveGoods")
    @PreAuthorize("@el.check('admin','yqmWechatLiveGoods:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody YqmWechatLiveGoods resources){
        return new ResponseEntity<>(yqmWechatLiveGoodsService.saveGoods(resources),HttpStatus.CREATED);
    }

    @ForbidSubmit
    @PutMapping
    @Log("修改yqmWechatLiveGoods")
    @ApiOperation("修改yqmWechatLiveGoods")
    @PreAuthorize("@el.check('admin','yqmWechatLiveGoods:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody YqmWechatLiveGoods resources){
        yqmWechatLiveGoodsService.updateGoods(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除yqmWechatLiveGoods")
    @ApiOperation("删除yqmWechatLiveGoods")
    @PreAuthorize("@el.check('admin','yqmWechatLiveGoods:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(id->{
                yqmWechatLiveGoodsService.removeGoods(id);
        });
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("同步数据")
    @PostMapping("/synchro")
    public ResponseEntity<Object> synchroWxOlLiveGoods(@RequestBody Integer[] ids) {
        yqmWechatLiveGoodsService.synchroWxOlLive(Arrays.asList(ids));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
