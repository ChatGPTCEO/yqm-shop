/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.template.service.impl;


import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.template.domain.YqmShippingTemplatesFree;
import com.yqm.modules.template.service.YqmShippingTemplatesFreeService;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesFreeDto;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesFreeQueryCriteria;
import com.yqm.modules.template.service.mapper.YqmShippingTemplatesFreeMapper;
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
public class YqmShippingTemplatesFreeServiceImpl extends BaseServiceImpl<YqmShippingTemplatesFreeMapper, YqmShippingTemplatesFree> implements YqmShippingTemplatesFreeService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmShippingTemplatesFreeQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmShippingTemplatesFree> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmShippingTemplatesFreeDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmShippingTemplatesFree> queryAll(YqmShippingTemplatesFreeQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmShippingTemplatesFree.class, criteria));
    }


    @Override
    public void download(List<YqmShippingTemplatesFreeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmShippingTemplatesFreeDto yqmShippingTemplatesFree : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("省ID", yqmShippingTemplatesFree.getProvinceId());
            map.put("模板ID", yqmShippingTemplatesFree.getTempId());
            map.put("城市ID", yqmShippingTemplatesFree.getCityId());
            map.put("包邮件数", yqmShippingTemplatesFree.getNumber());
            map.put("包邮金额", yqmShippingTemplatesFree.getPrice());
            map.put("计费方式", yqmShippingTemplatesFree.getType());
            map.put("分组唯一值", yqmShippingTemplatesFree.getUniqid());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
