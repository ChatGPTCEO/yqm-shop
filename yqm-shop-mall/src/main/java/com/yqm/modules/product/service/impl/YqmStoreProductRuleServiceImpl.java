/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.product.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.product.domain.YqmStoreProductRule;
import com.yqm.modules.product.service.YqmStoreProductRuleService;
import com.yqm.modules.product.service.dto.YqmStoreProductRuleQueryCriteria;
import com.yqm.modules.product.service.mapper.YqmStoreProductRuleMapper;
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
* @date 2020-06-28
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreProductRuleServiceImpl extends BaseServiceImpl<YqmStoreProductRuleMapper, YqmStoreProductRule> implements YqmStoreProductRuleService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreProductRuleQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreProductRule> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreProductRule> queryAll(YqmStoreProductRuleQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreProductRule.class, criteria));
    }


    @Override
    public void download(List<YqmStoreProductRule> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreProductRule yqmStoreProductRule : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("规格名称", yqmStoreProductRule.getRuleName());
            map.put("规格值", yqmStoreProductRule.getRuleValue());
            map.put(" createTime",  yqmStoreProductRule.getCreateTime());
            map.put(" updateTime",  yqmStoreProductRule.getUpdateTime());
            map.put(" isDel",  yqmStoreProductRule.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
