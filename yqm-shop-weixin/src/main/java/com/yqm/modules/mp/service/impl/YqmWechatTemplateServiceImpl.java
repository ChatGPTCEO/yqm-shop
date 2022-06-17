/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.mp.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.mp.domain.YqmWechatTemplate;
import com.yqm.modules.mp.service.YqmWechatTemplateService;
import com.yqm.modules.mp.service.dto.YqmWechatTemplateQueryCriteria;
import com.yqm.modules.mp.service.dto.YqmWechatTemplateDto;
import com.yqm.modules.mp.service.mapper.WechatTemplateMapper;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yqmWechatTemplate")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmWechatTemplateServiceImpl extends BaseServiceImpl<WechatTemplateMapper, YqmWechatTemplate> implements YqmWechatTemplateService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmWechatTemplateQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmWechatTemplate> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmWechatTemplateDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmWechatTemplate> queryAll(YqmWechatTemplateQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmWechatTemplate.class, criteria));
    }


    @Override
    public void download(List<YqmWechatTemplateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmWechatTemplateDto yqmWechatTemplate : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("模板编号", yqmWechatTemplate.getTempkey());
            map.put("模板名", yqmWechatTemplate.getName());
            map.put("回复内容", yqmWechatTemplate.getContent());
            map.put("模板ID", yqmWechatTemplate.getTempid());
            map.put("添加时间", yqmWechatTemplate.getAddTime());
            map.put("状态", yqmWechatTemplate.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public YqmWechatTemplate findByTempkey(String recharge_success_key) {
        return this.getOne(new LambdaQueryWrapper<YqmWechatTemplate>()
                .eq(YqmWechatTemplate::getTempkey,recharge_success_key));
    }
}
