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
import com.yqm.common.conversion.TpTeamClassifyToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpTeamClassifyDTO;
import com.yqm.common.entity.TpTeamClassify;
import com.yqm.common.request.TpTeamClassifyRequest;
import com.yqm.common.service.ITpTeamClassifyService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-团队分类
 * @Author: weiximei
 * @Date: 2021/11/7 17:30
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TeamClassifyService {

    private ITpTeamClassifyService iTpTeamClassifyService;

    public TeamClassifyService(ITpTeamClassifyService iTpTeamClassifyService) {
        this.iTpTeamClassifyService = iTpTeamClassifyService;
    }

    /**
     * 保存/修改 团队分类
     * @param request
     * @return
     */
    public TpTeamClassifyDTO saveTeamClassify(TpTeamClassifyRequest request) {
        User user = UserInfoService.getUser();

        TpTeamClassify teamClassify = TpTeamClassifyToDTO.toTpTeamClassify(request);
        if (StringUtils.isEmpty(request.getId())) {
            teamClassify.setCreateBy(user.getId());
            teamClassify.setCreateTime(LocalDateTime.now());
        }

        teamClassify.setUserId(user.getId());
        teamClassify.setStatus(YqmDefine.StatusType.effective.getValue());
        teamClassify.setUpdatedBy(user.getId());
        teamClassify.setUpdatedTime(LocalDateTime.now());
        iTpTeamClassifyService.saveOrUpdate(teamClassify);

        return TpTeamClassifyToDTO.toTpTeamClassifyDTO(teamClassify);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TpTeamClassifyDTO getById(String id) {
        TpTeamClassify teamClassify = iTpTeamClassifyService.getById(id);
        return TpTeamClassifyToDTO.toTpTeamClassifyDTO(teamClassify);
    }

    /**
     * 删除团队分类
     * @param id
     * @return
     */
    public String removeTeamClassify(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpTeamClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpTeamClassifyService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * @return
     */
    public String enableTeamClassify(TpTeamClassifyRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用团队分类错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpTeamClassify teamClassify = iTpTeamClassifyService.getById(request.getId());
        if (Objects.isNull(teamClassify)) {
            log.error("操作异常->停用/启用团队分类错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(teamClassify.getStatus())) {
            log.error("操作异常->停用/启用团队分类错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpTeamClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpTeamClassifyService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 团队分类
     * @param request
     * @return
     */
    public IPage<TpTeamClassifyDTO> pageTeamClassify(TpTeamClassifyRequest request) {
        User currentUser = UserInfoService.getUser();
        Page<TpTeamClassify> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpTeamClassifyService.page(page, iTpTeamClassifyService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpTeamClassifyToDTO.toTpTeamClassifyDTOList(list));
        }
        return pageList;
    }

    /**
     * 查询 团队分类
     * @param request
     * @return
     */
    public List<TpTeamClassifyDTO> listTeamClassify(TpTeamClassifyRequest request) {
        User currentUser = UserInfoService.getUser();
        List<TpTeamClassifyDTO> teamClassifyDTOS = new ArrayList<>();

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpTeamClassify> classifyList = iTpTeamClassifyService.list(iTpTeamClassifyService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            teamClassifyDTOS = TpTeamClassifyToDTO.toTpTeamClassifyDTOList(classifyList);
        }
        return teamClassifyDTOS;
    }

}
