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
import com.yqm.common.conversion.TpPartnersToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpPartnersDTO;
import com.yqm.common.entity.TpPartners;
import com.yqm.common.request.TpPartnersRequest;
import com.yqm.common.service.ITpPartnersService;
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
 * 管理端-合作伙伴
 * @Author: weiximei
 * @Date: 2021/11/7 19:33
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class PartnersService {


    private ITpPartnersService iTpPartnersService;

    public PartnersService(ITpPartnersService iTpPartnersService) {
        this.iTpPartnersService = iTpPartnersService;
    }

    /**
     * 保存/修改 合作伙伴分类
     * @param request
     * @return
     */
    public TpPartnersDTO savePartners(TpPartnersRequest request) {
        User user = UserInfoService.getUser();

        TpPartners partners = TpPartnersToDTO.toTpPartners(request);
        if (StringUtils.isEmpty(request.getId())) {
            partners.setCreatedBy(user.getId());
            partners.setCreatedTime(LocalDateTime.now());

            int maxSort = iTpPartnersService.getMaxSort(user.getId());
            iTpPartnersService.updateAllSortGal(maxSort,user.getId());
            partners.setSort(1);
        }

        partners.setUserId(user.getId());
        partners.setStatus(YqmDefine.StatusType.effective.getValue());
        partners.setUpdatedBy(user.getId());
        partners.setUpdatedTime(LocalDateTime.now());
        iTpPartnersService.saveOrUpdate(partners);

        return TpPartnersToDTO.toTpPartnersDTO(partners);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TpPartnersDTO getById(String id) {
        TpPartners partners = iTpPartnersService.getById(id);
        return TpPartnersToDTO.toTpPartnersDTO(partners);
    }

    /**
     * 删除合作伙伴分类
     * @param id
     * @return
     */
    public String removePartners(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpPartners> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpPartnersService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * @return
     */
    public String enablePartners(TpPartnersRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用合作伙伴错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpPartners partners = iTpPartnersService.getById(request.getId());
        if (Objects.isNull(partners)) {
            log.error("操作异常->停用/启用合作伙伴错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(partners.getStatus())) {
            log.error("操作异常->停用/启用合作伙伴错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpPartners> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpPartnersService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 合作伙伴分类
     * @param request
     * @return
     */
    public IPage<TpPartnersDTO> pagePartners(TpPartnersRequest request) {
        User currentUser = UserInfoService.getUser();
        Page<TpPartners> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(currentUser.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpPartnersService.page(page, iTpPartnersService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpPartnersToDTO.toTpPartnersDTOList(list));
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

        TpPartners tpPartners = iTpPartnersService.getById(id);
        if (Objects.nonNull(tpPartners)) {
            iTpPartnersService.updateAllSortGal(tpPartners.getSort(), user.getId());
            iTpPartnersService.top(id, user.getId());
        }

        return id;
    }

}
