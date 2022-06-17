/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.canvas.service.impl;

import com.yqm.modules.canvas.domain.StoreCanvas;
import com.yqm.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import com.yqm.dozer.service.IGenerator;
import com.github.pagehelper.PageInfo;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.utils.FileUtil;
import com.yqm.modules.canvas.service.StoreCanvasService;
import com.yqm.modules.canvas.service.dto.StoreCanvasDto;
import com.yqm.modules.canvas.service.dto.StoreCanvasQueryCriteria;
import com.yqm.modules.canvas.service.mapper.StoreCanvasMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.yqm.domain.PageResult;
/**
* @author yqm-shop
* @date 2021-02-01
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "storeCanvas")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreCanvasServiceImpl extends BaseServiceImpl<StoreCanvasMapper, StoreCanvas> implements StoreCanvasService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<StoreCanvasDto> queryAll(StoreCanvasQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<StoreCanvas> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,StoreCanvasDto.class);
    }


    @Override
    //@Cacheable
    public List<StoreCanvas> queryAll(StoreCanvasQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreCanvas.class, criteria));
    }


    @Override
    public void download(List<StoreCanvasDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StoreCanvasDto storeCanvas : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("终端 1-小程序 2-H5 3-APP 4-PC", storeCanvas.getTerminal());
            map.put("画布json数据", storeCanvas.getJson());
            map.put("类型 1-系统画布 2-自定义页面 3-商家店铺装修", storeCanvas.getType());
            map.put("名称", storeCanvas.getName());
            map.put("店铺id，当type=3的时候，值为具体的店铺id，其它情况为0", storeCanvas.getShopId());
            map.put("创建时间", storeCanvas.getCreateTime());
            map.put("修改时间", storeCanvas.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
