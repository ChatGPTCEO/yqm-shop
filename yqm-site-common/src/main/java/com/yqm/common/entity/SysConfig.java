package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 站点配置
    * </p>
*
* @author weiximei
* @since 2021-10-23
*/
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class SysConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String configName;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 描述
     */
    private String configDesc;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;


}