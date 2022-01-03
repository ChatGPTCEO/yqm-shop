package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 团队分类
 * </p>
 *
 * @author weiximei
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpTeamClassify extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String teamClassifyName;

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


}