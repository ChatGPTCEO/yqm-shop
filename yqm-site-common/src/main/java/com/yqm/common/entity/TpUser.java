package com.yqm.common.entity;

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

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 性别;0 未知 1 男 2 女
     */
    private Integer sex;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址;详细地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

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

    /**
     * 企业认证;0未认证 1 认证 2 认证审核中
     */
    private Integer enterpriseCertification;

    /**
     * 状态;effective 有效 failure 失效 delete 删除
     */
    private String status;

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
    private LocalDateTime updatedTime;

    /**
     * 头像
     */
    private String avatar;



}