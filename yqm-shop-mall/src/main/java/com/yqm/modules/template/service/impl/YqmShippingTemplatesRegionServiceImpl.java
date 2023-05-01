/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.template.service.impl;


import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.template.domain.YqmShippingTemplatesRegion;
import com.yqm.modules.template.service.YqmShippingTemplatesRegionService;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesRegionDto;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesRegionQueryCriteria;
import com.yqm.modules.template.service.mapper.YqmShippingTemplatesRegionMapper;
import com.yqm.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author weiximei
* @date 2020-06-29
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yqmShippingTemplatesRegion")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmShippingTemplatesRegionServiceImpl extends BaseServiceImpl<YqmShippingTemplatesRegionMapper, YqmShippingTemplatesRegion> implements YqmShippingTemplatesRegionService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmShippingTemplatesRegionQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmShippingTemplatesRegion> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmShippingTemplatesRegionDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmShippingTemplatesRegion> queryAll(YqmShippingTemplatesRegionQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmShippingTemplatesRegion.class, criteria));
    }


    @Override
    public void download(List<YqmShippingTemplatesRegionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmShippingTemplatesRegionDto yqmShippingTemplatesRegion : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("省ID", yqmShippingTemplatesRegion.getProvinceId());
            map.put("模板ID", yqmShippingTemplatesRegion.getTempId());
            map.put("城市ID", yqmShippingTemplatesRegion.getCityId());
            map.put("首件", yqmShippingTemplatesRegion.getFirst());
            map.put("首件运费", yqmShippingTemplatesRegion.getFirstPrice());
            map.put("续件", yqmShippingTemplatesRegion.getContinues());
            map.put("续件运费", yqmShippingTemplatesRegion.getContinuePrice());
            map.put("计费方式", yqmShippingTemplatesRegion.getType());
            map.put("分组唯一值", yqmShippingTemplatesRegion.getUniqid());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
