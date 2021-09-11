package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 用户表
    * </p>
*
* @author weiximei
* @since 2021-09-11
*/
    @Data
    @EqualsAndHashCode(callSuper = false)
    public class TpUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

        @TableField("USER_NAME")
    private String userName;

        @TableField("SEX")
    private Integer sex;

        @TableField("PHONE")
    private String phone;

        @TableField("EMAIL")
    private String email;

        @TableField("ADDRESS")
    private String address;

        @TableField("POSTAL_CODE")
    private String postalCode;

        @TableField("ACCOUNT")
    private String account;

        @TableField("PASSWORD")
    private String password;

        @TableField("PROVINCE_ID")
    private String provinceId;

    private String cityId;

    private String areaId;

    private Integer enterpriseCertification;

    private String status;

        @TableField("CREATED_BY")
    private String createdBy;

        @TableField("CREATED_TIME")
    private LocalDateTime createdTime;

        @TableField("UPDATED_BY")
    private String updatedBy;

        @TableField("UPDATED_TIME")
    private LocalDateTime updatedTime;


}