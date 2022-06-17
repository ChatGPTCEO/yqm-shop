package com.yqm.modules.user.service.dto;


import com.yqm.modules.user.vo.YqmSystemUserLevelQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName UserLevelDto
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/12/6
 **/
@Data
public class UserLevelDto implements Serializable {
    private List<YqmSystemUserLevelQueryVo> list;
    private TaskDto task;
}
