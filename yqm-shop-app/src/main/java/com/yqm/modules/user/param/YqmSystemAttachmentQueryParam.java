package com.yqm.modules.user.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 附件管理表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmSystemAttachmentQueryParam对象", description="附件管理表查询参数")
public class YqmSystemAttachmentQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
