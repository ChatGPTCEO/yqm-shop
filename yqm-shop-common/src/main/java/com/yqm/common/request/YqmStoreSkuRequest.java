package com.yqm.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品sku
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmStoreSkuRequest extends BaseRequest implements Serializable {

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
    private String updatedTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedBy;

    /**
     * 名称
     */
    private String skuName;

    /**
     * 值
     */
    private String skuValue;

    /**
     * 销售价
     */
    private String salePrice;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 库存预警值;0 没有
     */
    private String inventoryWarning;

    /**
     * sku编号
     */
    private String skuNum;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图片
     */
    private String img;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 状态;delete 删除 success 有效
     */
    private String status;

    /**
     * spec;组合属性
     */
    private String specIds;

    /**
     * 父spec;组合父属性
     */
    private String pspecIds;

    /**
     * sku 集合
     */
    private List<YqmStoreSpecRequest> spec;
}