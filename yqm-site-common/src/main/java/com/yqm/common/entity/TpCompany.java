package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 公司
 * </p>
 *
 * @author weiximei
 * @since 2021-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpCompany extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公司名称
     */
    private String company;

    /**
     * LOGO
     */
    private String logo;

    /**
     * 公司法人
     */
    private String legalRepresentative;

    /**
     * 法人电话
     */
    private String legalRepresentativePhone;

    /**
     * 成立时间
     */
    private LocalDate establishedTime;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 传真
     */
    private String fax;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 企业执照
     */
    private String businessLicense;

    /**
     * 简介
     */
    private String introduce;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", update = "now()")
    private LocalDateTime updatedTime;

    /**
     * 业务人员名称
     */
    private String businessPersonnelName;

    /**
     * 业务人员手机
     */
    private String businessPersonnelPhone;

    /**
     * 业务人员邮件
     */
    private String businessPersonnelEmail;

    /**
     * 业务人员性别
     */
    private String businessPersonnelSex;

    /**
     * 业务人员职务
     */
    private String businessPersonnelPost;

    /**
     * 省
     */
    private String provinceId;

    /**
     * 市
     */
    private String cityId;

    /**
     * 区
     */
    private String areaId;

}