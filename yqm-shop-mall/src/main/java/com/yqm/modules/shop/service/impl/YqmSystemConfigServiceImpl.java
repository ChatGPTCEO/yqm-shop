/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.shop.domain.YqmSystemConfig;
import com.yqm.modules.shop.service.YqmSystemConfigService;
import com.yqm.modules.shop.service.dto.YqmSystemConfigDto;
import com.yqm.modules.shop.service.dto.YqmSystemConfigQueryCriteria;
import com.yqm.modules.shop.service.mapper.SystemConfigMapper;
import com.yqm.utils.FileUtil;
import com.yqm.utils.RedisUtils;
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


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmSystemConfigServiceImpl extends BaseServiceImpl<SystemConfigMapper, YqmSystemConfig> implements YqmSystemConfigService {

    private final IGenerator generator;
    private final RedisUtils redisUtils;

    /**
     * 获取配置值
     * @param name 配置名
     * @return string
     */
    @Override
    public String getData(String name) {
        String result = redisUtils.getY(name);
        if(StrUtil.isNotBlank(result)) {
            return result;
        }

       LambdaQueryWrapper<YqmSystemConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmSystemConfig::getMenuName,name);
        YqmSystemConfig systemConfig = this.baseMapper.selectOne(wrapper);
        if(systemConfig == null) {
            return "";
        }
        return systemConfig.getValue();
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmSystemConfigQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmSystemConfig> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmSystemConfigDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmSystemConfig> queryAll(YqmSystemConfigQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmSystemConfig.class, criteria));
    }


    @Override
    public void download(List<YqmSystemConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmSystemConfigDto yqmSystemConfig : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("字段名称", yqmSystemConfig.getMenuName());
            map.put("默认值", yqmSystemConfig.getValue());
            map.put("排序", yqmSystemConfig.getSort());
            map.put("是否隐藏", yqmSystemConfig.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public YqmSystemConfig findByKey(String key) {
        return this.getOne(new LambdaQueryWrapper<YqmSystemConfig>()
                .eq(YqmSystemConfig::getMenuName,key));
    }
}
