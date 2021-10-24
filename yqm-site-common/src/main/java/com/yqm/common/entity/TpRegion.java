package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    public class TpRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 地区代码
     */
    @TableId(type = IdType.NONE)
    private Integer code;

    /**
     * 上级地区代码
     */
    private Integer pCode;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 等级 1 省 2 市 3 区
     */
    private Integer level;

}