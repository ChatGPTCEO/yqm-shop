/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.activity.domain.YqmStoreCouponUser;
import com.yqm.modules.activity.service.dto.YqmStoreCouponUserDto;
import com.yqm.modules.activity.service.dto.YqmStoreCouponUserQueryCriteria;
import com.yqm.modules.activity.vo.StoreCouponUserVo;
import com.yqm.modules.activity.vo.YqmStoreCouponUserQueryVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author weiximei
 * @date 2020-05-13
 */
public interface YqmStoreCouponUserService extends BaseService<YqmStoreCouponUser> {

    /**
     * 获取当前用户优惠券数量
     *
     * @param uid uid
     * @return int
     */
    int getUserValidCouponCount(Long uid);

    void useCoupon(int id);

    /**
     * 获取用户优惠券
     *
     * @param id  优惠券id
     * @param uid 用户id
     * @return YqmStoreCouponUser
     */
    YqmStoreCouponUser getCoupon(Integer id, Long uid);


    /**
     * 获取满足条件的可用优惠券
     *
     * @param cartIds 购物车ids
     * @return list
     */
    List<StoreCouponUserVo> beUsableCouponList(Long uid, String cartIds);

    /**
     * 获取下单时候满足的优惠券
     *
     * @param uid        uid
     * @param price      总价格
     * @param productIds list
     * @return list
     */
    List<StoreCouponUserVo> getUsableCouponList(Long uid, double price, List<String> productIds);


    /**
     * 获取用户优惠券
     *
     * @param uid uid
     * @return list
     */
    List<YqmStoreCouponUserQueryVo> getUserCoupon(Long uid);

    /**
     * 添加优惠券记录
     *
     * @param uid 用户id
     * @param cid 优惠券id
     */
    void addUserCoupon(Long uid, Integer cid);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(YqmStoreCouponUserQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<YqmStoreCouponUserDto>
     */
    List<YqmStoreCouponUser> queryAll(YqmStoreCouponUserQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<YqmStoreCouponUserDto> all, HttpServletResponse response) throws IOException;

    /**
     * pc端查询优惠券
     *
     * @param uid   用户id
     * @param page  当前页码
     * @param limit 一页多少
     * @param type
     * @return /
     */
    Map<String, Object> getUserPCCoupon(Long uid, int page, int limit, Integer type);
}
