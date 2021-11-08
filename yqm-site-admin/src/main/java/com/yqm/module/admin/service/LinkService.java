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
import com.yqm.common.conversion.TpLinkToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpLinkDTO;
import com.yqm.common.entity.TpLink;
import com.yqm.common.request.TpLinkRequest;
import com.yqm.common.service.ITpLinkService;
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
 * 管理端-友情链接
 * @Author: weiximei
 * @Date: 2021/11/7 19:33
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class LinkService {


    private ITpLinkService iTpLinkService;

    public LinkService(ITpLinkService iTpLinkService) {
        this.iTpLinkService = iTpLinkService;
    }

    /**
     * 保存/修改 友情链接分类
     * @param request
     * @return
     */
    public TpLinkDTO saveLink(TpLinkRequest request) {
        User user = UserInfoService.getUser();

        TpLink link = TpLinkToDTO.toTpLink(request);
        if (StringUtils.isEmpty(request.getId())) {
            link.setCreatedBy(user.getId());
            link.setCreatedTime(LocalDateTime.now());

            int maxSort = iTpLinkService.getMaxSort(user.getId());
            iTpLinkService.updateAllSortGal(maxSort,user.getId());
            link.setSort(1);
        }

        link.setUserId(user.getId());
        link.setStatus(YqmDefine.StatusType.effective.getValue());
        link.setUpdatedBy(user.getId());
        link.setUpdatedTime(LocalDateTime.now());
        iTpLinkService.saveOrUpdate(link);

        return TpLinkToDTO.toTpLinkDTO(link);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TpLinkDTO getById(String id) {
        TpLink link = iTpLinkService.getById(id);
        return TpLinkToDTO.toTpLinkDTO(link);
    }

    /**
     * 删除友情链接分类
     * @param id
     * @return
     */
    public String removeLink(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpLink> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpLinkService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * @return
     */
    public String enableLink(TpLinkRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用友情链接错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpLink link = iTpLinkService.getById(request.getId());
        if (Objects.isNull(link)) {
            log.error("操作异常->停用/启用友情链接错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(link.getStatus())) {
            log.error("操作异常->停用/启用友情链接错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpLink> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpLinkService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 友情链接分类
     * @param request
     * @return
     */
    public IPage<TpLinkDTO> pageLink(TpLinkRequest request) {
        Page<TpLink> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpLinkService.page(page, iTpLinkService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpLinkToDTO.toTpLinkDTOList(list));
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

        TpLink tpLink = iTpLinkService.getById(id);
        if (Objects.nonNull(tpLink)) {
            iTpLinkService.updateAllSortGal(tpLink.getSort(), user.getId());
            iTpLinkService.top(id, user.getId());
        }

        return id;
    }

}
