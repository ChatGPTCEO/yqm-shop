package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 友情链接
    * </p>
*
* @author weiximei
* @since 2021-09-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class TpLink extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 链接名称
     */
    private String linkName;

    /**
     * 链接图片
     */
    private String linkImg;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 链接分类
     */
    private String linkClassifyId;

    /**
     * 公司id
     */
    private String companyId;

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


}