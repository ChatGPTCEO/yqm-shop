/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ProductTypeEnum;
import com.yqm.modules.activity.domain.YqmStoreVisit;
import com.yqm.modules.activity.service.YqmStoreVisitService;
import com.yqm.modules.activity.service.dto.YqmStoreVisitDto;
import com.yqm.modules.activity.service.dto.YqmStoreVisitQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreVisitMapper;
import com.yqm.modules.product.domain.YqmStoreProduct;
import com.yqm.modules.product.service.YqmStoreProductService;
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



/**
* @author weiximei
* @date 2020-05-13
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreVisitServiceImpl extends BaseServiceImpl<YqmStoreVisitMapper, YqmStoreVisit> implements YqmStoreVisitService {

    private final IGenerator generator;
    private final YqmStoreProductService yqmStoreProductService;
    private final YqmStoreVisitMapper yqmStoreVisitMapper;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreVisitQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreVisit> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmStoreVisitDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreVisit> queryAll(YqmStoreVisitQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreVisit.class, criteria));
    }

    @Override
    public void download(List<YqmStoreVisitDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreVisitDto yqmStoreVisit : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("产品ID", yqmStoreVisit.getProductId());
            map.put("产品类型", yqmStoreVisit.getProductType());
            map.put("产品分类ID", yqmStoreVisit.getCateId());
            map.put("产品类型", yqmStoreVisit.getType());
            map.put("用户ID", yqmStoreVisit.getUid());
            map.put("访问次数", yqmStoreVisit.getCount());
            map.put("备注描述", yqmStoreVisit.getContent());
            map.put("添加时间", yqmStoreVisit.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 添加用户访问拼团记录
     * @param uid 用户id
     * @param productId 产品id
     */
    @Override
    public void addStoreVisit(Long uid, Long productId) {

        LambdaQueryWrapper<YqmStoreVisit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreVisit::getUid, uid).eq(YqmStoreVisit::getProductId, productId);
        YqmStoreVisit storeVisit = this.baseMapper.selectOne(wrapper);

        if (ObjectUtil.isNull(storeVisit)) {
            //查询产品分类
            YqmStoreProduct yqmStoreProduct = yqmStoreProductService.getProductInfo(productId);

            YqmStoreVisit yqmStoreVisit = YqmStoreVisit.builder()
                    .productId(productId)
                    .productType(ProductTypeEnum.COMBINATION.getValue())
                    .cateId(Integer.valueOf(yqmStoreProduct.getCateId()))
                    .type(ProductTypeEnum.COMBINATION.getValue())
                    .uid(uid)
                    .count(1)
                    .build();
            this.save(yqmStoreVisit);
        }

    }
}
