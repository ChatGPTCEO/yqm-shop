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
import com.yqm.modules.template.domain.YqmSystemCity;
import com.yqm.modules.template.service.YqmSystemCityService;
import com.yqm.modules.template.service.dto.YqmSystemCityDto;
import com.yqm.modules.template.service.dto.YqmSystemCityQueryCriteria;
import com.yqm.modules.template.service.mapper.YqmSystemCityMapper;
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
* @date 2020-06-29
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmSystemCityServiceImpl extends BaseServiceImpl<YqmSystemCityMapper, YqmSystemCity> implements YqmSystemCityService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmSystemCityQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmSystemCity> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmSystemCityDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmSystemCity> queryAll(YqmSystemCityQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmSystemCity.class, criteria));
    }


    @Override
    public void download(List<YqmSystemCityDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmSystemCityDto yqmSystemCity : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("城市id", yqmSystemCity.getCityId());
            map.put("省市级别", yqmSystemCity.getLevel());
            map.put("父级id", yqmSystemCity.getParentId());
            map.put("区号", yqmSystemCity.getAreaCode());
            map.put("名称", yqmSystemCity.getName());
            map.put("合并名称", yqmSystemCity.getMergerName());
            map.put("经度", yqmSystemCity.getLng());
            map.put("纬度", yqmSystemCity.getLat());
            map.put("是否展示", yqmSystemCity.getIsShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
