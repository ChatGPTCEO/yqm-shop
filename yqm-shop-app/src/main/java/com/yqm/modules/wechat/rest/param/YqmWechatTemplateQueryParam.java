package com.yqm.modules.wechat.rest.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 微信模板 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmWechatTemplateQueryParam对象", description="微信模板查询参数")
public class YqmWechatTemplateQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
