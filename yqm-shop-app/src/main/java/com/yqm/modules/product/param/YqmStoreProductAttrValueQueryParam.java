package com.yqm.modules.product.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品属性值表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmStoreProductAttrValueQueryParam对象", description="商品属性值表查询参数")
public class YqmStoreProductAttrValueQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
