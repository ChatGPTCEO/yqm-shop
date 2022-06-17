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
import com.yqm.modules.activity.domain.YqmStoreCouponIssueUser;
import com.yqm.modules.activity.service.dto.YqmStoreCouponIssueUserDto;
import com.yqm.modules.activity.service.dto.YqmStoreCouponIssueUserQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-13
*/
public interface YqmStoreCouponIssueUserService  extends BaseService<YqmStoreCouponIssueUser>{

    /**
     * 添加优惠券领取记录
     * @param uid 用户id
     * @param id 前台优惠券id
     */
    void addUserIssue(Long uid, Integer id);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmStoreCouponIssueUserQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmStoreCouponIssueUserDto>
    */
    List<YqmStoreCouponIssueUser> queryAll(YqmStoreCouponIssueUserQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmStoreCouponIssueUserDto> all, HttpServletResponse response) throws IOException;
}
