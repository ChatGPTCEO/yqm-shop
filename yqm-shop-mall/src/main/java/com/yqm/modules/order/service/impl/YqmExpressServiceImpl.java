/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.order.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.order.domain.YqmExpress;
import com.yqm.modules.order.service.YqmExpressService;
import com.yqm.modules.order.service.dto.YqmExpressDto;
import com.yqm.modules.order.service.dto.YqmExpressQueryCriteria;
import com.yqm.modules.order.service.mapper.ExpressMapper;
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


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmExpressServiceImpl extends BaseServiceImpl<ExpressMapper, YqmExpress> implements YqmExpressService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmExpressQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmExpress> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmExpressDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmExpress> queryAll(YqmExpressQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmExpress.class, criteria));
    }


    @Override
    public void download(List<YqmExpressDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmExpressDto yqmExpress : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("快递公司简称", yqmExpress.getCode());
            map.put("快递公司全称", yqmExpress.getName());
            map.put("排序", yqmExpress.getSort());
            map.put("是否显示", yqmExpress.getIsShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
