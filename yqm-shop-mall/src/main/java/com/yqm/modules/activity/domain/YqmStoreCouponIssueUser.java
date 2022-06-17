/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.activity.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yqm.domain.BaseDomain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author weiximei
* @date 2020-05-13
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yqm_store_coupon_issue_user")
public class YqmStoreCouponIssueUser extends BaseDomain {

    @TableId
    @ApiModelProperty(value = "优惠券前台用户领取记录ID")
    private Long id;


    /** 领取优惠券用户ID */
    @ApiModelProperty(value = "领取优惠券用户ID")
    private Long uid;


    /** 优惠券前台领取ID */
    @ApiModelProperty(value = "优惠券前台领取ID")
    private Integer issueCouponId;




    public void copy(YqmStoreCouponIssueUser source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
