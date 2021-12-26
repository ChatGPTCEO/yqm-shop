package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 站点
 * </p>
 *
 * @author weiximei
 * @since 2021-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpSite implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 语言版本:zh_cn 中文 us_en 英文
     */
    @TableField("`language`")
    private String language;

    /**
     * 系统版本
     */
    private String systemVersion;

    /**
     * 到期时间
     */
    private String dueTime;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

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
    private LocalDateTime updatedTime;


}