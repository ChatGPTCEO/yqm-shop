package com.yqm.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 运费模板
 * </p>
 *
 * @author weiximei
 * @since 2022-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmFreightTemplateRequest extends BaseRequest implements Serializable {

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
     * 状态;delete 删除 success 有效
     */
    private String status;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 运费方式;weight 重量 piece 单件
     */
    private String priceType;

    /**
     * 首重量kg
     */
    private String firstNum;

    /**
     * 首费(分)
     */
    private String firstPrice;

    /**
     * 续重kg
     */
    private String continueNum;

    /**
     * 续费(分)
     */
    private String continuePiece;


}