package com.yqm.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 帮助
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmHelpDTO extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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


    private YqmHelpClassificationDTO helpClassificationDTO;

    private YqmUserDTO userDTO;


}