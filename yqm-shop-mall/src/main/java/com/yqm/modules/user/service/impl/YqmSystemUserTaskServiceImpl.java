/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.order.service.mapper.StoreOrderMapper;
import com.yqm.modules.shop.domain.YqmSystemUserLevel;
import com.yqm.modules.user.domain.YqmSystemUserTask;
import com.yqm.modules.user.domain.YqmUserTaskFinish;
import com.yqm.modules.user.service.YqmSystemUserLevelService;
import com.yqm.modules.user.service.YqmSystemUserTaskService;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.YqmUserTaskFinishService;
import com.yqm.modules.user.service.dto.TaskDto;
import com.yqm.modules.user.service.dto.YqmSystemUserTaskDto;
import com.yqm.modules.user.service.dto.YqmSystemUserTaskQueryCriteria;
import com.yqm.modules.user.service.mapper.SystemUserTaskMapper;
import com.yqm.modules.user.service.mapper.UserBillMapper;
import com.yqm.modules.user.service.mapper.YqmUserTaskFinishMapper;
import com.yqm.modules.user.vo.YqmSystemUserTaskQueryVo;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmSystemUserTaskServiceImpl extends BaseServiceImpl<SystemUserTaskMapper, YqmSystemUserTask> implements YqmSystemUserTaskService {

    @Autowired
    private IGenerator generator;
    @Autowired
    private YqmSystemUserLevelService systemUserLevelService;

    @Autowired
    private SystemUserTaskMapper yqmSystemUserTaskMapper;
    @Autowired
    private YqmUserTaskFinishMapper yqmUserTaskFinishMapper;

    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private StoreOrderMapper storeOrderMapper;

    @Autowired
    private YqmUserTaskFinishService userTaskFinishService;
    @Autowired
    private YqmUserBillService userBillService;





    /**
     * 获取已经完成的任务数量
     * @param levelId 等级id
     * @param uid uid
     * @return int
     */
    @Override
    public int getTaskComplete(int levelId, Long uid) {
        List<YqmSystemUserTask> list = this.lambdaQuery()
                .eq(YqmSystemUserTask::getLevelId,levelId)
                .eq(YqmSystemUserTask::getIsShow,ShopCommonEnum.SHOW_1.getValue())
                .list();
        List<Integer> taskIds = list.stream().map(YqmSystemUserTask::getId)
                .collect(Collectors.toList());
        if(taskIds.isEmpty()) {
            return 0;
        }

        int count = yqmUserTaskFinishMapper.selectCount(Wrappers.<YqmUserTaskFinish>lambdaQuery()
                .in(YqmUserTaskFinish::getTaskId,taskIds)
                .eq(YqmUserTaskFinish::getUid,uid));
        return count;
    }

    /**
     * 获取等级会员任务列表
     * @param levelId 等级id
     * @param uid uid
     * @return TaskDto
     */
    @Override
    public TaskDto getTaskList(int levelId, Long uid) {
       LambdaQueryWrapper<YqmSystemUserTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmSystemUserTask::getLevelId,levelId)
                .eq(YqmSystemUserTask::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .orderByDesc(YqmSystemUserTask::getSort);
        List<YqmSystemUserTaskQueryVo> list = generator.convert(yqmSystemUserTaskMapper
                .selectList(wrapper),YqmSystemUserTaskQueryVo.class);

        TaskDto taskDTO = new TaskDto();
        taskDTO.setList(list);
        taskDTO.setReachCount(this.getTaskComplete(levelId,uid));
        taskDTO.setTask(this.tidyTask(list,uid));

        return taskDTO;
    }


    /**
     * 设置任务内容完成情况
     * @param task 任务列表
     * @param uid uid
     * @return list
     */
    private List<YqmSystemUserTaskQueryVo> tidyTask(List<YqmSystemUserTaskQueryVo> task,Long uid) {
        for (YqmSystemUserTaskQueryVo taskQueryVo : task) {
            int count = userTaskFinishService.lambdaQuery()
                    .eq(YqmUserTaskFinish::getTaskId,taskQueryVo.getId())
                    .eq(YqmUserTaskFinish::getUid,uid)
                    .count();
            if(count > 0){
                taskQueryVo.setNewNumber(taskQueryVo.getNumber());
                taskQueryVo.setSpeed(100); //完成比例
                taskQueryVo.setFinish(ShopCommonEnum.IS_FINISH_1.getValue());
                taskQueryVo.setTaskTypeTitle("");
            }else{
                double sumNumber = 0d;
                String title = "";
                switch (taskQueryVo.getTaskType()){
                    case "SatisfactionIntegral":
                        sumNumber = userBillMapper.sumIntegral(uid);
                        title = "还需要{0}经验";
                        break;
                    case "ConsumptionAmount":
                        sumNumber = storeOrderMapper.sumPrice(uid);
                        title = "还需消费{0}元";
                        break;
                    case "CumulativeAttendance":
                        sumNumber = userBillService.cumulativeAttendance(uid);
                        title = "还需签到{0}天";
                        break;
                    default:
                }

                if(sumNumber >= taskQueryVo.getNumber()){
                    userTaskFinishService.setFinish(uid,taskQueryVo.getId());
                    taskQueryVo.setFinish(ShopCommonEnum.IS_FINISH_1.getValue());
                    taskQueryVo.setSpeed(100);
                    taskQueryVo.setTaskTypeTitle("");
                    taskQueryVo.setNewNumber(taskQueryVo.getNumber());
                }else{
                    double numdata = NumberUtil.sub(taskQueryVo.getNumber().doubleValue(),sumNumber);
                    taskQueryVo.setTaskTypeTitle(MessageFormat.format(title,numdata));
                    double speed = NumberUtil.div(sumNumber,taskQueryVo.getNumber().doubleValue());
                    taskQueryVo.setSpeed(Double.valueOf(NumberUtil.mul(speed,100)).intValue());
                    taskQueryVo.setFinish(ShopCommonEnum.IS_FINISH_0.getValue());
                    taskQueryVo.setNewNumber(Double.valueOf(sumNumber).intValue());
                }
            }
        }

        return task;
    }


    //==========================================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmSystemUserTaskQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmSystemUserTask> page = new PageInfo<>(queryAll(criteria));
        List<YqmSystemUserTaskDto> systemUserTaskDTOS = generator.convert(page.getList(),YqmSystemUserTaskDto.class);
        for (YqmSystemUserTaskDto systemUserTaskDTO : systemUserTaskDTOS) {
            YqmSystemUserLevel userLevel=systemUserLevelService.getById(systemUserTaskDTO.getLevelId());
            if(userLevel == null) {
                continue;
            }
            systemUserTaskDTO.setLevalName(userLevel.getName());
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", systemUserTaskDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmSystemUserTask> queryAll(YqmSystemUserTaskQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmSystemUserTask.class, criteria));
    }


    @Override
    public void download(List<YqmSystemUserTaskDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmSystemUserTaskDto yqmSystemUserTask : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("任务名称", yqmSystemUserTask.getName());
            map.put("配置原名", yqmSystemUserTask.getRealName());
            map.put("任务类型", yqmSystemUserTask.getTaskType());
            map.put("限定数", yqmSystemUserTask.getNumber());
            map.put("等级id", yqmSystemUserTask.getLevelId());
            map.put("排序", yqmSystemUserTask.getSort());
            map.put("是否显示", yqmSystemUserTask.getIsShow());
            map.put("是否务必达成任务,1务必达成,0=满足其一", yqmSystemUserTask.getIsMust());
            map.put("任务说明", yqmSystemUserTask.getIllustrate());
            map.put("新增时间", yqmSystemUserTask.getAddTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
