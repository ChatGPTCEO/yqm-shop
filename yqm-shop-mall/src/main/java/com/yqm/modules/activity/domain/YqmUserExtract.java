/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.activity.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yqm.domain.BaseDomain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
* @author weiximei
* @date 2020-05-13
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yqm_user_extract")
public class YqmUserExtract extends BaseDomain {

    @TableId
    private Long id;


    private Long uid;


    /** 名称 */
    private String realName;


    /** bank = 银行卡 alipay = 支付宝wx=微信 */
    private String extractType;


    /** 银行卡 */
    private String bankCode;


    /** 开户地址 */
    private String bankAddress;


    /** 支付宝账号 */
    private String alipayCode;


    /** 提现金额 */
    private BigDecimal extractPrice;


    private String mark;


    private BigDecimal balance;


    /** 无效原因 */
    private String failMsg;


    private Date failTime;


    /** -1 未通过 0 审核中 1 已提现 */
    private Integer status;


    /** 微信号 */
    private String wechat;


    public void copy(YqmUserExtract source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
