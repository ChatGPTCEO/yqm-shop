/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.template.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.yqm.api.BusinessException;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.template.domain.YqmShippingTemplates;
import com.yqm.modules.template.domain.YqmShippingTemplatesFree;
import com.yqm.modules.template.domain.YqmShippingTemplatesRegion;
import com.yqm.modules.template.service.YqmShippingTemplatesFreeService;
import com.yqm.modules.template.service.YqmShippingTemplatesRegionService;
import com.yqm.modules.template.service.YqmShippingTemplatesService;
import com.yqm.modules.template.service.dto.AppointInfoDto;
import com.yqm.modules.template.service.dto.RegionChildrenDto;
import com.yqm.modules.template.service.dto.RegionDto;
import com.yqm.modules.template.service.dto.RegionInfoDto;
import com.yqm.modules.template.service.dto.ShippingTemplatesDto;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesDto;
import com.yqm.modules.template.service.dto.YqmShippingTemplatesQueryCriteria;
import com.yqm.modules.template.service.mapper.YqmShippingTemplatesMapper;
import com.yqm.utils.FileUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
public class YqmShippingTemplatesServiceImpl extends BaseServiceImpl<YqmShippingTemplatesMapper, YqmShippingTemplates> implements YqmShippingTemplatesService {

    private final IGenerator generator;


    private final YqmShippingTemplatesRegionService yqmShippingTemplatesRegionService;
    private final YqmShippingTemplatesFreeService yqmShippingTemplatesFreeService;


    /**
     * 新增与更新模板
     * @param id 模板id
     * @param shippingTemplatesDto ShippingTemplatesDto
     */
    @Override
    public void addAndUpdate(Integer id,ShippingTemplatesDto shippingTemplatesDto) {
        if(ShopCommonEnum.ENABLE_1.getValue().equals(shippingTemplatesDto.getAppoint())
                && shippingTemplatesDto.getAppointInfo().isEmpty()){
            throw new YqmShopException("请指定包邮地区");
        }
        YqmShippingTemplates shippingTemplates = new YqmShippingTemplates();
        BeanUtil.copyProperties(shippingTemplatesDto,shippingTemplates);
        shippingTemplates.setRegionInfo(JSON.toJSONString(shippingTemplatesDto.getRegionInfo()));
        shippingTemplates.setAppointInfo(JSON.toJSONString(shippingTemplatesDto.getAppointInfo()));
        if(id != null && id > 0){
            shippingTemplates.setId(id);
            this.updateById(shippingTemplates);
        }else{
            this.save(shippingTemplates);
        }

        this.saveRegion(shippingTemplatesDto,shippingTemplates.getId());
        this.saveFreeReigion(shippingTemplatesDto,shippingTemplates.getId());
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmShippingTemplatesQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmShippingTemplates> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmShippingTemplatesDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmShippingTemplates> queryAll(YqmShippingTemplatesQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmShippingTemplates.class, criteria));
    }


    @Override
    public void download(List<YqmShippingTemplatesDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmShippingTemplatesDto yqmShippingTemplates : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("模板名称", yqmShippingTemplates.getName());
            map.put("计费方式", yqmShippingTemplates.getType());
            map.put("地域以及费用", yqmShippingTemplates.getRegionInfo());
            map.put("指定包邮开关", yqmShippingTemplates.getAppoint());
            map.put("指定包邮内容", yqmShippingTemplates.getAppointInfo());
            map.put("添加时间", yqmShippingTemplates.getCreateTime());
            map.put(" updateTime",  yqmShippingTemplates.getUpdateTime());
            map.put(" isDel",  yqmShippingTemplates.getIsDel());
            map.put("排序", yqmShippingTemplates.getSort());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 保存包邮区域
     * @param yqmShippingTemplates ShippingTemplatesDto
     * @param tempId 模板id
     */
    private void saveFreeReigion(ShippingTemplatesDto yqmShippingTemplates,Integer tempId){

        if(yqmShippingTemplates.getAppointInfo() == null
                || yqmShippingTemplates.getAppointInfo().isEmpty()){
            return;
        }

        int count = yqmShippingTemplatesFreeService.count(Wrappers
                .<YqmShippingTemplatesFree>lambdaQuery()
                .eq(YqmShippingTemplatesFree::getTempId,tempId));
        if(count > 0) {
            yqmShippingTemplatesFreeService.remove(Wrappers
                    .<YqmShippingTemplatesFree>lambdaQuery()
                    .eq(YqmShippingTemplatesFree::getTempId,tempId));
        }

        List<YqmShippingTemplatesFree> shippingTemplatesFrees = new ArrayList<>();


        List<AppointInfoDto> appointInfo = yqmShippingTemplates.getAppointInfo();
        for (AppointInfoDto appointInfoDto : appointInfo){
            String uni = IdUtil.simpleUUID();

            if(appointInfoDto.getPlace() != null && !appointInfoDto.getPlace().isEmpty()){
                for (RegionDto regionDto : appointInfoDto.getPlace()){
                    if(regionDto.getChildren() != null && !regionDto.getChildren().isEmpty()){
                        for (RegionChildrenDto childrenDto : regionDto.getChildren()){
                            YqmShippingTemplatesFree shippingTemplatesFree = YqmShippingTemplatesFree.builder()
                                    .tempId(tempId)
                                    .number(new BigDecimal(appointInfoDto.getA_num()))
                                    .price(new BigDecimal(appointInfoDto.getA_price()))
                                    .type(yqmShippingTemplates.getType())
                                    .uniqid(uni)
                                    .provinceId(Integer.valueOf(regionDto.getCity_id()))
                                    .cityId(Integer.valueOf(childrenDto.getCity_id()))
                                    .build();
                            shippingTemplatesFrees.add(shippingTemplatesFree);
                        }
                    }
                }
            }
        }


        if(shippingTemplatesFrees.isEmpty()) {
            throw new YqmShopException("请添加包邮区域");
        }

        yqmShippingTemplatesFreeService.saveBatch(shippingTemplatesFrees);


    }

    /**
     * 保存模板设置的区域价格
     * @param yqmShippingTemplates ShippingTemplatesDTO
     * @param tempId 运费模板id
     */
    private void saveRegion(ShippingTemplatesDto yqmShippingTemplates,Integer tempId){
        int count = yqmShippingTemplatesRegionService.count(Wrappers
                .<YqmShippingTemplatesRegion>lambdaQuery()
                .eq(YqmShippingTemplatesRegion::getTempId,tempId));
        if(count > 0) {
            yqmShippingTemplatesRegionService.remove(Wrappers
                    .<YqmShippingTemplatesRegion>lambdaQuery()
                    .eq(YqmShippingTemplatesRegion::getTempId,tempId));
        }

        List<YqmShippingTemplatesRegion> shippingTemplatesRegions = new ArrayList<>();


        List<RegionInfoDto> regionInfo = yqmShippingTemplates.getRegionInfo();


        for (RegionInfoDto regionInfoDto : regionInfo){
            String uni = IdUtil.simpleUUID();
            if(regionInfoDto.getRegion() != null && !regionInfoDto.getRegion().isEmpty()){
                for (RegionDto regionDto : regionInfoDto.getRegion()){
                    if(regionDto.getChildren() != null && !regionDto.getChildren().isEmpty()){
                        for (RegionChildrenDto childrenDtp : regionDto.getChildren()){
                            YqmShippingTemplatesRegion shippingTemplatesRegion = YqmShippingTemplatesRegion.builder()
                                    .tempId(tempId)
                                    .first(new BigDecimal(regionInfoDto.getFirst()))
                                    .firstPrice(new BigDecimal(regionInfoDto.getPrice()))
                                    .continues(new BigDecimal(regionInfoDto.get_continue()))
                                    .continuePrice(new BigDecimal(regionInfoDto.getContinue_price()))
                                    .type(yqmShippingTemplates.getType())
                                    .uniqid(uni)
                                    .provinceId(Integer.valueOf(regionDto.getCity_id()))
                                    .cityId(Integer.valueOf(childrenDtp.getCity_id()))
                                    .build();
                            shippingTemplatesRegions.add(shippingTemplatesRegion);
                        }
                    }else{
                        YqmShippingTemplatesRegion shippingTemplatesRegion = YqmShippingTemplatesRegion.builder()
                                .tempId(tempId)
                                .first(new BigDecimal(regionInfoDto.getFirst()))
                                .firstPrice(new BigDecimal(regionInfoDto.getPrice()))
                                .continues(new BigDecimal(regionInfoDto.get_continue()))
                                .continuePrice(new BigDecimal(regionInfoDto.getContinue_price()))
                                .type(yqmShippingTemplates.getType())
                                .uniqid(uni)
                                .provinceId(Integer.valueOf(regionDto.getCity_id()))
                                .build();
                        shippingTemplatesRegions.add(shippingTemplatesRegion);
                    }
                }
            }
        }

        if(shippingTemplatesRegions.isEmpty()) {
            throw new BusinessException("请添加区域");
        }

        yqmShippingTemplatesRegionService.saveBatch(shippingTemplatesRegions);

    }


}
