package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author weiximei
 * @since 2022-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmDictionary extends BaseEntity implements Serializable {

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
     * 字典名称
     */
    private String dictionaryName;

    /**
     * 字典code
     */
    private String dictionaryCode;

    /**
     * 父code
     */
    private String pcode;

    /**
     * 描述
     */
    private String dictionaryDesc;


}