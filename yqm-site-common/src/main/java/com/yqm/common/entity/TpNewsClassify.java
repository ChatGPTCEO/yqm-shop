package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 新闻分类
 * </p>
 *
 * @author weiximei
 * @since 2021-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpNewsClassify extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父编号
     */
    private String pid;

    /**
     * 父id集合
     */
    private String pids;

    /**
     * 名称
     */
    private String newsClassifyName;

    /**
     * 图片
     */
    private String newsClassifyImg;

    /**
     * 描述
     */
    private String content;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示;0 不显示 1显示
     */
    @TableField("`show`")
    private String show;

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
     * 插件代码位置
     */
    private String plugLocation;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

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