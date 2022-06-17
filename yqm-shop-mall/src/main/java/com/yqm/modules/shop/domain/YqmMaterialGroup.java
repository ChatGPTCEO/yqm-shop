/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yqm.domain.BaseDomain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
* @author weiximei
* @date 2020-05-12
*/

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("yqm_material_group")
public class YqmMaterialGroup extends BaseDomain {

    /** PK */
    @TableId(type = IdType.UUID)
    private String id;


    /** 创建者ID */
    private String createId;


    /** 分组名 */
    private String name;


    public void copy(YqmMaterialGroup source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
