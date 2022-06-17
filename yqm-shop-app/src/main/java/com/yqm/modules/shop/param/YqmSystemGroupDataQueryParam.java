package com.yqm.modules.shop.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 组合数据详情表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmSystemGroupDataQueryParam对象", description="组合数据详情表查询参数")
public class YqmSystemGroupDataQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
