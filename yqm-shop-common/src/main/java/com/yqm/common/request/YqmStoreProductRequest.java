package com.yqm.common.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmStoreProductRequest extends BaseRequest implements Serializable {

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
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 轮播图
     */
    private String productBanner;

    /**
     * 轮播图 集合
     */
    private List<String> productBannerList;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 品牌id
     */
    private String brandId;

    /**
     * 商品分类id
     */
    private String classifyId;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 运费模板id
     */
    private String freightTemplateId;

    /**
     * 货号
     */
    private String articleNumber;

    /**
     * 价格
     */
    private String price;

    /**
     * 市场价
     */
    private String marketPrice;

    /**
     * 总库存预警值
     */
    private Integer inventoryWarning;

    /**
     * 计量单位
     */
    private String measuringUnit;

    /**
     * 重量;单位克
     */
    private String weightNum;

    /**
     * 是否上架;shelves 上架 not_shelves 下架
     */
    private String isShelves;

    /**
     * 推荐
     */
    private String recommended;

    /**
     * 服务保证
     */
    private String serviceGuarantee;

    /**
     * 详情页标题
     */
    private String detailsTitle;

    /**
     * 详情页描述
     */
    private String detailsDescribe;

    /**
     * 商品备注
     */
    private String note;

    /**
     * 规格汇总
     */
    private String specifications;

    /**
     * 商品参数;json数据
     */
    private String parameter;

    /**
     * 审核;-1 待审核 0 不同意 1 同意
     */
    private Integer audit;

    /**
     * 审核完后信息
     */
    private String auditMessage;

    /**
     * 状态;delete 删除 success 有效
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示;show 显示 not_show 不显示
     */
    private String isShow;

    /**
     * sku 集合
     */
    private List<YqmStoreSkuRequest> skuList;

    /**
     * 规格
     */
    private List<YqmStoreSpecRequest> specList;

}