/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.shop.domain.YqmSystemUserLevel;
import com.yqm.modules.user.domain.YqmUserLevel;
import com.yqm.modules.user.service.YqmSystemUserLevelService;
import com.yqm.modules.user.service.YqmSystemUserTaskService;
import com.yqm.modules.user.service.YqmUserLevelService;
import com.yqm.modules.user.service.dto.TaskDto;
import com.yqm.modules.user.service.dto.UserLevelDto;
import com.yqm.modules.user.service.dto.YqmSystemUserLevelDto;
import com.yqm.modules.user.service.dto.YqmSystemUserLevelQueryCriteria;
import com.yqm.modules.user.service.mapper.SystemUserLevelMapper;
import com.yqm.modules.user.vo.YqmSystemUserLevelQueryVo;
import com.yqm.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmSystemUserLevelServiceImpl extends BaseServiceImpl<SystemUserLevelMapper, YqmSystemUserLevel> implements YqmSystemUserLevelService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private SystemUserLevelMapper yqmSystemUserLevelMapper;
    @Autowired
    private YqmUserLevelService userLevelService;
    @Autowired
    private YqmSystemUserTaskService systemUserTaskService;

    /**
     * 获取当前的下一个会员等级id
     * @param levelId 等级id
     * @return int
     */
    @Override
    public int getNextLevelId(int levelId) {
        List<YqmSystemUserLevel> list = this.lambdaQuery()
                .eq(YqmSystemUserLevel::getIsShow,ShopCommonEnum.SHOW_1.getValue())
                .orderByAsc(YqmSystemUserLevel::getGrade)
                .list();

        int grade = 0;
        for (YqmSystemUserLevel userLevel : list) {
            if(userLevel.getId() == levelId) {
                grade = userLevel.getGrade();
            }
        }

        YqmSystemUserLevel userLevel = this.lambdaQuery()
                .eq(YqmSystemUserLevel::getIsShow,ShopCommonEnum.SHOW_1.getValue())
                .orderByAsc(YqmSystemUserLevel::getGrade)
                .gt(YqmSystemUserLevel::getGrade,grade)
                .last("limit 1")
                .one();
        if(ObjectUtil.isNull(userLevel)) {
            return 0;
        }
        return userLevel.getId();
    }

//    @Override
//    public boolean getClear(int levelId) {
//        List<YqmSystemUserLevelQueryVo> systemUserLevelQueryVos = this.getLevelListAndGrade(levelId);
//        for (YqmSystemUserLevelQueryVo userLevelQueryVo : systemUserLevelQueryVos) {
//            if(userLevelQueryVo.getId() == levelId) return userLevelQueryVo.getIsClear();
//        }
//        return false;
//    }



    /**
     * 获取会员等级列表及其任务列表
     * @return UserLevelDto
     */
    @Override
    public UserLevelDto getLevelInfo(Long uid) {
        int levelId = 0; //用户当前等级id 0-表示无
        YqmUserLevel userLevel = userLevelService.getUserLevel(uid, null);
        if(userLevel != null){
            levelId =  userLevel.getLevelId();
        }


        //会员等级列表
        List<YqmSystemUserLevelQueryVo> list = this.getLevelListAndGrade(levelId);
        if(list.isEmpty()) {
            throw new YqmShopException("请后台设置会员等级");
        }

        //任务列表
        TaskDto taskDTO = systemUserTaskService.getTaskList(list.get(0).getId(),uid);

        UserLevelDto userLevelDTO = new UserLevelDto();
        userLevelDTO.setList(list);
        userLevelDTO.setTask(taskDTO);

        return userLevelDTO;
    }

    /**
     * 获取会员等级列表
     * @param levelId 等级id
     * @return list
     */
    private List<YqmSystemUserLevelQueryVo> getLevelListAndGrade(Integer levelId) {
        Integer grade = 0;
        List<YqmSystemUserLevel> list = this.lambdaQuery()
                .eq(YqmSystemUserLevel::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .orderByAsc(YqmSystemUserLevel::getGrade)
                .list();
        List<YqmSystemUserLevelQueryVo> newList = generator.convert(list,YqmSystemUserLevelQueryVo.class);
        for (YqmSystemUserLevelQueryVo userLevelQueryVo : newList) {
            if(userLevelQueryVo.getId().compareTo(levelId) == 0) {
                grade = userLevelQueryVo.getGrade();
            }
            if(grade.compareTo(userLevelQueryVo.getGrade()) < 0){
                userLevelQueryVo.setIsClear(true); //不解锁
            }else{
                userLevelQueryVo.setIsClear(false);//开启会员解锁
            }
        }
        return newList;
    }


    //=========================================================================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmSystemUserLevelQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmSystemUserLevel> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmSystemUserLevelDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmSystemUserLevel> queryAll(YqmSystemUserLevelQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmSystemUserLevel.class, criteria));
    }


    @Override
    public void download(List<YqmSystemUserLevelDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmSystemUserLevelDto yqmSystemUserLevel : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商户id", yqmSystemUserLevel.getMerId());
            map.put("会员名称", yqmSystemUserLevel.getName());
            map.put("购买金额", yqmSystemUserLevel.getMoney());
            map.put("有效时间", yqmSystemUserLevel.getValidDate());
            map.put("是否为永久会员", yqmSystemUserLevel.getIsForever());
            map.put("是否购买,1=购买,0=不购买", yqmSystemUserLevel.getIsPay());
            map.put("是否显示 1=显示,0=隐藏", yqmSystemUserLevel.getIsShow());
            map.put("会员等级", yqmSystemUserLevel.getGrade());
            map.put("享受折扣", yqmSystemUserLevel.getDiscount());
            map.put("会员卡背景", yqmSystemUserLevel.getImage());
            map.put("会员图标", yqmSystemUserLevel.getIcon());
            map.put("说明", yqmSystemUserLevel.getExplain());
            map.put("添加时间", yqmSystemUserLevel.getAddTime());
            map.put("是否删除.1=删除,0=未删除", yqmSystemUserLevel.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
