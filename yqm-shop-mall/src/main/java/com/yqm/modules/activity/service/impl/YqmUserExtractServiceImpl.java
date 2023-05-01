/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.BillDetailEnum;
import com.yqm.enums.PayTypeEnum;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.event.TemplateBean;
import com.yqm.event.TemplateEvent;
import com.yqm.event.TemplateListenEnum;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.activity.domain.YqmUserExtract;
import com.yqm.modules.activity.param.UserExtParam;
import com.yqm.modules.activity.service.YqmUserExtractService;
import com.yqm.modules.activity.service.dto.YqmUserExtractDto;
import com.yqm.modules.activity.service.dto.YqmUserExtractQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmUserExtractMapper;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
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
* @date 2020-05-13
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmUserExtractServiceImpl extends BaseServiceImpl<YqmUserExtractMapper, YqmUserExtract> implements YqmUserExtractService {

    private final IGenerator generator;
    private final YqmUserExtractMapper yqmUserExtractMapper;
    private final YqmUserService userService;
    private final YqmUserBillService billService;
    private final ApplicationEventPublisher publisher;

    /**
     * 开始提现
     * @param userInfo 用户
     * @param param UserExtParam
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void userExtract(YqmUser userInfo, UserExtParam param) {
        BigDecimal extractPrice = userInfo.getBrokeragePrice();
        if(extractPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new YqmShopException("提现佣金不足");
        }

        double money = Double.valueOf(param.getMoney());
        if( extractPrice.compareTo(BigDecimal.valueOf(money)) < 0) {
            throw new YqmShopException("提现佣金不足");
        }

        if(money <= 0) {
            throw new YqmShopException("提现佣金大于0");
        }

        double balance = NumberUtil.sub(extractPrice.doubleValue(),money);
        if(balance < 0) {
            balance = 0;
        }

        YqmUserExtract userExtract = new YqmUserExtract();
        userExtract.setUid(userInfo.getUid());
        userExtract.setExtractType(param.getExtractType());
        userExtract.setExtractPrice(new BigDecimal(param.getMoney()));
        userExtract.setBalance(BigDecimal.valueOf(balance));

        if(StrUtil.isNotEmpty(param.getName())){
            userExtract.setRealName(param.getName());
        }else {
            userExtract.setRealName(userInfo.getNickname());
        }

        if(StrUtil.isNotEmpty(param.getWeixin())){
            userExtract.setWechat(param.getWeixin());
        }else {
            userExtract.setWechat(userInfo.getNickname());
        }

        String mark = "";

        if(PayTypeEnum.ALI.getValue().equals(param.getExtractType())){
            if(StrUtil.isEmpty(param.getAlipayCode())){
                throw new YqmShopException("请输入支付宝账号");
            }
            userExtract.setAlipayCode(param.getAlipayCode());
            mark = "使用支付宝提现"+param.getMoney()+"元";
        }else if(PayTypeEnum.WEIXIN.getValue().equals(param.getExtractType())){
            if(StrUtil.isEmpty(param.getWeixin())){
                throw new YqmShopException("请输入微信账号");
            }
            mark = "使用微信提现"+param.getMoney()+"元";
        }

        yqmUserExtractMapper.insert(userExtract);

        //更新佣金
        YqmUser yqmUser = new YqmUser();
        yqmUser.setBrokeragePrice(BigDecimal.valueOf(balance));
        yqmUser.setUid(userInfo.getUid());
        userService.updateById(yqmUser);

        //插入流水
        billService.expend(userInfo.getUid(),"佣金提现", BillDetailEnum.CATEGORY_1.getValue(),
                BillDetailEnum.TYPE_4.getValue(),money,balance, mark);

    }

    /**
     * 累计提现金额
     * @param uid uid
     * @return double
     */
    @Override
    public double extractSum(Long uid) {
        return yqmUserExtractMapper.sumPrice(uid);
    }


    //==============================================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmUserExtractQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmUserExtract> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmUserExtractDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmUserExtract> queryAll(YqmUserExtractQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmUserExtract.class, criteria));
    }


    @Override
    public void download(List<YqmUserExtractDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmUserExtractDto yqmUserExtract : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" uid",  yqmUserExtract.getUid());
            map.put("名称", yqmUserExtract.getRealName());
            map.put("bank = 银行卡 alipay = 支付宝wx=微信", yqmUserExtract.getExtractType());
            map.put("银行卡", yqmUserExtract.getBankCode());
            map.put("开户地址", yqmUserExtract.getBankAddress());
            map.put("支付宝账号", yqmUserExtract.getAlipayCode());
            map.put("提现金额", yqmUserExtract.getExtractPrice());
            map.put(" mark",  yqmUserExtract.getMark());
            map.put(" balance",  yqmUserExtract.getBalance());
            map.put("无效原因", yqmUserExtract.getFailMsg());
            map.put(" failTime",  yqmUserExtract.getFailTime());
            map.put("-1 未通过 0 审核中 1 已提现", yqmUserExtract.getStatus());
            map.put("微信号", yqmUserExtract.getWechat());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 操作提现
     * @param resources YqmUserExtract
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doExtract(YqmUserExtract resources){
        if(resources.getStatus() == null){
            throw new BadRequestException("请选择审核状态");
        }

        if(ShopCommonEnum.EXTRACT_0.getValue().equals(resources.getStatus())){
            throw new BadRequestException("请选择审核状态");
        }
        YqmUserExtract userExtract = this.getById(resources.getId());
        if(!ShopCommonEnum.EXTRACT_0.getValue().equals(userExtract.getStatus())){
            throw new BadRequestException("该申请已经处理过啦！");
        }
        if(ShopCommonEnum.EXTRACT_MINUS_1.getValue().equals(resources.getStatus())){
            if(StrUtil.isEmpty(resources.getFailMsg())){
                throw new BadRequestException("请填写失败原因");
            }
            //防止无限添加佣金
            if (ObjectUtil.isNull(userExtract.getFailTime())) {
                String mark = "提现失败,退回佣金"+resources.getExtractPrice()+"元";
                YqmUser yqmUser = userService.getById(resources.getUid());

                double balance = NumberUtil.add(yqmUser.getBrokeragePrice(),resources.getExtractPrice()).doubleValue();
                //插入流水
                billService.income(resources.getUid(),"提现失败", BillDetailEnum.CATEGORY_1.getValue(),
                        BillDetailEnum.TYPE_4.getValue(),resources.getExtractPrice().doubleValue(),balance,
                        mark,resources.getId().toString());

                //返回提现金额
                userService.incBrokeragePrice(resources.getExtractPrice(),resources.getUid());

                resources.setFailTime(new Date());
            }

        }else{
            //模板消息支付成功发布事件
            TemplateBean templateBean = TemplateBean.builder()
                    .extractId( resources.getId())
                    .templateType(TemplateListenEnum.TYPE_8.getValue())
                    .build();
            publisher.publishEvent(new TemplateEvent(this,templateBean));
        }
        this.saveOrUpdate(resources);
    }
}
