package com.yqm.common.dto;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单子表
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmOrderItemDTO extends BaseEntity implements Serializable {

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
     * 商品编号
     */
    private String productId;

    /**
     * sku
     */
    private String skuId;

    /**
     * 数量
     */
    private String num;

    /**
     * 价格
     */
    private String price;

    // 额外

    /**
     * 商品
     */
    private YqmStoreProductDTO storeProductDTO;

}