package com.yqm.modules.activity.vo;


import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.vo.YqmStoreProductAttrQueryVo;
import com.yqm.modules.product.vo.YqmStoreProductReplyQueryVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀产品表 查询结果对象
 * </p>
 *
 * @author weiximei
 * @date 2019-12-17
 */
@Data
@Builder
public class StoreSeckillVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "秒杀产品评论信息")
    private YqmStoreProductReplyQueryVo reply;

    @ApiModelProperty(value = "秒杀产品评论数量")
    private Integer replyCount;

    @ApiModelProperty(value = "秒杀产品信息")
    private YqmStoreSeckillQueryVo storeInfo;

    @Builder.Default
    @ApiModelProperty(value = "秒杀产品用户是否收藏")
    private Boolean userCollect = false;

    @ApiModelProperty(value = "模板名称")
    private String tempName;

    private List<YqmStoreProductAttrQueryVo> productAttr = new ArrayList();

    private Map<String, YqmStoreProductAttrValue> productValue = new LinkedHashMap<>();

}
