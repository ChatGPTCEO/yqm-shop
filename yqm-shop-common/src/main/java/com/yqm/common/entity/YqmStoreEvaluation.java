package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品评价
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmStoreEvaluation extends BaseEntity implements Serializable {

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
     * 商品id
     */
    private String productId;

    /**
     * sku编号
     */
    private String skuId;

    /**
     * 分数
     */
    private String score;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 是否显示;show 显示 not_show 不显示
     */
    private String isShow;

    /**
     * 内容
     */
    private String content;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 评论id
     */
    private String replyId;


}