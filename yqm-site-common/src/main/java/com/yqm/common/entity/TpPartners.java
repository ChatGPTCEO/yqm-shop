package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 合作伙伴
    * </p>
*
* @author weiximei
* @since 2021-09-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class TpPartners extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合作伙伴名称
     */
    private String partnersName;

    /**
     * 合作伙伴分类
     */
    private String partnersClassifyId;

    /**
     * 合作伙伴地址
     */
    private String partnersAddress;

    /**
     * 合作伙伴图片
     */
    private String img;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 用户id
     */
    private String userId;

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