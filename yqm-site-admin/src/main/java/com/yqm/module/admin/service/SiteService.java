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
import com.yqm.common.conversion.TpSiteToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpSiteDTO;
import com.yqm.common.entity.TpSite;
import com.yqm.common.request.TpSiteRequest;
import com.yqm.common.service.ITpSiteService;
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
 * 管理端-站点
 *
 * @Author: weiximei
 * @Date: 2021/11/28 16:32
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class SiteService {

    private ITpSiteService iTpSiteService;

    public SiteService(ITpSiteService iTpSiteService) {
        this.iTpSiteService = iTpSiteService;
    }

    /**
     * 保存/修改 站点
     *
     * @param request
     * @return
     */
    public TpSiteDTO saveSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();

        TpSite site = TpSiteToDTO.toTpSite(request);
        if (StringUtils.isEmpty(request.getId())) {
            LocalDateTime nowDate = LocalDateTime.now();
            site.setCreateBy(user.getId());
            site.setCreateTime(nowDate);
            int maxSort = iTpSiteService.getMaxSort(user.getId());
            iTpSiteService.updateAllSortGal(maxSort, user.getId());
            site.setSort(1);
        }

        site.setUserId(user.getId());
        site.setStatus(YqmDefine.StatusType.effective.getValue());
        site.setUpdatedBy(user.getId());
        site.setUpdatedTime(LocalDateTime.now());
        iTpSiteService.saveOrUpdate(site);

        return TpSiteToDTO.toTpSiteDTO(site);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpSiteDTO getById(String id) {
        TpSite site = iTpSiteService.getById(id);
        return TpSiteToDTO.toTpSiteDTO(site);
    }

    /**
     * 删除站点
     *
     * @param id
     * @return
     */
    public String removeSite(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpSite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpSiteService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enableSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用站点错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpSite site = iTpSiteService.getById(request.getId());
        if (Objects.isNull(site)) {
            log.error("操作异常->停用/启用站点错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(site.getStatus())) {
            log.error("操作异常->停用/启用站点错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpSite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpSiteService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 站点
     *
     * @param request
     * @return
     */
    public IPage<TpSiteDTO> pageSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();
        Page<TpSite> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpSiteService.page(page, iTpSiteService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            List<TpSiteDTO> dtoList = TpSiteToDTO.toTpSiteDTOList(list);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    /**
     * 查询 站点
     *
     * @param request
     * @return
     */
    public List<TpSiteDTO> listSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();
        List<TpSiteDTO> siteDTOS = new ArrayList<>();

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpSite> classifyList = iTpSiteService.list(iTpSiteService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            siteDTOS = TpSiteToDTO.toTpSiteDTOList(classifyList);
        }
        return siteDTOS;
    }

    /**
     * 置顶
     *
     * @param id
     * @return
     */
    public String top(String id) {
        User user = UserInfoService.getUser();

        TpSite tpSite = iTpSiteService.getById(id);
        if (Objects.nonNull(tpSite)) {
            iTpSiteService.updateAllSortGal(tpSite.getSort(), user.getId());
            iTpSiteService.top(id, user.getId());
        }

        return id;
    }


}
