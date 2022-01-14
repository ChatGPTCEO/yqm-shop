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
import com.yqm.common.conversion.TpHonorToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpHonorDTO;
import com.yqm.common.entity.TpHonor;
import com.yqm.common.request.TpHonorRequest;
import com.yqm.common.service.ITpHonorService;
import com.yqm.common.service.ITpHonorService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-荣誉证书
 * @Author: weiximei
 * @Date: 2021/11/8 19:54
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HonorService {

    private ITpHonorService iTpHonorService;

    public HonorService(ITpHonorService iTpHonorService) {
        this.iTpHonorService = iTpHonorService;
    }

    /**
     * 保存/修改 荣誉证书分类
     * @param request
     * @return
     */
    public TpHonorDTO saveHonor(TpHonorRequest request) {
        User user = UserInfoService.getUser();

        TpHonor honor = TpHonorToDTO.toTpHonor(request);
        if (StringUtils.isEmpty(request.getId())) {
            honor.setCreatedBy(user.getId());
            honor.setCreatedTime(LocalDateTime.now());

            int maxSort = iTpHonorService.getMaxSort(user.getId());
            iTpHonorService.updateAllSortGal(maxSort,user.getId());
            honor.setSort(1);
        }

        honor.setUserId(user.getId());
        honor.setStatus(YqmDefine.StatusType.effective.getValue());
        honor.setUpdatedBy(user.getId());
        honor.setUpdatedTime(LocalDateTime.now());
        iTpHonorService.saveOrUpdate(honor);

        return TpHonorToDTO.toTpHonorDTO(honor);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TpHonorDTO getById(String id) {
        TpHonor honor = iTpHonorService.getById(id);
        return TpHonorToDTO.toTpHonorDTO(honor);
    }

    /**
     * 删除荣誉证书分类
     * @param id
     * @return
     */
    public String removeHonor(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpHonor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpHonorService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * @return
     */
    public String enableHonor(TpHonorRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用荣誉证书错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpHonor honor = iTpHonorService.getById(request.getId());
        if (Objects.isNull(honor)) {
            log.error("操作异常->停用/启用荣誉证书错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(honor.getStatus())) {
            log.error("操作异常->停用/启用荣誉证书错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpHonor> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpHonorService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 荣誉证书分类
     * @param request
     * @return
     */
    public IPage<TpHonorDTO> pageHonor(TpHonorRequest request) {
        User currentUser = UserInfoService.getUser();
        Page<TpHonor> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpHonorService.page(page, iTpHonorService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpHonorToDTO.toTpHonorDTOList(list));
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

        TpHonor tpHonor = iTpHonorService.getById(id);
        if (Objects.nonNull(tpHonor)) {
            iTpHonorService.updateAllSortGal(tpHonor.getSort(), user.getId());
            iTpHonorService.top(id, user.getId());
        }

        return id;
    }


}
