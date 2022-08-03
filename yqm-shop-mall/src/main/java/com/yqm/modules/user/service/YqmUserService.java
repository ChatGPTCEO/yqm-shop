/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.order.vo.YqmStoreOrderQueryVo;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.dto.PromUserDto;
import com.yqm.modules.user.service.dto.UserMoneyDto;
import com.yqm.modules.user.service.dto.YqmUserDto;
import com.yqm.modules.user.service.dto.YqmUserQueryCriteria;
import com.yqm.modules.user.vo.YqmUserQueryVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmUserService  extends BaseService<YqmUser>{

    /**
     * 返回用户累计充值金额与消费金额
     * @param uid uid
     * @return Double[]
     */
    Double[] getUserMoney(Long uid);

    /**
     * 一级返佣
     * @param order 订单
     */
    void backOrderBrokerage(YqmStoreOrderQueryVo order);



    /**
     * 统计分销人员
     * @param uid uid
     * @return map
     */
    Map<String,Integer> getSpreadCount(Long uid);

    /**
     * 获取我的分销下人员列表
     * @param uid uid
     * @param page page
     * @param limit limit
     * @param grade ShopCommonEnum.GRADE_0
     * @param keyword 关键字搜索
     * @param sort 排序
     * @return list
     */
    List<PromUserDto> getUserSpreadGrade(Long uid, int page, int limit, Integer grade, String keyword, String sort);

    /**
     * 减去用户积分
     * @param uid 用户id
     * @param integral 积分
     */
    void decIntegral(Long uid,double integral);

    /**
     * 增加购买次数
     * @param uid uid
     */
    void incPayCount(Long uid);

    /**
     * 减去用户余额
     * @param uid uid
     * @param payPrice 金额
     */
    void decPrice(Long uid, BigDecimal payPrice);

   // YqmUser findByName(String name);

    /**
     * 更新用户余额
     * @param uid y用户id
     * @param price 金额
     */
    void incMoney(Long uid,BigDecimal price);

    /**
     * 增加积分
     * @param uid uid
     * @param integral 积分
     */
    void incIntegral(Long uid,double integral);

    /**
     * 获取用户信息
     * @param uid uid
     * @return YqmUserQueryVo
     */
    YqmUserQueryVo getYqmUserById(Long uid);

    /**
     * 获取用户个人详细信息
     * @param yqmUser yqmUser
     * @return YqmUserQueryVo
     */
    YqmUserQueryVo getNewYqmUserById(YqmUser yqmUser);

    /**
     * 转换用户信息
     * @param yqmUser user
     * @return YqmUserQueryVo
     */
    YqmUserQueryVo handleUser(YqmUser yqmUser);

    /**
     * 返回会员价
     * @param price 原价
     * @param uid 用户id
     * @return vip 价格
     */
    double setLevelPrice(double price, long uid);

    /**
     * 设置推广关系
     * @param spread 上级人
     * @param uid 本人
     */
    void setSpread(String spread, long uid);


    /**
     * 查看下级
     * @param uid uid
     * @param grade 等级
     * @return list
     */
    List<PromUserDto> querySpread(Long uid, Integer grade);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmUserQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmUserDto>
    */
    List<YqmUser> queryAll(YqmUserQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmUserDto> all, HttpServletResponse response) throws IOException;

    /**
     * 更新用户状态
     * @param uid uid
     * @param status ShopCommonEnum
     */
    void onStatus(Long uid, Integer status);

    /**
     * 修改余额
     * @param param UserMoneyDto
     */
    void updateMoney(UserMoneyDto param);

    /**
     * 增加佣金
     * @param price 金额
     * @param uid 用户id
     */
    void incBrokeragePrice(BigDecimal price, Long uid);


}
