package com.yqm.modules.shop.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 配置表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmSystemConfigQueryParam对象", description="配置表查询参数")
public class YqmSystemConfigQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
