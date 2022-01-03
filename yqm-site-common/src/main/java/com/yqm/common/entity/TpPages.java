package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 页面
 * </p>
 *
 * @author weiximei
 * @since 2022-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpPages extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 站点id
     */
    private String siteId;

    /**
     * 页面路径
     */
    private String pageUrl;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 页面类型;system 系统 user 用户
     */
    private String pageType;

    /**
     * SEO标题
     */
    private String seoTitle;

    /**
     * SEO关键字
     */
    private String seoKeyword;

    /**
     * SEO描述
     */
    private String seoContent;

    /**
     * 插件代码
     */
    private String plugCode;

    /**
     * 插件代码位置;head 在head结束标签前 body 在body结束标签前
     */
    private String plugLocation;

    /**
     * 父id
     */
    private String pid;

    /**
     * 父id集合
     */
    private String pids;

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