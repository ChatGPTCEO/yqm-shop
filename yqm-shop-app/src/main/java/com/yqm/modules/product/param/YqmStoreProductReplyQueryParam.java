package com.yqm.modules.product.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmStoreProductReplyQueryParam对象", description="评论表查询参数")
public class YqmStoreProductReplyQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
