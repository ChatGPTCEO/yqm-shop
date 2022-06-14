package com.yqm.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 专题
 * </p>
 *
 * @author weiximei
 * @since 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmProjectRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态;delete 删除 success 有效 failure 失效
     */
    private String status;

    /**
     * 分类id
     */
    private String classificationId;

    /**
     * 标题
     */
    private String title;

    /**
     * 图片
     */
    private String imgs;


    /**
     * 主图
     */
    private String headerImg;

    /**
     * 描述
     */
    private String describe;


    /**
     * 正文内容
     */
    private String content;

    /**
     * 商品集合
     */
    private List<YqmStoreProductRequest> productList;

}