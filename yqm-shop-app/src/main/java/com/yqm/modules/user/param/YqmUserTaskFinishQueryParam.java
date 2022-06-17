package com.yqm.modules.user.param;

import com.yqm.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户任务完成记录表 查询参数对象
 * </p>
 *
 * @author weiximei
 * @date 2019-12-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YqmUserTaskFinishQueryParam对象", description="用户任务完成记录表查询参数")
public class YqmUserTaskFinishQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
