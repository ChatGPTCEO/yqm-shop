/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.order.service.impl;

import cn.hutool.core.util.IdUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.order.domain.YqmStoreOrderCartInfo;
import com.yqm.modules.order.service.YqmStoreOrderCartInfoService;
import com.yqm.modules.order.service.dto.YqmStoreOrderCartInfoDto;
import com.yqm.modules.order.service.dto.YqmStoreOrderCartInfoQueryCriteria;
import com.yqm.modules.order.service.mapper.StoreOrderCartInfoMapper;
import com.yqm.utils.FileUtil;
import com.alibaba.fastjson.JSONObject;
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
public class YqmStoreOrderCartInfoServiceImpl extends BaseServiceImpl<StoreOrderCartInfoMapper, YqmStoreOrderCartInfo> implements YqmStoreOrderCartInfoService {

    private final IGenerator generator;

    @Override
    public YqmStoreOrderCartInfo findByUni(String unique) {
       LambdaQueryWrapper<YqmStoreOrderCartInfo> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreOrderCartInfo::getUnique,unique);
        return this.baseMapper.selectOne(wrapper);
    }

    /**
     * 添加购物车商品信息
     * @param oid 订单id
     * @param orderId
     * @param cartInfo 购物车信息
     */
    @Override
    public void saveCartInfo(Long oid, String orderId, List<YqmStoreCartQueryVo> cartInfo) {

        List<YqmStoreOrderCartInfo> list = new ArrayList<>();
        for (YqmStoreCartQueryVo cart : cartInfo) {
            YqmStoreOrderCartInfo info = new YqmStoreOrderCartInfo();
            info.setOid(oid);
            info.setOrderId(orderId);
            info.setCartId(cart.getId());
            info.setProductId(cart.getProductId());
            info.setCartInfo(JSONObject.toJSON(cart).toString());
            info.setUnique(IdUtil.simpleUUID());
            info.setIsAfterSales(1);
            list.add(info);
        }

        this.saveBatch(list);
    }


    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreOrderCartInfoQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreOrderCartInfo> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmStoreOrderCartInfoDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreOrderCartInfo> queryAll(YqmStoreOrderCartInfoQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreOrderCartInfo.class, criteria));
    }


    @Override
    public void download(List<YqmStoreOrderCartInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreOrderCartInfoDto yqmStoreOrderCartInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单id", yqmStoreOrderCartInfo.getOid());
            map.put("购物车id", yqmStoreOrderCartInfo.getCartId());
            map.put("商品ID", yqmStoreOrderCartInfo.getProductId());
            map.put("购买东西的详细信息", yqmStoreOrderCartInfo.getCartInfo());
            map.put("唯一id", yqmStoreOrderCartInfo.getUnique());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
