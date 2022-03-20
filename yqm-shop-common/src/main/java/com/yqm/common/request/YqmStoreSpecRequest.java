package com.yqm.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 规格属性
 * </p>
 *
 * @author weiximei
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmStoreSpecRequest extends BaseRequest implements Serializable {

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
     * 父id
     */
    private String pid;

    /**
     * 名称
     */
    private String specName;

    /**
     * 父属性名称
     */
    private String prentSpecName;

    /**
     * 0属性 1规格
     */
    private Integer isParent;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 属性模板id
     */
    private String attrbuteId;

    /**
     * 商品类型id
     */
    private String typeId;

    /**
     * sku 集合
     */
    private List<YqmStoreSpecRequest> inputValue;


}