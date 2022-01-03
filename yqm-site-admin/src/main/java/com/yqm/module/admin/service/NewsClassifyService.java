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
import com.yqm.common.conversion.TpNewsClassifyToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpNewsClassifyDTO;
import com.yqm.common.entity.TpNewsClassify;
import com.yqm.common.request.TpNewsClassifyRequest;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.service.ITpNewsClassifyService;
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
 * 管理端-新闻分类
 *
 * @Author: weiximei
 * @Date: 2021/11/7 17:30
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class NewsClassifyService {

    private ITpNewsClassifyService iTpNewsClassifyService;

    public NewsClassifyService(ITpNewsClassifyService iTpNewsClassifyService) {
        this.iTpNewsClassifyService = iTpNewsClassifyService;
    }

    /**
     * 保存/修改 新闻分类
     *
     * @param request
     * @return
     */
    public TpNewsClassifyDTO saveNewsClassify(TpNewsClassifyRequest request) {
        User user = UserInfoService.getUser();

        TpNewsClassify newsClassify = TpNewsClassifyToDTO.toTpNewsClassify(request);
        if (StringUtils.isEmpty(request.getId())) {
            newsClassify.setCreateBy(user.getId());
            newsClassify.setCreateTime(LocalDateTime.now());

            int maxSort = iTpNewsClassifyService.getMaxSort(user.getId());
            iTpNewsClassifyService.updateAllSortGal(maxSort, user.getId());
            newsClassify.setSort(1);
        }

        TpNewsClassify tpNewsClassify = iTpNewsClassifyService.getById(request.getPid());
        if (Objects.isNull(tpNewsClassify)) {
            newsClassify.setPid("-1");
        }

        newsClassify.setUserId(user.getId());
        newsClassify.setStatus(YqmDefine.StatusType.effective.getValue());
        newsClassify.setUpdatedBy(user.getId());
        newsClassify.setUpdatedTime(LocalDateTime.now());
        iTpNewsClassifyService.saveOrUpdate(newsClassify);

        return TpNewsClassifyToDTO.toTpNewsClassifyDTO(newsClassify);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpNewsClassifyDTO getById(String id) {
        TpNewsClassify newsClassify = iTpNewsClassifyService.getById(id);
        return TpNewsClassifyToDTO.toTpNewsClassifyDTO(newsClassify);
    }

    /**
     * 删除新闻分类
     *
     * @param id
     * @return
     */
    public String removeNewsClassify(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpNewsClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpNewsClassifyService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enableNewsClassify(TpNewsClassifyRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用新闻分类错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpNewsClassify newsClassify = iTpNewsClassifyService.getById(request.getId());
        if (Objects.isNull(newsClassify)) {
            log.error("操作异常->停用/启用新闻分类错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(newsClassify.getStatus())) {
            log.error("操作异常->停用/启用新闻分类错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpNewsClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpNewsClassifyService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 新闻分类
     *
     * @param request
     * @return
     */
    public IPage<TpNewsClassifyDTO> pageNewsClassify(TpNewsClassifyRequest request) {
        User user = UserInfoService.getUser();
        Page<TpNewsClassify> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setPid("-1");
        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpNewsClassifyService.page(page, iTpNewsClassifyService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            List<TpNewsClassifyDTO> dtoList = TpNewsClassifyToDTO.toTpNewsClassifyDTOList(list);
            pageList.setRecords(this.getNewsClassify(dtoList));

        }
        return pageList;
    }

    /**
     * 分页查询 新闻分类
     * 直接列表形式
     *
     * @param request
     * @return
     */
    public IPage<TpNewsClassifyDTO> pageNewsClassifyList(TpNewsClassifyRequest request) {
        User user = UserInfoService.getUser();
        Page<TpNewsClassify> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpNewsClassifyService.page(page, iTpNewsClassifyService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            List<TpNewsClassifyDTO> dtoList = TpNewsClassifyToDTO.toTpNewsClassifyDTOList(list);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    /**
     * 查询 新闻分类
     *
     * @param request
     * @return
     */
    public List<TpNewsClassifyDTO> listNewsClassify(TpNewsClassifyRequest request) {
        User user = UserInfoService.getUser();
        List<TpNewsClassifyDTO> newsClassifyDTOS = new ArrayList<>();

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpNewsClassify> classifyList = iTpNewsClassifyService.list(iTpNewsClassifyService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            newsClassifyDTOS = TpNewsClassifyToDTO.toTpNewsClassifyDTOList(classifyList);
        }
        return newsClassifyDTOS;
    }

    /**
     * 根据pid查询 新闻分类
     *
     * @param pid
     * @return
     */
    public List<TpNewsClassifyDTO> listNewsClassify(String pid) {
        User user = UserInfoService.getUser();
        TpNewsClassifyRequest request = new TpNewsClassifyRequest();
        request.setPid(pid);
        request.setUserId(user.getId());
        return TpNewsClassifyToDTO.toTpNewsClassifyDTOList(TpNewsClassifyToDTO.toTpNewsClassifyList(this.listNewsClassify(request)));
    }

    /**
     * 查询 新闻分类
     *
     * @return
     */
    public List<TpNewsClassifyDTO> getNewsClassify(List<TpNewsClassifyDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        for (TpNewsClassifyDTO dto : dtoList) {
            List<TpNewsClassifyDTO> DtoList2 = this.listNewsClassify(dto.getId());
            dto.setChildren(DtoList2);
            if (CollectionUtils.isEmpty(DtoList2)) {
                continue;
            }
            for (TpNewsClassifyDTO dto3 : DtoList2) {
                List<TpNewsClassifyDTO> areaDtoList = this.listNewsClassify(dto3.getId());
                dto3.setChildren(areaDtoList);
            }

        }
        return dtoList;
    }

    /**
     * 查询 新闻分类 梯形
     *
     * @return
     */
    public List<TpNewsClassifyDTO> getNewsClassify() {
        List<TpNewsClassifyDTO> dtoList = this.listNewsClassify("-1");
        if (CollectionUtils.isEmpty(dtoList)) {
            return new ArrayList<>();
        }
        for (TpNewsClassifyDTO dto : dtoList) {
            List<TpNewsClassifyDTO> DtoList2 = this.listNewsClassify(dto.getId());
            dto.setChildren(DtoList2);
            if (CollectionUtils.isEmpty(DtoList2)) {
                continue;
            }
            for (TpNewsClassifyDTO dto3 : DtoList2) {
                List<TpNewsClassifyDTO> areaDtoList = this.listNewsClassify(dto3.getId());
                dto3.setChildren(areaDtoList);
            }

        }
        return dtoList;
    }

    /**
     * 置顶
     *
     * @param id
     * @return
     */
    public String top(String id) {
        User user = UserInfoService.getUser();

        TpNewsClassify tpNewsClassify = iTpNewsClassifyService.getById(id);
        if (Objects.nonNull(tpNewsClassify)) {
            iTpNewsClassifyService.updateAllSortGal(tpNewsClassify.getSort(), user.getId());
            iTpNewsClassifyService.top(id, user.getId());
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
        UpdateWrapper<TpNewsClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        updateWrapper.set("seo_title", request.getSeoTitle());
        updateWrapper.set("seo_keyword", request.getSeoKeyword());
        updateWrapper.set("seo_content", request.getSeoContent());
        updateWrapper.set("plug_code", request.getPlugCode());
        updateWrapper.set("plug_location", request.getPlugLocation());
        iTpNewsClassifyService.update(updateWrapper);

        return request.getId();
    }

}
