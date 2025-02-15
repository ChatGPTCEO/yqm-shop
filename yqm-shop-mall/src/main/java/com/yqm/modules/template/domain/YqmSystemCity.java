/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.template.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
* @author weiximei
* @date 2020-06-29
*/
@Data
@TableName("yqm_system_city")
public class YqmSystemCity implements Serializable {

    @TableId
    private Integer id;


    /** 城市id */
    @NotNull
    @JsonProperty(value = "city_id")
    private Integer cityId;


    /** 省市级别 */
    @NotNull
    private Integer level;


    /** 父级id */
    @NotNull
    private Integer parentId;


    /** 区号 */
    @NotBlank
    private String areaCode;


    /** 名称 */
    @NotBlank
    private String name;


    /** 合并名称 */
    @NotBlank
    private String mergerName;


    /** 经度 */
    @NotBlank
    private String lng;


    /** 纬度 */
    @NotBlank
    private String lat;


    /** 是否展示 */
    @NotNull
    private Integer isShow;

    @TableField(exist = false)
    private List<YqmSystemCity> children;


    public void copy(YqmSystemCity source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
