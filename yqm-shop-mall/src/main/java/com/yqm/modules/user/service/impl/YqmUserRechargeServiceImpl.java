/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.BillDetailEnum;
import com.yqm.enums.OrderInfoEnum;
import com.yqm.enums.PayTypeEnum;
import com.yqm.event.TemplateBean;
import com.yqm.event.TemplateEvent;
import com.yqm.event.TemplateListenEnum;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.domain.YqmUserRecharge;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.YqmUserRechargeService;
import com.yqm.modules.user.service.dto.YqmUserRechargeDto;
import com.yqm.modules.user.service.dto.YqmUserRechargeQueryCriteria;
import com.yqm.modules.user.service.mapper.UserMapper;
import com.yqm.modules.user.service.mapper.UserRechargeMapper;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
* @author weiximei
* @date 2020-05-12
*/
@SuppressWarnings("unchecked")
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmUserRechargeServiceImpl extends BaseServiceImpl<UserRechargeMapper, YqmUserRecharge> implements YqmUserRechargeService {
    @Autowired
    private IGenerator generator;

    @Autowired
    private UserRechargeMapper yqmUserRechargeMapper;
    @Autowired
    private YqmUserBillService billService;

    @Autowired
    private UserMapper yqmUserMapper;

    @Autowired
    private ApplicationEventPublisher publisher;


    @Override
    public void updateRecharge(YqmUserRecharge userRecharge) {
        YqmUser user = yqmUserMapper.selectById(userRecharge.getUid());

        //修改状态
        userRecharge.setPaid(OrderInfoEnum.PAY_STATUS_1.getValue());
        userRecharge.setPayTime(new Date());
        yqmUserRechargeMapper.updateById(userRecharge);

        //最终充值金额
        BigDecimal newPrice = NumberUtil.add(userRecharge.getPrice(),user.getNowMoney());
        newPrice = newPrice.add(userRecharge.getGivePrice());


        //增加流水
        billService.income(userRecharge.getUid(),"用户余额充值",BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_1.getValue(),userRecharge.getPrice().doubleValue(),newPrice.doubleValue(),
                "成功充值余额"+userRecharge.getPrice(),userRecharge.getId().toString());


        //update 余额
        user.setNowMoney(newPrice);
        yqmUserMapper.updateById(user);

        //模板消息发布事件
        TemplateBean templateBean = TemplateBean.builder()
                .time(DateUtil.formatTime(userRecharge.getPayTime()))
                .price(userRecharge.getPrice().toString())
                .orderId(userRecharge.getOrderId())
                .uid(userRecharge.getUid())
                .templateType(TemplateListenEnum.TYPE_4.getValue())
                .build();
        publisher.publishEvent(new TemplateEvent(this, templateBean));

    }

    @Override
    public YqmUserRecharge getInfoByOrderId(String orderId) {
        YqmUserRecharge userRecharge = new YqmUserRecharge();
        userRecharge.setOrderId(orderId);

        return yqmUserRechargeMapper.selectOne(Wrappers.query(userRecharge));
    }

    /**
     * 添加充值记录
     * @param user 用户
     * @param price 充值金额
     * @param paidPrice 赠送金额
     */
    @Override
    public String addRecharge(YqmUser user,String price,String paidPrice) {
        if(StrUtil.isBlank(price) || StrUtil.isBlank(paidPrice)){
            throw new YqmShopException("参数非法");
        }
        YqmUserRecharge yqmUserRecharge = new YqmUserRecharge();

        String orderSn = IdUtil.getSnowflake(0,0).nextIdStr();

        yqmUserRecharge.setNickname(user.getNickname());
        yqmUserRecharge.setOrderId(orderSn);
        yqmUserRecharge.setUid(user.getUid());
        yqmUserRecharge.setPrice(new BigDecimal(price));
        yqmUserRecharge.setGivePrice(new BigDecimal(paidPrice));
        yqmUserRecharge.setRechargeType(PayTypeEnum.WEIXIN.getValue());
        yqmUserRecharge.setPaid(OrderInfoEnum.PAY_STATUS_0.getValue());

        yqmUserRechargeMapper.insert(yqmUserRecharge);

        return orderSn;

    }



    //==========================================================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmUserRechargeQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmUserRecharge> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmUserRechargeDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmUserRecharge> queryAll(YqmUserRechargeQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmUserRecharge.class, criteria));
    }


    @Override
    public void download(List<YqmUserRechargeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmUserRechargeDto yqmUserRecharge : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("充值用户UID", yqmUserRecharge.getUid());
            map.put("订单号", yqmUserRecharge.getOrderId());
            map.put("充值金额", yqmUserRecharge.getPrice());
            map.put("充值类型", yqmUserRecharge.getRechargeType());
            map.put("是否充值", yqmUserRecharge.getPaid());
            map.put("充值支付时间", yqmUserRecharge.getPayTime());
            map.put("退款金额", yqmUserRecharge.getRefundPrice());
            map.put("昵称", yqmUserRecharge.getNickname());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
