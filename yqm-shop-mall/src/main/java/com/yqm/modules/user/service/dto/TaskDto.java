package com.yqm.modules.user.service.dto;


import com.yqm.modules.user.vo.YqmSystemUserTaskQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TaskDto
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/12/6
 **/
@Data
public class TaskDto implements Serializable {
    private List<YqmSystemUserTaskQueryVo> list;
    private Integer reachCount;
    private List<YqmSystemUserTaskQueryVo> task;
}
