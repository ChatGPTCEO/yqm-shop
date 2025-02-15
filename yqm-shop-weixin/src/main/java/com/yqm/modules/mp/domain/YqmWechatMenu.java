/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.mp.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
* @author weiximei
* @date 2020-05-12
*/
@Data
@TableName("yqm_wechat_menu")
public class YqmWechatMenu implements Serializable {

    @TableId(value = "`key`")
    private String key;


    /** 缓存数据 */
    private String result;


    /** 缓存时间 */
    @TableField(fill= FieldFill.INSERT)
    private Integer addTime;


    public void copy(YqmWechatMenu source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
