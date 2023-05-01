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
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.shop.domain.YqmSystemStoreStaff;
import com.yqm.modules.shop.service.YqmSystemStoreStaffService;
import com.yqm.modules.shop.service.dto.YqmSystemStoreStaffDto;
import com.yqm.modules.shop.service.dto.YqmSystemStoreStaffQueryCriteria;
import com.yqm.modules.shop.service.mapper.SystemStoreStaffMapper;
import com.yqm.utils.FileUtil;
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



/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmSystemStoreStaffServiceImpl extends BaseServiceImpl<SystemStoreStaffMapper, YqmSystemStoreStaff> implements YqmSystemStoreStaffService {

    private final IGenerator generator;


    /**
     * 接测店员客服状态
     * @param uid 用户id
     * @param storeId 门店id
     * @return boolean true=可核销
     */
    @Override
    public boolean checkStatus(Long uid,Integer storeId) {
        YqmSystemStoreStaff storeStaff = new YqmSystemStoreStaff();
        storeStaff.setUid(uid);
        storeStaff.setVerifyStatus(ShopCommonEnum.IS_STATUS_1.getValue());
        if(storeId != null) {
            storeStaff.setStoreId(storeId);
        }
        return this.baseMapper.selectCount(Wrappers.query(storeStaff)) > 0;
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmSystemStoreStaffQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmSystemStoreStaff> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmSystemStoreStaffDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmSystemStoreStaff> queryAll(YqmSystemStoreStaffQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmSystemStoreStaff.class, criteria));
    }


    @Override
    public void download(List<YqmSystemStoreStaffDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmSystemStoreStaffDto yqmSystemStoreStaff : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("微信用户id", yqmSystemStoreStaff.getUid());
            map.put("店员头像", yqmSystemStoreStaff.getAvatar());
            map.put("门店id", yqmSystemStoreStaff.getStoreId());
            map.put("店员名称", yqmSystemStoreStaff.getStaffName());
            map.put("手机号码", yqmSystemStoreStaff.getPhone());
            map.put("核销开关", yqmSystemStoreStaff.getVerifyStatus());
            map.put("状态", yqmSystemStoreStaff.getStatus());
            map.put("微信昵称", yqmSystemStoreStaff.getNickname());
            map.put("所属门店", yqmSystemStoreStaff.getStoreName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
