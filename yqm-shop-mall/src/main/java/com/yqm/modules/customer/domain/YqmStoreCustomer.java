/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.customer.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;

import com.yqm.domain.BaseDomain;

/**
* @author Bug
* @date 2020-12-10
*/
@Data
@TableName("yqm_store_customer")
public class YqmStoreCustomer extends BaseDomain {
    /** id */
    @TableId
    private Long id;

    /** 用户昵称 */
    private String nickName;

    /** openId */
    @NotBlank(message = "请用户扫码后提交")
    private String openId;

    /** 备注 */
    private String remark;




    /** 是否启用 */
    private Integer isEnable;


    public void copy(YqmStoreCustomer source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
