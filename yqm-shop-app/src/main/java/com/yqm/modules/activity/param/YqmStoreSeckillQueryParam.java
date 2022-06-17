package com.yqm.modules.activity.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品秒杀产品表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-12-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="商品秒杀产品表查询参数", description="商品秒杀产品表查询参数")
public class YqmStoreSeckillQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
