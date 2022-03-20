package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品属性
 * </p>
 *
 * @author weiximei
 * @since 2022-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmStoreAttribute extends BaseEntity implements Serializable {

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
     * 类型id
     */
    private String storeTypeId;

    /**
     * 属性名称
     */
    private String specName;

    /**
     * 选择类型
     */
    private String chooseType;

    /**
     * 录入方式
     */
    private String inputType;

    /**
     * 值
     */
    private String inputValue;


}