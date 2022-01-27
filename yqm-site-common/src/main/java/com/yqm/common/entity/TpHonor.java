package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 荣誉证书
 * </p>
 *
 * @author weiximei
 * @since 2021-09-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpHonor extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 证书名称
     */
    private String honorName;

    /**
     * 证书图片
     */
    private String honorImg;

    /**
     * 证书分类id
     */
    private String honorClassifyId;

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
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

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
    @TableField(value = "updated_time", update = "now()")
    private LocalDateTime updatedTime;


}