/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yqm.domain.BaseDomain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
* @author weiximei
* @date 2020-05-12
*/

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yqm_system_user_level")
public class YqmSystemUserLevel extends BaseDomain {

    @TableId
    private Integer id;


    /** 商户id */
    private Integer merId;


    /** 会员名称 */
    @NotBlank(message = "名称必填")
    private String name;


    /** 购买金额 */
    private BigDecimal money;


    /** 有效时间 */
    private Integer validDate;


    /** 是否为永久会员 */
    private Integer isForever;


    /** 是否购买,1=购买,0=不购买 */
    private Integer isPay;


    /** 是否显示 1=显示,0=隐藏 */
    private Integer isShow;


    /** 会员等级 */
    @NotNull(message = "请输入会员等级")
    private Integer grade;


    /** 享受折扣 */
    @NotNull(message = "请输入会员折扣")
    private BigDecimal discount;

    /** 会员卡背景 */
    @NotBlank(message = "请上传会员背景")
    private String image;


    /** 会员图标 */
    @NotBlank(message = "请上传会员图标")
    private String icon;


    /** 说明 */
    @TableField(value = "`explain`")
    private String explain;



    public void copy(YqmSystemUserLevel source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
