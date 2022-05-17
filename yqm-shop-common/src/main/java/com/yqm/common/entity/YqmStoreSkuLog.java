package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品sku日志
 * </p>
 *
 * @author weiximei
 * @since 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmStoreSkuLog extends BaseEntity implements Serializable {

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
     * 商品id
     */
    private String productId;

    /**
     * sku编号
     */
    private String skuId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 数量
     */
    private String num;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 库存类型;product 商品库存
     */
    private String inventoryType;

    /**
     * 操作类型;return_product 退货商品 add_product 添加商品 edi_product 编辑商品 order_close 订单关闭 order_cancel 订单取消 order_return_product 订单退货
     */
    private String operationType;

    /**
     * 操作人员名称
     */
    private String operationUserName;

    /**
     * 操作人员编号
     */
    private String operationUserId;

    /**
     * 类型;put_in_storage 入库 outbound 出库
     */
    private String logType;


}