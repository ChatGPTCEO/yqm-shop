package com.yqm.modules.product.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 商品点赞和收藏表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-23
 */
@Getter
@Setter
@ApiModel("查询参数对象")
public class YqmStoreProductRelationQueryParam  {

    @NotBlank(message = "参数有误")
    @ApiModelProperty(value = "商品id",required=true)
    private String id;

    @ApiModelProperty(value = "某种类型的商品(普通商品、秒杀商品)")
    private String category = "root";
}
