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

package com.yqm.module.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yqm.common.conversion.TpPagesToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpPagesDTO;
import com.yqm.common.entity.TpPages;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.service.ITpPagesService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author: weiximei
 * @Date: 2022/1/14 21:11
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
public class PagesCommonService {

    private ITpPagesService iTpPagesService;

    public PagesCommonService(ITpPagesService iTpPagesService) {
        this.iTpPagesService = iTpPagesService;
    }


    /**
     * 查询 页面
     *
     * @param request
     * @return
     */
    public List<TpPagesDTO> baseListPages(TpPagesRequest request) {
        List<TpPagesDTO> pagesDTOS = new ArrayList<>();
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpPages> classifyList = iTpPagesService.list(iTpPagesService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            pagesDTOS = TpPagesToDTO.toTpPagesDTOList(classifyList);
        }
        return pagesDTOS;
    }

    /**
     * 更新 seo
     *
     * @param request
     * @return
     */
    public String updateSEO(TpPagesRequest request) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpPages> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        updateWrapper.set("seo_title", request.getSeoTitle());
        updateWrapper.set("seo_keyword", request.getSeoKeyword());
        updateWrapper.set("seo_content", request.getSeoContent());
        updateWrapper.set("plug_code", request.getPlugCode());
        updateWrapper.set("plug_location", request.getPlugLocation());
        iTpPagesService.update(updateWrapper);

        return request.getId();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpPagesDTO getById(String id) {
        TpPages pages = iTpPagesService.getById(id);
        return TpPagesToDTO.toTpPagesDTO(pages);
    }

    /**
     * 查询一条数据
     *
     * @param request
     * @return
     */
    public TpPagesDTO getOne(TpPagesRequest request) {
        TpPages pages = iTpPagesService.getOne(iTpPagesService.queryWrapper(request));
        return TpPagesToDTO.toTpPagesDTO(pages);
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
            pages.setSort(maxSort + 1);
        }

        TpPages pagesPidEntity = iTpPagesService.getById(request.getPid());
        if (Objects.isNull(pagesPidEntity)) {
            pages.setPid("-1");
            pages.setPids("-1");
        }

        pages.setUserId(user.getId());
        pages.setStatus(YqmDefine.StatusType.effective.getValue());
        pages.setUpdatedBy(user.getId());
        pages.setUpdatedTime(LocalDateTime.now());
        iTpPagesService.saveOrUpdate(pages);

        return TpPagesToDTO.toTpPagesDTO(pages);
    }

}
