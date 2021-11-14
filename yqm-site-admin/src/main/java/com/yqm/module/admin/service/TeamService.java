/*
 * Copyright 2021 Wei xi mei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 *
 * limitations under the License.
 */

package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.TpTeamToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpTeamDTO;
import com.yqm.common.entity.TpTeam;
import com.yqm.common.request.TpTeamRequest;
import com.yqm.common.service.ITpTeamService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-团队
 * @Author: weiximei
 * @Date: 2021/11/7 19:33
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class TeamService {


    private ITpTeamService iTpTeamService;

    public TeamService(ITpTeamService iTpTeamService) {
        this.iTpTeamService = iTpTeamService;
    }

    /**
     * 保存/修改 团队分类
     * @param request
     * @return
     */
    public TpTeamDTO saveTeam(TpTeamRequest request) {
        User user = UserInfoService.getUser();

        TpTeam team = TpTeamToDTO.toTpTeam(request);
        if (StringUtils.isEmpty(request.getId())) {
            team.setCreatedBy(user.getId());
            team.setCreatedTime(LocalDateTime.now());

            int maxSort = iTpTeamService.getMaxSort(user.getId());
            iTpTeamService.updateAllSortGal(maxSort,user.getId());
            team.setSort(1);
        }

        team.setUserId(user.getId());
        team.setStatus(YqmDefine.StatusType.effective.getValue());
        team.setUpdatedBy(user.getId());
        team.setUpdatedTime(LocalDateTime.now());
        iTpTeamService.saveOrUpdate(team);

        return TpTeamToDTO.toTpTeamDTO(team);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TpTeamDTO getById(String id) {
        TpTeam team = iTpTeamService.getById(id);
        return TpTeamToDTO.toTpTeamDTO(team);
    }

    /**
     * 删除团队分类
     * @param id
     * @return
     */
    public String removeTeam(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpTeam> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpTeamService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * @return
     */
    public String enableTeam(TpTeamRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用团队错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpTeam team = iTpTeamService.getById(request.getId());
        if (Objects.isNull(team)) {
            log.error("操作异常->停用/启用团队错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(team.getStatus())) {
            log.error("操作异常->停用/启用团队错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpTeam> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpTeamService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 团队分类
     * @param request
     * @return
     */
    public IPage<TpTeamDTO> pageTeam(TpTeamRequest request) {
        User currentUser = UserInfoService.getUser();
        Page<TpTeam> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpTeamService.page(page, iTpTeamService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpTeamToDTO.toTpTeamDTOList(list));
        }
        return pageList;
    }

    /**
     * 置顶
     * @param id
     * @return
     */
    public String top(String id) {
        User user = UserInfoService.getUser();

        TpTeam tpTeam = iTpTeamService.getById(id);
        if (Objects.nonNull(tpTeam)) {
            iTpTeamService.updateAllSortGal(tpTeam.getSort(), user.getId());
            iTpTeamService.top(id, user.getId());
        }

        return id;
    }

}
