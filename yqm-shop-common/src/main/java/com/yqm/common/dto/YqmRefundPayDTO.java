package com.yqm.common.dto;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 退款
 * </p>
 *
 * @author weiximei
 * @since 2022-04-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmRefundPayDTO extends BaseEntity implements Serializable {

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
     * 订单编号
     */
    private String orderId;

    /**
     * 申请状态
     */
    private Integer refundStatus;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 订单金额
     */
    private String amountPayable;

    /**
     * 退款金额
     */
    private String refundAmount;

    /**
     * 退款方式;return_pay 退回到原支付渠道
     */
    private String refundWay;

    /**
     * 退款类型;cancel_order 取消订单
     */
    private String refundType;

    /**
     * 退款描述
     */
    private String refundWhy;

    /**
     * 处理时间
     */
    private LocalDateTime processingTime;

    /**
     * 处理人员姓名
     */
    private String processingUserName;

    /**
     * 处理人员id
     */
    private String processingUserId;

    /**
     * 处理备注
     */
    private String processingNote;


}