/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.order.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.order.domain.YqmStoreOrderStatus;
import com.yqm.modules.order.service.YqmStoreOrderStatusService;
import com.yqm.modules.order.service.dto.YqmStoreOrderStatusDto;
import com.yqm.modules.order.service.dto.YqmStoreOrderStatusQueryCriteria;
import com.yqm.modules.order.service.mapper.StoreOrderStatusMapper;
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
public class YqmStoreOrderStatusServiceImpl extends BaseServiceImpl<StoreOrderStatusMapper, YqmStoreOrderStatus> implements YqmStoreOrderStatusService {

    private final IGenerator generator;


    /**
     * 添加订单操作记录
     * @param oid 订单id
     * @param changetype 操作状态
     * @param changeMessage 操作内容
     */
    @Override
    public void create(Long oid, String changetype, String changeMessage) {
        YqmStoreOrderStatus storeOrderStatus = new YqmStoreOrderStatus();
        storeOrderStatus.setOid(oid);
        storeOrderStatus.setChangeType(changetype);
        storeOrderStatus.setChangeMessage(changeMessage);
        this.baseMapper.insert(storeOrderStatus);
    }



    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreOrderStatusQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreOrderStatus> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmStoreOrderStatusDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreOrderStatus> queryAll(YqmStoreOrderStatusQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreOrderStatus.class, criteria));
    }


    @Override
    public void download(List<YqmStoreOrderStatusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreOrderStatusDto yqmStoreOrderStatus : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单id", yqmStoreOrderStatus.getOid());
            map.put("操作类型", yqmStoreOrderStatus.getChangeType());
            map.put("操作备注", yqmStoreOrderStatus.getChangeMessage());
            map.put("操作时间", yqmStoreOrderStatus.getChangeTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
