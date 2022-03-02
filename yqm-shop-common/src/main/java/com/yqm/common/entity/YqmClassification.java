package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmClassification extends BaseEntity implements Serializable {

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
     * 商品分类
     */
    private String classifyName;

    /**
     * 父编号
     */
    private String pids;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 状态;delete 删除 success 有效
     */
    private String status;


}