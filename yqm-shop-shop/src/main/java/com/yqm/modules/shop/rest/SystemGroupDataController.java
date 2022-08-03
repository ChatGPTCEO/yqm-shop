/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 */
package com.yqm.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yqm.constant.ShopConstants;
import com.yqm.exception.BadRequestException;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.aop.ForbidSubmit;
import com.yqm.modules.shop.domain.YqmSystemGroupData;
import com.yqm.modules.shop.service.YqmSystemGroupDataService;
import com.yqm.modules.shop.service.dto.YqmSystemGroupDataQueryCriteria;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yqm.constant.ShopConstants.YQM_SHOP_SECKILL_TIME;

/**
 * @author weiximei
 * @date 2019-10-18
 */
@Api(tags = "商城:数据配置管理")
@RestController
@RequestMapping("api")
public class SystemGroupDataController {

    private final YqmSystemGroupDataService yqmSystemGroupDataService;

    public SystemGroupDataController(YqmSystemGroupDataService yqmSystemGroupDataService) {
        this.yqmSystemGroupDataService = yqmSystemGroupDataService;
    }

    @Log("查询数据配置")
    @ApiOperation(value = "查询数据配置")
    @GetMapping(value = "/yqmSystemGroupData")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMGROUPDATA_ALL','YQMSYSTEMGROUPDATA_SELECT')")
    public ResponseEntity getYqmSystemGroupDatas(YqmSystemGroupDataQueryCriteria criteria,
                                                Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "sort");
        Pageable pageableT = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sort);
        return new ResponseEntity<>(yqmSystemGroupDataService.queryAll(criteria, pageableT), HttpStatus.OK);
    }

    @ForbidSubmit
    @Log("新增数据配置")
    @ApiOperation(value = "新增数据配置")
    @PostMapping(value = "/yqmSystemGroupData")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY, allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMGROUPDATA_ALL','YQMSYSTEMGROUPDATA_CREATE')")
    public ResponseEntity create(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        this.checkParam(jsonObject);

        YqmSystemGroupData yqmSystemGroupData = new YqmSystemGroupData();
        yqmSystemGroupData.setGroupName(jsonObject.get("groupName").toString());
        jsonObject.remove("groupName");
        yqmSystemGroupData.setValue(jsonObject.toJSONString());
        yqmSystemGroupData.setStatus(jsonObject.getInteger("status"));
        yqmSystemGroupData.setSort(jsonObject.getInteger("sort"));

        List<YqmSystemGroupData> yqmSeckillTime = yqmSystemGroupDataService.list(Wrappers.<YqmSystemGroupData>lambdaQuery()
                .eq(YqmSystemGroupData::getGroupName, YQM_SHOP_SECKILL_TIME));
        if (yqmSystemGroupData.getStatus() == 1) {
            yqmSeckillTime.forEach(item -> {
                Map map = JSONUtil.toBean(item.getValue(), Map.class);
                if (Objects.nonNull(jsonObject.getInteger("time")) && jsonObject.getInteger("time").equals(map.get("time"))) {
                    throw new BadRequestException("不能同时开启同一时间点");
                }
            });
        }

        return new ResponseEntity<>(yqmSystemGroupDataService.save(yqmSystemGroupData), HttpStatus.CREATED);
    }

    @ForbidSubmit
    @Log("修改数据配置")
    @ApiOperation(value = "修改数据配置")
    @PutMapping(value = "/yqmSystemGroupData")
    @CacheEvict(cacheNames = ShopConstants.YQM_SHOP_REDIS_INDEX_KEY, allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMGROUPDATA_ALL','YQMSYSTEMGROUPDATA_EDIT')")
    public ResponseEntity update(@RequestBody String jsonStr) {
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        this.checkParam(jsonObject);

        YqmSystemGroupData yqmSystemGroupData = new YqmSystemGroupData();

        yqmSystemGroupData.setGroupName(jsonObject.get("groupName").toString());
        jsonObject.remove("groupName");
        yqmSystemGroupData.setValue(jsonObject.toJSONString());
        yqmSystemGroupData.setStatus(jsonObject.getInteger("status"));

        List<YqmSystemGroupData> yqm_shop_seckill_time = yqmSystemGroupDataService.list(Wrappers.<YqmSystemGroupData>lambdaQuery()
                .eq(YqmSystemGroupData::getGroupName, "yqm_shop_seckill_time"));
        if (yqmSystemGroupData.getStatus() == 1 && ObjectUtil.isNotEmpty(jsonObject.getInteger("time"))) {
            yqm_shop_seckill_time.forEach(item -> {
                Map map = JSONUtil.toBean(item.getValue(), Map.class);
                if (jsonObject.getInteger("time").equals(map.get("time"))) {
                    throw new BadRequestException("不能同时开启同一时间点");
                }
            });
        }

        if (jsonObject.getInteger("status") == null) {
            yqmSystemGroupData.setStatus(1);
        } else {
            yqmSystemGroupData.setStatus(jsonObject.getInteger("status"));
        }

        if (jsonObject.getInteger("sort") == null) {
            yqmSystemGroupData.setSort(0);
        } else {
            yqmSystemGroupData.setSort(jsonObject.getInteger("sort"));
        }


        yqmSystemGroupData.setId(Integer.valueOf(jsonObject.get("id").toString()));
        yqmSystemGroupDataService.saveOrUpdate(yqmSystemGroupData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ForbidSubmit
    @Log("删除数据配置")
    @ApiOperation(value = "删除数据配置")
    @DeleteMapping(value = "/yqmSystemGroupData/{id}")
    @PreAuthorize("hasAnyRole('admin','YQMSYSTEMGROUPDATA_ALL','YQMSYSTEMGROUPDATA_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id) {
        yqmSystemGroupDataService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 检测参数
     *
     * @param jsonObject
     */
    private void checkParam(JSONObject jsonObject) {
        if (ObjectUtil.isNotNull(jsonObject.get("name"))) {
            if (StrUtil.isEmpty(jsonObject.getString("name"))) {
                throw new BadRequestException("名称必须填写");
            }
        }

        if (ObjectUtil.isNotNull(jsonObject.get("title"))) {
            if (StrUtil.isEmpty(jsonObject.getString("title"))) {
                throw new BadRequestException("标题必须填写");
            }
        }

        if (ObjectUtil.isNotNull(jsonObject.get("pic"))) {
            if (StrUtil.isEmpty(jsonObject.getString("pic"))) {
                throw new BadRequestException("图片必须上传");
            }
        }


        if (ObjectUtil.isNotNull(jsonObject.get("info"))) {
            if (StrUtil.isEmpty(jsonObject.getString("info"))) {
                throw new BadRequestException("简介必须填写");
            }
        }

    }
}
