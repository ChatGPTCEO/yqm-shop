package com.yqm.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 相册图片
    * </p>
*
* @author weiximei
* @since 2022-01-30
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class YqmPhotoalbumImg implements Serializable {

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
     * 排序
     */
    private Integer sort;

    /**
     * 状态;delete 删除 success 有效
     */
    private String status;

    /**
     * 相册id
     */
    private String photoalbumId;

    /**
     * 图片
     */
    private String img;

    /**
     * 介绍
     */
    private String introduce;


}