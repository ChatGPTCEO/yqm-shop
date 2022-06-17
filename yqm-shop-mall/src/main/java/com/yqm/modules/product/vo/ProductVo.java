package com.yqm.modules.product.vo;

import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品dto
 * </p>
 *
 * @author weiximei
 * @date 2019-10-23
 */
@Data
public class ProductVo{

    @ApiModelProperty(value = "商品信息列表")
    private List<YqmStoreProductQueryVo> goodList = new ArrayList();

    @ApiModelProperty(value = "商户ID，预留字段")
    private Integer merId = 0;

    private String priceName = "";

    private List<YqmStoreProductAttrQueryVo> productAttr = new ArrayList();

    private Map<String, YqmStoreProductAttrValue>  productValue = new LinkedHashMap<>();

    @ApiModelProperty(value = "评论信息")
    private YqmStoreProductReplyQueryVo reply;

    @ApiModelProperty(value = "回复渠道")
    private String replyChance;

    @ApiModelProperty(value = "回复数")
    private Integer replyCount = 0;

    //todo
    private List similarity = new ArrayList();

    @ApiModelProperty(value = "商品信息")
    private YqmStoreProductQueryVo storeInfo;

    @ApiModelProperty(value = "腾讯地图key")
    private String mapKey;

    @ApiModelProperty(value = "门店信息")
    private YqmSystemStoreQueryVo systemStore;

    @ApiModelProperty(value = "用户ID")
    private Integer uid = 0;

    @ApiModelProperty(value = "模版名称")
    private String tempName;

}
