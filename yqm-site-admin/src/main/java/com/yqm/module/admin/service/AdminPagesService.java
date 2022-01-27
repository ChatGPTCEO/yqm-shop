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
import com.yqm.common.conversion.TpPagesToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpPagesDTO;
import com.yqm.common.entity.TpPages;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.service.ITpPagesService;
import com.yqm.module.service.PagesCommonService;
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
import java.util.stream.Collectors;

/**
 * 管理端-页面
 *
 * @Author: weiximei
 * @Date: 2021/11/28 16:32
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AdminPagesService {

    private final ITpPagesService iTpPagesService;
    private final PagesCommonService pagesCommonService;

    public AdminPagesService(ITpPagesService iTpPagesService, PagesCommonService pagesCommonService) {
        this.iTpPagesService = iTpPagesService;
        this.pagesCommonService = pagesCommonService;
    }

    /**
     * 保存/修改 页面
     *
     * @param request
     * @return
     */
    public TpPagesDTO savePages(TpPagesRequest request) {
        User user = UserInfoService.getUser();

        TpPages pages = TpPagesToDTO.toTpPages(request);
        if (StringUtils.isEmpty(request.getId())) {
            LocalDateTime nowDate = LocalDateTime.now();
            pages.setCreateBy(user.getId());
            pages.setCreateTime(nowDate);
            int maxSort = iTpPagesService.getMaxSort(user.getId());
            iTpPagesService.updateAllSortGal(maxSort, user.getId());
            pages.setSort(1);
        }

        pages.setUserId(user.getId());
        pages.setStatus(YqmDefine.StatusType.effective.getValue());
        pages.setUpdatedBy(user.getId());
        pages.setUpdatedTime(LocalDateTime.now());
        iTpPagesService.saveOrUpdate(pages);

        return TpPagesToDTO.toTpPagesDTO(pages);
    }

    /**
     * 批量 保存/修改 页面
     *
     * @param requestList
     * @return
     */
    public List<TpPagesDTO> saveBachPages(List<TpPagesRequest> requestList) {
        User user = UserInfoService.getUser();

        if (CollectionUtils.isEmpty(requestList)) {
            return null;
        }
        List<TpPages> pagesEntityList = requestList.stream().map(e -> {
            TpPages pages = TpPagesToDTO.toTpPages(e);
            if (StringUtils.isEmpty(e.getId())) {
                LocalDateTime nowDate = LocalDateTime.now();
                pages.setCreateBy(user.getId());
                pages.setCreateTime(nowDate);
                int maxSort = iTpPagesService.getMaxSort(user.getId());
                iTpPagesService.updateAllSortGal(maxSort, user.getId());
                pages.setSort(1);
            }

            pages.setUserId(user.getId());
            pages.setStatus(YqmDefine.StatusType.effective.getValue());
            pages.setUpdatedBy(user.getId());
            pages.setUpdatedTime(LocalDateTime.now());

            return pages;
        }).collect(Collectors.toList());

        boolean bool = iTpPagesService.saveBatch(pagesEntityList);
        if (false == bool) {
            log.error("批量 保存/修改 页面! [userId={}, pagesRequest={}]", user.getId(), requestList);
            throw new YqmException("处理页面失败");
        }

        return TpPagesToDTO.toTpPagesDTOList(pagesEntityList);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpPagesDTO getById(String id) {
        return pagesCommonService.getById(id);
    }

    /**
     * 删除页面
     *
     * @param id
     * @return
     */
    public String removePages(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpPages> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpPagesService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enablePages(TpPagesRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用页面错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpPages pages = iTpPagesService.getById(request.getId());
        if (Objects.isNull(pages)) {
            log.error("操作异常->停用/启用页面错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(pages.getStatus())) {
            log.error("操作异常->停用/启用页面错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpPages> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpPagesService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 页面导航
     *
     * @param request
     * @return
     */
    public IPage<TpPagesDTO> pagePagesNavigator(TpPagesRequest request) {
        request.setPageType(YqmDefine.PageType.navigation.getValue());
        request.setPageBelongs(YqmDefine.PageBelongsType.system.getValue());
        return this.pagePages(request);
    }

    /**
     * 分页查询 页面
     *
     * @param request
     * @return
     */
    public IPage<TpPagesDTO> pagePages(TpPagesRequest request) {
        User user = UserInfoService.getUser();
        Page<TpPages> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpPagesService.page(page, iTpPagesService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            List<TpPagesDTO> dtoList = TpPagesToDTO.toTpPagesDTOList(list);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    /**
     * 查询 页面
     *
     * @param request
     * @return
     */
    public List<TpPagesDTO> listPages(TpPagesRequest request) {
        User user = UserInfoService.getUser();

        request.setUserId(user.getId());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));

        return pagesCommonService.baseListPages(request);
    }

    /**
     * 置顶
     *
     * @param id
     * @return
     */
    public String top(String id) {
        User user = UserInfoService.getUser();

        TpPages tpPages = iTpPagesService.getById(id);
        if (Objects.nonNull(tpPages)) {
            iTpPagesService.updateAllSortGal(tpPages.getSort(), user.getId());
            iTpPagesService.top(id, user.getId());
        }

        return id;
    }

    /**
     * 更新 seo
     *
     * @param request
     * @return
     */
    public String updateSEO(TpPagesRequest request) {
        User user = UserInfoService.getUser();
        request.setUserId(user.getId());
        return pagesCommonService.updateSEO(request);
    }

}
