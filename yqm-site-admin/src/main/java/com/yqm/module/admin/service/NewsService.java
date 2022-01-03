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
import com.yqm.common.conversion.TpNewsToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpNewsDTO;
import com.yqm.common.entity.TpNews;
import com.yqm.common.request.TpNewsRequest;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.service.ITpNewsService;
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
 * 管理端-新闻
 *
 * @Author: weiximei
 * @Date: 2021/11/28 16:32
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class NewsService {

    private ITpNewsService iTpNewsService;

    public NewsService(ITpNewsService iTpNewsService) {
        this.iTpNewsService = iTpNewsService;
    }

    /**
     * 保存/修改 新闻
     *
     * @param request
     * @return
     */
    public TpNewsDTO saveNews(TpNewsRequest request) {
        User user = UserInfoService.getUser();

        TpNews news = TpNewsToDTO.toTpNews(request);
        if (StringUtils.isEmpty(request.getId())) {
            LocalDateTime nowDate = LocalDateTime.now();
            news.setCreateBy(user.getId());
            news.setCreateTime(nowDate);
            news.setReleaseTime(nowDate);
            int maxSort = iTpNewsService.getMaxSort(user.getId());
            iTpNewsService.updateAllSortGal(maxSort, user.getId());
            news.setSort(1);
        }

        news.setUserId(user.getId());
        news.setStatus(YqmDefine.StatusType.effective.getValue());
        news.setUpdatedBy(user.getId());
        news.setUpdatedTime(LocalDateTime.now());
        iTpNewsService.saveOrUpdate(news);

        return TpNewsToDTO.toTpNewsDTO(news);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpNewsDTO getById(String id) {
        TpNews news = iTpNewsService.getById(id);
        return TpNewsToDTO.toTpNewsDTO(news);
    }

    /**
     * 删除新闻
     *
     * @param id
     * @return
     */
    public String removeNews(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpNews> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpNewsService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enableNews(TpNewsRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用新闻错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpNews news = iTpNewsService.getById(request.getId());
        if (Objects.isNull(news)) {
            log.error("操作异常->停用/启用新闻错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(news.getStatus())) {
            log.error("操作异常->停用/启用新闻错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpNews> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpNewsService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 新闻
     *
     * @param request
     * @return
     */
    public IPage<TpNewsDTO> pageNews(TpNewsRequest request) {
        User user = UserInfoService.getUser();
        Page<TpNews> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpNewsService.page(page, iTpNewsService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            List<TpNewsDTO> dtoList = TpNewsToDTO.toTpNewsDTOList(list);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    /**
     * 查询 新闻
     *
     * @param request
     * @return
     */
    public List<TpNewsDTO> listNews(TpNewsRequest request) {
        User user = UserInfoService.getUser();
        List<TpNewsDTO> newsDTOS = new ArrayList<>();

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpNews> classifyList = iTpNewsService.list(iTpNewsService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            newsDTOS = TpNewsToDTO.toTpNewsDTOList(classifyList);
        }
        return newsDTOS;
    }

    /**
     * 置顶
     *
     * @param id
     * @return
     */
    public String top(String id) {
        User user = UserInfoService.getUser();

        TpNews tpNews = iTpNewsService.getById(id);
        if (Objects.nonNull(tpNews)) {
            iTpNewsService.updateAllSortGal(tpNews.getSort(), user.getId());
            iTpNewsService.top(id, user.getId());
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
        UpdateWrapper<TpNews> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        updateWrapper.set("seo_title", request.getSeoTitle());
        updateWrapper.set("seo_keyword", request.getSeoKeyword());
        updateWrapper.set("seo_content", request.getSeoContent());
        updateWrapper.set("plug_code", request.getPlugCode());
        updateWrapper.set("plug_location", request.getPlugLocation());
        iTpNewsService.update(updateWrapper);

        return request.getId();
    }

}
