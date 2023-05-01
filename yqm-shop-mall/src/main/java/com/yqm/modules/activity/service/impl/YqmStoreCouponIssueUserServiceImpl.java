/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.activity.domain.YqmStoreCouponIssueUser;
import com.yqm.modules.activity.service.YqmStoreCouponIssueUserService;
import com.yqm.modules.activity.service.dto.YqmStoreCouponIssueUserDto;
import com.yqm.modules.activity.service.dto.YqmStoreCouponIssueUserQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreCouponIssueUserMapper;
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
* @date 2020-05-13
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreCouponIssueUserServiceImpl extends BaseServiceImpl<YqmStoreCouponIssueUserMapper, YqmStoreCouponIssueUser> implements YqmStoreCouponIssueUserService {

    private final IGenerator generator;

    /**
     * 添加优惠券领取记录
     * @param uid 用户id
     * @param id 前台优惠券id
     */
    @Override
    public void addUserIssue(Long uid, Integer id) {
        YqmStoreCouponIssueUser couponIssueUser = new YqmStoreCouponIssueUser();
        couponIssueUser.setIssueCouponId(id);
        couponIssueUser.setUid(uid);
        this.save(couponIssueUser);
    }


    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreCouponIssueUserQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCouponIssueUser> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmStoreCouponIssueUserDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreCouponIssueUser> queryAll(YqmStoreCouponIssueUserQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreCouponIssueUser.class, criteria));
    }


    @Override
    public void download(List<YqmStoreCouponIssueUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCouponIssueUserDto yqmStoreCouponIssueUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("领取优惠券用户ID", yqmStoreCouponIssueUser.getUid());
            map.put("优惠券前台领取ID", yqmStoreCouponIssueUser.getIssueCouponId());
            map.put("领取时间", yqmStoreCouponIssueUser.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
