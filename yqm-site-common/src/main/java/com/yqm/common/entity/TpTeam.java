package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 团队
    * </p>
*
* @author weiximei
* @since 2021-09-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class TpTeam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String img;

    private String teamClassifyId;

    private Integer sort;

    private String companyId;

        @TableField("CREATED_BY")
    private String createdBy;

        @TableField("CREATED_TIME")
    private LocalDateTime createdTime;

        @TableField("UPDATED_BY")
    private String updatedBy;

        @TableField("UPDATED_TIME")
    private LocalDateTime updatedTime;


}