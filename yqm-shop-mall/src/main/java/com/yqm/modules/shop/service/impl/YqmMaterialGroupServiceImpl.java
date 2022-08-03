/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.shop.domain.YqmMaterialGroup;
import com.yqm.modules.shop.service.YqmMaterialGroupService;
import com.yqm.modules.shop.service.dto.YqmMaterialGroupDto;
import com.yqm.modules.shop.service.dto.YqmMaterialGroupQueryCriteria;
import com.yqm.modules.shop.service.mapper.MaterialGroupMapper;
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
public class YqmMaterialGroupServiceImpl extends BaseServiceImpl<MaterialGroupMapper, YqmMaterialGroup> implements YqmMaterialGroupService {

    private final IGenerator generator;

    @Override
    public Map<String, Object> queryAll(YqmMaterialGroupQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmMaterialGroup> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmMaterialGroupDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<YqmMaterialGroup> queryAll(YqmMaterialGroupQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmMaterialGroup.class, criteria));
    }


    @Override
    public void download(List<YqmMaterialGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmMaterialGroupDto yqmMaterialGroup : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", yqmMaterialGroup.getCreateTime());
            map.put("创建者ID", yqmMaterialGroup.getCreateId());
            map.put("分组名", yqmMaterialGroup.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
