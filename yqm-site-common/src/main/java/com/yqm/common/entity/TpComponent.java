package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 组件
 * </p>
 *
 * @author weiximei
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpComponent extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模块id
     */
    private String moduleId;

    /**
     * 模板id
     */
    private String templateId;

    /**
     * code名称
     */
    private String codeName;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 图片
     */
    private String componentImg;

    /**
     * 内容
     */
    private String content;

    /**
     * 模块排序
     */
    private Integer templateSort;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", update = "now()")
    private LocalDateTime updatedTime;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除
     */
    private String status;


}