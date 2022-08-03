/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.product.vo.YqmSystemStoreQueryVo;
import com.yqm.modules.shop.domain.YqmSystemStore;
import com.yqm.modules.shop.service.YqmSystemStoreService;
import com.yqm.modules.shop.service.dto.YqmSystemStoreDto;
import com.yqm.modules.shop.service.dto.YqmSystemStoreQueryCriteria;
import com.yqm.modules.shop.service.mapper.SystemStoreMapper;
import com.yqm.utils.FileUtil;
import com.yqm.utils.RedisUtil;
import com.yqm.utils.ShopKeyUtils;
import com.yqm.utils.location.LocationUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class YqmSystemStoreServiceImpl extends BaseServiceImpl<SystemStoreMapper, YqmSystemStore> implements YqmSystemStoreService {

    private final IGenerator generator;
    private final SystemStoreMapper systemStoreMapper;

    @Override
    public YqmSystemStoreQueryVo getYqmSystemStoreById(int id){
        return generator.convert(this.getById(id),YqmSystemStoreQueryVo.class);
    }

    /**
     * 获取门店列表
     * @param latitude 纬度
     * @param longitude 经度
     * @param page page
     * @param limit limit
     * @return List
     */
    @Override
    public List<YqmSystemStoreQueryVo> getStoreList(String latitude, String longitude, int page, int limit) {
        Page<YqmSystemStore> pageModel = new Page<>(page, limit);
        if(StrUtil.isBlank(latitude) || StrUtil.isBlank(longitude)){
            return generator.convert(this.page(pageModel).getRecords(),YqmSystemStoreQueryVo.class);
        }
        List<YqmSystemStoreQueryVo> list = systemStoreMapper.getStoreList(pageModel,Double.valueOf(longitude),Double.valueOf(latitude));
        list.forEach(item->{
            String newDis = NumberUtil.round(Double.valueOf(item.getDistance()) / 1000,2).toString();
            item.setDistance(newDis);
        });
        return list;
    }

    /**
     * 获取最新单个门店
     * @param latitude 纬度
     * @param longitude 经度
     * @return YqmSystemStoreQueryVo
     */
    @Override
    public YqmSystemStoreQueryVo getStoreInfo(String latitude,String longitude) {
        YqmSystemStore yqmSystemStore = systemStoreMapper.selectOne(
                Wrappers.<YqmSystemStore>lambdaQuery()
                        .eq(YqmSystemStore::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                        .orderByDesc(YqmSystemStore::getId)
                        .last("limit 1"));
        if(yqmSystemStore == null) {
            return null;
        }
        String mention = RedisUtil.get(ShopKeyUtils.getStoreSelfMention());
        if(StrUtil.isBlank(mention) || ShopCommonEnum.ENABLE_2.getValue().toString().equals(mention)) {
            return null;
        }
        YqmSystemStoreQueryVo systemStoreQueryVo = generator.convert(yqmSystemStore,YqmSystemStoreQueryVo.class);
        if(StrUtil.isNotEmpty(latitude) && StrUtil.isNotEmpty(longitude)){
            double distance = LocationUtils.getDistance(Double.valueOf(latitude),Double.valueOf(longitude),
                    Double.valueOf(yqmSystemStore.getLatitude()),Double.valueOf(yqmSystemStore.getLongitude()));
            systemStoreQueryVo.setDistance(String.valueOf(distance));
        }
        return systemStoreQueryVo;
    }



    //===================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmSystemStoreQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmSystemStore> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmSystemStoreDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmSystemStore> queryAll(YqmSystemStoreQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmSystemStore.class, criteria));
    }


    @Override
    public void download(List<YqmSystemStoreDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmSystemStoreDto yqmSystemStore : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("门店名称", yqmSystemStore.getName());
            map.put("简介", yqmSystemStore.getIntroduction());
            map.put("手机号码", yqmSystemStore.getPhone());
            map.put("省市区", yqmSystemStore.getAddress());
            map.put("详细地址", yqmSystemStore.getDetailedAddress());
            map.put("门店logo", yqmSystemStore.getImage());
            map.put("纬度", yqmSystemStore.getLatitude());
            map.put("经度", yqmSystemStore.getLongitude());
            map.put("核销有效日期", yqmSystemStore.getValidTime());
            map.put("每日营业开关时间", yqmSystemStore.getDayTime());
            map.put("是否显示", yqmSystemStore.getIsShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
