package com.yqm.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmClassificationRequest extends BaseRequest implements Serializable {

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
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 商品分类
     */
    private String classifyName;

    /**
     * 父编号
     */
    private String pids;

    /**
     * 父编号
     */
    private List<String> pidsList;

    /**
     * 父编号
     */
    private String pid;

    /**
     * 等级
     */
    private Integer level;

    /**
     * 状态;delete 删除 success 有效
     */
    private String status;

    /**
     * 是否显示;show 显示 not_show 不显示
     */
    private String isShow;


    /**
     * 是否显示;show 显示 not_show 不显示
     */
    private String isNavigation;

    /**
     * 图标
     */
    private String logo;

    /**
     * 排序
     */
    private Integer sort;


}