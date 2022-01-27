package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
* <p>
    * 地区表
    * </p>
*
* @author weiximei
* @since 2021-10-24
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class TpRegion extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域上级标识
     */
    private Integer pid;

    /**
     * 地名简称
     */
    private String sname;

    /**
     * 区域等级
     */
    private Integer level;

    /**
     * 区域编码
     */
    private String citycode;

    /**
     * 邮政编码
     */
    private String yzcode;

    /**
     * 组合名称
     */
    private String mername;

    /**
     *
     */
    @TableField("Lng")
    private Float lng;

    /**
     *
     */
    @TableField("Lat")
    private Float lat;

    /**
     *
     */
    private String pinyin;


}