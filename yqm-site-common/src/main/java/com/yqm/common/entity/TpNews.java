package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 新闻
 * </p>
 *
 * @author weiximei
 * @since 2021-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TpNews extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id;用户id
     */
    private String userId;

    /**
     * 公司id;公司id
     */
    private String companyId;

    /**
     * 新闻标题
     */
    private String newsTitle;

    /**
     * 短标题
     */
    private String shortTitle;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 摘要
     */
    @TableField("`abstract`")
    private String abstractText;

    /**
     * 分类
     */
    private String newsClassifyId;

    /**
     * 发布时间
     */
    private LocalDateTime releaseTime;

    /**
     * 来源
     */
    private String source;

    /**
     * 作者
     */
    private String author;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否置顶;0 否 1 是
     */
    private String isTop;

    /**
     * 封面
     */
    private String newsImg;

    /**
     * 是否显示封面;0 否 1 是
     */
    private String showNewsImg;

    /**
     * 外链地址
     */
    private String url;

    /**
     * 英文标识
     */
    private String englishId;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签
     */
    private String tags;

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
    private LocalDateTime updatedTime;


}