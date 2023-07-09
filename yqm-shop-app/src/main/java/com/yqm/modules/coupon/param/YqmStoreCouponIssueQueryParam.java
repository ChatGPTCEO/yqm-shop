package com.yqm.modules.coupon.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 优惠券前台领取表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmStoreCouponIssueQueryParam对象", description="优惠券前台领取表查询参数")
public class YqmStoreCouponIssueQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}