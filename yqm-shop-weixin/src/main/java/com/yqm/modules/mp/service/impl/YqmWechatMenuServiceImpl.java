/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.mp.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.mp.service.mapper.WechatMenuMapper;
import com.yqm.modules.mp.domain.YqmWechatMenu;
import com.yqm.modules.mp.service.YqmWechatMenuService;
import com.yqm.modules.mp.service.dto.YqmWechatMenuDto;
import com.yqm.modules.mp.service.dto.YqmWechatMenuQueryCriteria;
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
//@CacheConfig(cacheNames = "yqmWechatMenu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmWechatMenuServiceImpl extends BaseServiceImpl<WechatMenuMapper, YqmWechatMenu> implements YqmWechatMenuService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmWechatMenuQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmWechatMenu> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmWechatMenuDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmWechatMenu> queryAll(YqmWechatMenuQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmWechatMenu.class, criteria));
    }


    @Override
    public void download(List<YqmWechatMenuDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmWechatMenuDto yqmWechatMenu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("缓存数据", yqmWechatMenu.getResult());
            map.put("缓存时间", yqmWechatMenu.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Boolean isExist(String wechat_menus) {
        YqmWechatMenu yqmWechatMenu = this.getOne(new LambdaQueryWrapper<YqmWechatMenu>()
                .eq(YqmWechatMenu::getKey,wechat_menus));
        if(yqmWechatMenu == null){
            return false;
        }
        return true;
    }
}
