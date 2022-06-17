/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.activity.domain.YqmStoreCoupon;
import com.yqm.modules.activity.service.YqmStoreCouponService;
import com.yqm.modules.activity.service.dto.YqmStoreCouponDto;
import com.yqm.modules.activity.service.dto.YqmStoreCouponQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreCouponMapper;
import com.yqm.modules.product.domain.YqmStoreProduct;
import com.yqm.modules.product.service.YqmStoreProductService;
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
import java.util.Arrays;
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
public class YqmStoreCouponServiceImpl extends BaseServiceImpl<YqmStoreCouponMapper, YqmStoreCoupon> implements YqmStoreCouponService {

    private final IGenerator generator;
    private final YqmStoreProductService storeProductService;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreCouponQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCoupon> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        List<YqmStoreCouponDto> storeCouponDtos = generator.convert(page.getList(), YqmStoreCouponDto.class);
        for (YqmStoreCouponDto storeCouponDto : storeCouponDtos) {
            if(StrUtil.isNotBlank(storeCouponDto.getProductId())){
                List<YqmStoreProduct> storeProducts = storeProductService.lambdaQuery()
                        .in(YqmStoreProduct::getId, Arrays.asList(storeCouponDto.getProductId().split(",")))
                        .list();
                storeCouponDto.setProduct(storeProducts);
            }
        }
        map.put("content", storeCouponDtos);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreCoupon> queryAll(YqmStoreCouponQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreCoupon.class, criteria));
    }


    @Override
    public void download(List<YqmStoreCouponDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCouponDto yqmStoreCoupon : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("优惠券名称", yqmStoreCoupon.getTitle());
            map.put("兑换消耗积分值", yqmStoreCoupon.getIntegral());
            map.put("兑换的优惠券面值", yqmStoreCoupon.getCouponPrice());
            map.put("最低消费多少金额可用优惠券", yqmStoreCoupon.getUseMinPrice());
            map.put("优惠券有效期限（单位：天）", yqmStoreCoupon.getCouponTime());
            map.put("排序", yqmStoreCoupon.getSort());
            map.put("状态（0：关闭，1：开启）", yqmStoreCoupon.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
