package com.yqm.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 话题
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmHelpRequest extends BaseRequest implements Serializable {

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
     * 分类id
     */
    private String classificationId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
    
    private String headerImg;


}