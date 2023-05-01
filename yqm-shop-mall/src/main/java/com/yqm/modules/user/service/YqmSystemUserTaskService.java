/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.user.domain.YqmSystemUserTask;
import com.yqm.modules.user.service.dto.TaskDto;
import com.yqm.modules.user.service.dto.YqmSystemUserTaskDto;
import com.yqm.modules.user.service.dto.YqmSystemUserTaskQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmSystemUserTaskService  extends BaseService<YqmSystemUserTask>{


    /**
     * 获取已经完成的任务数量
     * @param levelId 等级id
     * @param uid uid
     * @return int
     */
    int getTaskComplete(int levelId,Long uid);

    /**
     * 获取等级会员任务列表
     * @param levelId 等级id
     * @param uid uid
     * @return TaskDto
     */
    TaskDto getTaskList(int levelId, Long uid);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmSystemUserTaskQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmSystemUserTaskDto>
    */
    List<YqmSystemUserTask> queryAll(YqmSystemUserTaskQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmSystemUserTaskDto> all, HttpServletResponse response) throws IOException;
}
