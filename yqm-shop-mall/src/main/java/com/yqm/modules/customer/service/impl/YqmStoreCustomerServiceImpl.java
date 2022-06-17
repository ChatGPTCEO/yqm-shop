/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.customer.service.impl;

import com.yqm.modules.customer.domain.YqmStoreCustomer;
import com.yqm.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import com.yqm.dozer.service.IGenerator;
import com.github.pagehelper.PageInfo;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.utils.FileUtil;
import com.yqm.modules.customer.service.YqmStoreCustomerService;
import com.yqm.modules.customer.service.dto.YqmStoreCustomerDto;
import com.yqm.modules.customer.service.dto.YqmStoreCustomerQueryCriteria;
import com.yqm.modules.customer.service.mapper.YqmStoreCustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.yqm.domain.PageResult;
/**
* @author Bug
* @date 2020-12-10
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yqmStoreCustomer")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreCustomerServiceImpl extends BaseServiceImpl<YqmStoreCustomerMapper, YqmStoreCustomer> implements YqmStoreCustomerService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<YqmStoreCustomerDto> queryAll(YqmStoreCustomerQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCustomer> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,YqmStoreCustomerDto.class);
    }


    @Override
    //@Cacheable
    public List<YqmStoreCustomer> queryAll(YqmStoreCustomerQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreCustomer.class, criteria));
    }


    @Override
    public void download(List<YqmStoreCustomerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCustomerDto yqmStoreCustomer : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户昵称", yqmStoreCustomer.getNickName());
            map.put("openId", yqmStoreCustomer.getOpenId());
            map.put("备注", yqmStoreCustomer.getRemark());
            map.put("添加时间", yqmStoreCustomer.getCreateTime());
            map.put("修改时间", yqmStoreCustomer.getUpdateTime());
            map.put(" isDel",  yqmStoreCustomer.getIsDel());
            map.put("是否启用", yqmStoreCustomer.getIsEnable());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
