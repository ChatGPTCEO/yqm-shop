/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.shop.domain.YqmMaterial;
import com.yqm.modules.shop.service.YqmMaterialService;
import com.yqm.modules.shop.service.dto.YqmMaterialDto;
import com.yqm.modules.shop.service.dto.YqmMaterialQueryCriteria;
import com.yqm.modules.shop.service.mapper.MaterialMapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class YqmMaterialServiceImpl extends BaseServiceImpl<MaterialMapper, YqmMaterial> implements YqmMaterialService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmMaterialQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmMaterial> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmMaterialDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmMaterial> queryAll(YqmMaterialQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmMaterial.class, criteria));
    }



}
