package com.yqm.modules.cart.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>
 * 购物车表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-25
 */
@Data
@ApiModel(value="YqmStoreCartQueryParam对象", description="购物车表查询参数")
public class YqmStoreCartQueryParam  {
    private static final long serialVersionUID = 1L;

    private Integer numType = 0;
}
