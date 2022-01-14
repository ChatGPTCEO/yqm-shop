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
import com.yqm.common.conversion.TpHonorClassifyToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpHonorClassifyDTO;
import com.yqm.common.entity.TpHonorClassify;
import com.yqm.common.request.TpHonorClassifyRequest;
import com.yqm.common.service.ITpHonorClassifyService;
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
 * 管理端-荣誉证书分类
 * 
 * @Author: weiximei
 * @Date: 2021/11/8 20:06
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class HonorClassifyService {

    private ITpHonorClassifyService iTpHonorClassifyService;

    public HonorClassifyService(ITpHonorClassifyService iTpHonorClassifyService) {
        this.iTpHonorClassifyService = iTpHonorClassifyService;
    }

    /**
     * 保存/修改 荣誉证书分类
     * 
     * @param request
     * @return
     */
    public TpHonorClassifyDTO saveHonorClassify(TpHonorClassifyRequest request) {
        User user = UserInfoService.getUser();

        TpHonorClassify honorClassify = TpHonorClassifyToDTO.toTpHonorClassify(request);
        if (StringUtils.isEmpty(request.getId())) {
            honorClassify.setCreatedBy(user.getId());
            honorClassify.setCreatedTime(LocalDateTime.now());
        }

        honorClassify.setUserId(user.getId());
        honorClassify.setStatus(YqmDefine.StatusType.effective.getValue());
        honorClassify.setUpdatedBy(user.getId());
        honorClassify.setUpdatedTime(LocalDateTime.now());
        iTpHonorClassifyService.saveOrUpdate(honorClassify);

        return TpHonorClassifyToDTO.toTpHonorClassifyDTO(honorClassify);
    }

    /**
     * 根据id查询
     * 
     * @param id
     * @return
     */
    public TpHonorClassifyDTO getById(String id) {
        TpHonorClassify honorClassify = iTpHonorClassifyService.getById(id);
        return TpHonorClassifyToDTO.toTpHonorClassifyDTO(honorClassify);
    }

    /**
     * 删除荣誉证书分类
     * 
     * @param id
     * @return
     */
    public String removeHonorClassify(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpHonorClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpHonorClassifyService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * 
     * @return
     */
    public String enableHonorClassify(TpHonorClassifyRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用荣誉证书分类错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpHonorClassify honorClassify = iTpHonorClassifyService.getById(request.getId());
        if (Objects.isNull(honorClassify)) {
            log.error("操作异常->停用/启用荣誉证书分类错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(honorClassify.getStatus())) {
            log.error("操作异常->停用/启用荣誉证书分类错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpHonorClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpHonorClassifyService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 荣誉证书分类
     * 
     * @param request
     * @return
     */
    public IPage<TpHonorClassifyDTO> pageHonorClassify(TpHonorClassifyRequest request) {
        User currentUser = UserInfoService.getUser();
        Page<TpHonorClassify> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpHonorClassifyService.page(page, iTpHonorClassifyService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpHonorClassifyToDTO.toTpHonorClassifyDTOList(list));
        }
        return pageList;
    }

    /**
     * 查询 荣誉证书分类
     * 
     * @param request
     * @return
     */
    public List<TpHonorClassifyDTO> listHonorClassify(TpHonorClassifyRequest request) {
        User currentUser = UserInfoService.getUser();
        List<TpHonorClassifyDTO> honorClassifyDTOS = new ArrayList<>();

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpHonorClassify> classifyList = iTpHonorClassifyService
                .list(iTpHonorClassifyService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            honorClassifyDTOS = TpHonorClassifyToDTO.toTpHonorClassifyDTOList(classifyList);
        }
        return honorClassifyDTOS;
    }

}
