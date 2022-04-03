package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单日志
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmOrderLog extends BaseEntity implements Serializable {

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
     * 订单id
     */
    private String orderId;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单状态;0 待付款 1 已付款 2 待发货 3 已完成 4 已关闭
     */
    private Integer orderStatus;

    /**
     * 备注
     */
    private String note;

    /**
     * 操作名称
     */
    private String userName;


}