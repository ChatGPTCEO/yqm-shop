package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 招聘
    * </p>
*
* @author weiximei
* @since 2021-09-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class TpRecruitment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String position;

    private String recNum;

    private String status;

    private Integer sort;

    private String responsibility;

    private String requirements;

    private String companyId;

    private String phone;

    private String userName;

        @TableField("CREATED_BY")
    private String createdBy;

        @TableField("CREATED_TIME")
    private LocalDateTime createdTime;

        @TableField("UPDATED_BY")
    private String updatedBy;

        @TableField("UPDATED_TIME")
    private LocalDateTime updatedTime;


}