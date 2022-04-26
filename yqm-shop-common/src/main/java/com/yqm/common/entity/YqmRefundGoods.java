package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 退货
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmRefundGoods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 排序
     */
    private Integer sort;

    /**
     * 状态;delete 删除 success 有效 failure 失效
     */
    private String status;

    /**
     * 退状态;0 待处理 1 退货中 2 已完成 3 已拒绝
     */
    private Integer refundStatus;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 申请人
     */
    private String userName;

    /**
     * 申请人联系方式
     */
    private String userPhone;

    /**
     * 原因
     */
    private String why;

    /**
     * 描述
     */
    private String refundDescribe;

    /**
     * 图片集
     */
    private String imgs;

    /**
     * 订单金额
     */
    private String amountPayable;

    /**
     * 是否退运费;0 不退运费 1 退运费
     */
    private String isFreight;

    /**
     * 退款金额
     */
    private String refundAmount;

    /**
     * 用户收货地址
     */
    private String userShippingAddress;

    /**
     * 用户收货电话
     */
    private String userShippingPhone;

    /**
     * 用户收货人姓名
     */
    private String userShippingName;

    /**
     * 省id
     */
    private String provinceId;

    /**
     * 省
     */
    private String provinceName;

    /**
     * 市id
     */
    private String cityId;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区id
     */
    private String areaId;

    /**
     * 区
     */
    private String areaName;

    /**
     * 快递单号
     */
    private String expressSingle;

    /**
     * 物流轨迹
     */
    private String expressLogistics;

    /**
     * 处理时间
     */
    private LocalDateTime processingTime;

    /**
     * 处理人姓名
     */
    private String processingUserName;

    /**
     * 处理人id
     */
    private String processingUserId;

    /**
     * 处理备注
     */
    private String processingNote;

    /**
     * 系统人员用户id
     */
    private String adminUserId;

    /**
     * 系统人员收货备注
     */
    private String adminShippingNote;

    /**
     * 系统人员收货时间
     */
    private LocalDateTime adminShippingTime;

    /**
     * 系统人员收货名称
     */
    private String adminShippingName;


}