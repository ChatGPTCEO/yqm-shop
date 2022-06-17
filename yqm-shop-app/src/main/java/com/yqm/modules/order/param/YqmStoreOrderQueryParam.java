package com.yqm.modules.order.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmStoreOrderQueryParam对象", description="订单表查询参数")
public class YqmStoreOrderQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    private Integer type = 1;
}
