/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.CouponEnum;
import com.yqm.modules.activity.domain.YqmStoreCouponIssue;
import com.yqm.modules.activity.domain.YqmStoreCouponIssueUser;
import com.yqm.modules.activity.service.YqmStoreCouponIssueService;
import com.yqm.modules.activity.service.YqmStoreCouponIssueUserService;
import com.yqm.modules.activity.service.YqmStoreCouponUserService;
import com.yqm.modules.activity.service.dto.YqmStoreCouponIssueDto;
import com.yqm.modules.activity.service.dto.YqmStoreCouponIssueQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreCouponIssueMapper;
import com.yqm.modules.activity.vo.YqmStoreCouponIssueQueryVo;
import com.yqm.utils.FileUtil;
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
* @date 2020-05-13
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreCouponIssueServiceImpl extends BaseServiceImpl<YqmStoreCouponIssueMapper, YqmStoreCouponIssue> implements YqmStoreCouponIssueService {

    private final IGenerator generator;

    private final YqmStoreCouponIssueMapper yqmStoreCouponIssueMapper;


    private final YqmStoreCouponUserService storeCouponUserService;
    private final YqmStoreCouponIssueUserService storeCouponIssueUserService;


    /**
     * 领取优惠券
     * @param id id 优惠券id
     * @param uid uid
     */
    @Override
    public void issueUserCoupon(Integer id, Long uid) {
        YqmStoreCouponIssueQueryVo couponIssueQueryVo = yqmStoreCouponIssueMapper
                .selectOne(id);
        if(ObjectUtil.isNull(couponIssueQueryVo)) {
            throw new YqmShopException("领取的优惠劵已领完或已过期");
        }

        int count = this.couponCount(id,uid);
        if(count > 0) {
            throw new YqmShopException("已领取过该优惠劵");
        }

        if(couponIssueQueryVo.getRemainCount() <= 0
                && CouponEnum.PERMANENT_0.getValue().equals(couponIssueQueryVo.getIsPermanent())){
            throw new YqmShopException("抱歉优惠卷已经领取完了");
        }

        storeCouponUserService.addUserCoupon(uid,couponIssueQueryVo.getCid());

        storeCouponIssueUserService.addUserIssue(uid,id);

        if(couponIssueQueryVo.getTotalCount() > 0){
            yqmStoreCouponIssueMapper.decCount(id);
        }

    }


    /**
     * 优惠券列表
     * @param page page
     * @param limit limit
     * @param uid  用户id
     * @return list
     */
    @Override
    public List<YqmStoreCouponIssueQueryVo> getCouponList(int page, int limit, Long uid,Long productId,Integer type) {
        Page<YqmStoreCouponIssue> pageModel = new Page<>(page, limit);

        if(type == null) {
            type = CouponEnum.TYPE_0.getValue();
        }
        List<YqmStoreCouponIssueQueryVo> list = yqmStoreCouponIssueMapper
                .selecCoupontList(pageModel,type,productId);
        for (YqmStoreCouponIssueQueryVo couponIssue : list) {
            int count = this.couponCount(couponIssue.getId(),uid);
            if(count > 0){
                couponIssue.setIsUse(true);
            }else{
                couponIssue.setIsUse(false);
            }

        }
        return list;
    }


    /**
     * 获取用户领取优惠券数量
     * @param id 前台优惠券id
     * @param uid 用户id
     * @return int
     */
    private int couponCount(Integer id, Long uid) {
        return storeCouponIssueUserService.lambdaQuery()
                .eq(YqmStoreCouponIssueUser::getUid,uid)
                .eq(YqmStoreCouponIssueUser::getIssueCouponId,id)
                .count();
    }

   //============================================================//


    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreCouponIssueQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCouponIssue> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmStoreCouponIssueDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreCouponIssue> queryAll(YqmStoreCouponIssueQueryCriteria criteria){
        return this.list(QueryHelpPlus.getPredicate(YqmStoreCouponIssue.class, criteria));
    }


    @Override
    public void download(List<YqmStoreCouponIssueDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCouponIssueDto yqmStoreCouponIssue : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" cname",  yqmStoreCouponIssue.getCname());
            map.put("优惠券ID", yqmStoreCouponIssue.getCid());
            map.put("优惠券领取开启时间", yqmStoreCouponIssue.getStartTime());
            map.put("优惠券领取结束时间", yqmStoreCouponIssue.getEndTime());
            map.put("优惠券领取数量", yqmStoreCouponIssue.getTotalCount());
            map.put("优惠券剩余领取数量", yqmStoreCouponIssue.getRemainCount());
            map.put("是否无限张数", yqmStoreCouponIssue.getIsPermanent());
            map.put("1 正常 0 未开启 -1 已无效", yqmStoreCouponIssue.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
