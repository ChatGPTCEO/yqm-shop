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
import com.yqm.enums.CommonEnum;
import com.yqm.modules.shop.domain.YqmSystemGroupData;
import com.yqm.modules.shop.service.YqmSystemGroupDataService;
import com.yqm.modules.shop.service.dto.YqmSystemGroupDataDto;
import com.yqm.modules.shop.service.dto.YqmSystemGroupDataQueryCriteria;
import com.yqm.modules.shop.service.mapper.SystemGroupDataMapper;
import com.yqm.utils.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import java.util.stream.Collectors;



/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmSystemGroupDataServiceImpl extends BaseServiceImpl<SystemGroupDataMapper, YqmSystemGroupData> implements YqmSystemGroupDataService {

    private final IGenerator generator;


    /**
     * 获取配置数据
     * @param name 配置名称
     * @return List
     */
    @Override
    //@Cacheable(value = "yqm-shop:configDatas",key = "#name")
    public List<JSONObject> getDatas(String name) {
        List<YqmSystemGroupData> systemGroupDatas = this.baseMapper
                .selectList(Wrappers.<YqmSystemGroupData>lambdaQuery()
                        .eq(YqmSystemGroupData::getGroupName,name)
                        .eq(YqmSystemGroupData::getStatus,CommonEnum.SHOW_STATUS_1.getValue())
                        .orderByDesc(YqmSystemGroupData::getSort));

        List<JSONObject> list = systemGroupDatas
                .stream()
                .map(YqmSystemGroupData::getValue)
                .map(JSONObject::parseObject)
                .collect(Collectors.toList());

        return list;
    }


    //===============管理后台==============

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmSystemGroupDataQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmSystemGroupData> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        List<YqmSystemGroupDataDto> systemGroupDataDTOS = new ArrayList<>();
        for (YqmSystemGroupData systemGroupData : page.getList()) {

            YqmSystemGroupDataDto systemGroupDataDTO = generator.convert(systemGroupData,YqmSystemGroupDataDto.class);
            systemGroupDataDTO.setMap(JSON.parseObject(systemGroupData.getValue()));
            systemGroupDataDTOS.add(systemGroupDataDTO);
        }
        map.put("content",systemGroupDataDTOS);
        map.put("totalElements",page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmSystemGroupData> queryAll(YqmSystemGroupDataQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmSystemGroupData.class, criteria));
    }


    @Override
    public void download(List<YqmSystemGroupDataDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmSystemGroupDataDto yqmSystemGroupData : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("对应的数据名称", yqmSystemGroupData.getGroupName());
            map.put("数据组对应的数据值（json数据）", yqmSystemGroupData.getValue());
            map.put("添加数据时间", yqmSystemGroupData.getAddTime());
            map.put("数据排序", yqmSystemGroupData.getSort());
            map.put("状态（1：开启；2：关闭；）", yqmSystemGroupData.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
