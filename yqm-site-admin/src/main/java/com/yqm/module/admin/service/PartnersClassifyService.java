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
import com.yqm.common.conversion.TpPartnersClassifyToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpPartnersClassifyDTO;
import com.yqm.common.entity.TpPartnersClassify;
import com.yqm.common.request.TpPartnersClassifyRequest;
import com.yqm.common.service.ITpPartnersClassifyService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-合作伙伴分类
 * @Author: weiximei
 * @Date: 2021/11/7 17:30
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class PartnersClassifyService {

    private ITpPartnersClassifyService iTpPartnersClassifyService;

    public PartnersClassifyService(ITpPartnersClassifyService iTpPartnersClassifyService) {
        this.iTpPartnersClassifyService = iTpPartnersClassifyService;
    }

    /**
     * 保存/修改 合作伙伴分类
     * @param request
     * @return
     */
    public TpPartnersClassifyDTO savePartnersClassify(TpPartnersClassifyRequest request) {
        User user = UserInfoService.getUser();

        TpPartnersClassify partnersClassify = TpPartnersClassifyToDTO.toTpPartnersClassify(request);
        if (StringUtils.isEmpty(request.getId())) {
            partnersClassify.setCreateBy(user.getId());
            partnersClassify.setCreateTime(LocalDateTime.now());
        }

        partnersClassify.setUserId(user.getId());
        partnersClassify.setStatus(YqmDefine.StatusType.effective.getValue());
        partnersClassify.setUpdatedBy(user.getId());
        partnersClassify.setUpdatedTime(LocalDateTime.now());
        iTpPartnersClassifyService.saveOrUpdate(partnersClassify);

        return TpPartnersClassifyToDTO.toTpPartnersClassifyDTO(partnersClassify);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TpPartnersClassifyDTO getById(String id) {
        TpPartnersClassify partnersClassify = iTpPartnersClassifyService.getById(id);
        return TpPartnersClassifyToDTO.toTpPartnersClassifyDTO(partnersClassify);
    }

    /**
     * 删除合作伙伴分类
     * @param id
     * @return
     */
    public String removePartnersClassify(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpPartnersClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpPartnersClassifyService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * @return
     */
    public String enablePartnersClassify(TpPartnersClassifyRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用合作伙伴分类错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpPartnersClassify partnersClassify = iTpPartnersClassifyService.getById(request.getId());
        if (Objects.isNull(partnersClassify)) {
            log.error("操作异常->停用/启用合作伙伴分类错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(partnersClassify.getStatus())) {
            log.error("操作异常->停用/启用合作伙伴分类错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpPartnersClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpPartnersClassifyService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 合作伙伴分类
     * @param request
     * @return
     */
    public IPage<TpPartnersClassifyDTO> pagePartnersClassify(TpPartnersClassifyRequest request) {
        User currentUser = UserInfoService.getUser();
        Page<TpPartnersClassify> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpPartnersClassifyService.page(page, iTpPartnersClassifyService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpPartnersClassifyToDTO.toTpPartnersClassifyDTOList(list));
        }
        return pageList;
    }

    /**
     * 查询 合作伙伴分类
     * @param request
     * @return
     */
    public List<TpPartnersClassifyDTO> listPartnersClassify(TpPartnersClassifyRequest request) {
        User currentUser = UserInfoService.getUser();
        List<TpPartnersClassifyDTO> partnersClassifyDTOS = new ArrayList<>();

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpPartnersClassify> classifyList = iTpPartnersClassifyService.list(iTpPartnersClassifyService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            partnersClassifyDTOS = TpPartnersClassifyToDTO.toTpPartnersClassifyDTOList(classifyList);
        }
        return partnersClassifyDTOS;
    }

}
