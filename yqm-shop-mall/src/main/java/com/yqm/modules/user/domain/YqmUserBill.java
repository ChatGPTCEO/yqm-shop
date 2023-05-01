/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.user.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yqm.domain.BaseDomain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
* @author weiximei
* @date 2020-05-12
*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("yqm_user_bill")
public class YqmUserBill extends BaseDomain {

    /** 用户账单id */
    @TableId
    private Long id;


    /** 用户uid */
    private Long uid;


    /** 关联id */
    private String linkId;


    /** 0 = 支出 1 = 获得 */
    private Integer pm;


    /** 账单标题 */
    private String title;


    /** 明细种类 */
    private String category;


    /** 明细类型 */
    private String type;


    /** 明细数字 */
    private BigDecimal number;


    /** 剩余 */
    private BigDecimal balance;


    /** 备注 */
    private String mark;



    /** 0 = 带确定 1 = 有效 -1 = 无效 */
    private Integer status;


    public void copy(YqmUserBill source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
