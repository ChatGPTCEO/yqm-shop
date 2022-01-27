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

package com.yqm.module.client.service;

import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpPagesDTO;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.service.ITpPagesService;
import com.yqm.module.service.PagesCommonService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2022/1/14 21:06
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
public class ClientPagesService {

    private final ITpPagesService iTpPagesService;
    private final PagesCommonService pagesCommonService;

    public ClientPagesService(ITpPagesService iTpPagesService, PagesCommonService pagesCommonService) {
        this.iTpPagesService = iTpPagesService;
        this.pagesCommonService = pagesCommonService;
    }


    /**
     * 查询 导航集合
     *
     * @param request
     * @return
     */
    public List<TpPagesDTO> listNavigation(TpPagesRequest request) {
        if (StringUtils.isEmpty(request.getSiteId())) {
            return new ArrayList<>();
        }
        User user = UserInfoService.getUser();
        request.setPageType(YqmDefine.PageType.navigation.getValue());
        request.setUserId(user.getId());
        return pagesCommonService.baseListPages(request);
    }

    /**
     * 查询 导航集合 梯子型
     *
     * @return
     */
    public List<TpPagesDTO> listNavigationChildren() {
        User user = UserInfoService.getUser();
        TpPagesRequest request = new TpPagesRequest();
        request.setPageType(YqmDefine.PageType.navigation.getValue());
        request.setUserId(user.getId());
        request.setPid("-1");

        List<TpPagesDTO> pagesDTOList = pagesCommonService.baseListPages(request);
        return this.navigationChildren(pagesDTOList);
    }

    /**
     * 查询 导航集合 id查询
     *
     * @return
     */
    public List<TpPagesDTO> navigationChildren(List<TpPagesDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        for (TpPagesDTO dto : list) {
            List<TpPagesDTO> dtoList2 = this.navigationPid(dto.getId());
            dto.setChildren(dtoList2);
            if (CollectionUtils.isEmpty(dtoList2)) {
                continue;
            }
            for (TpPagesDTO dto3 : dtoList2) {
                List<TpPagesDTO> areaDtoList = this.navigationPid(dto3.getId());
                dto3.setChildren(areaDtoList);
            }

        }

        return list;
    }

    /**
     * 查询 导航集合 id查询
     *
     * @return
     */
    public List<TpPagesDTO> navigationPid(String pid) {
        if (StringUtils.isEmpty(pid)) {
            return null;
        }

        User user = UserInfoService.getUser();
        TpPagesRequest request = new TpPagesRequest();
        request.setPid(pid);
        request.setUserId(user.getId());
        List<TpPagesDTO> pagesDTOList = pagesCommonService.baseListPages(request);
        return CollectionUtils.isNotEmpty(pagesDTOList) ? pagesDTOList : null;
    }

    /**
     * 查询 导航
     *
     * @return
     */
    public TpPagesDTO navigationInfo(String id, String siteId) {

        if (StringUtils.isEmpty(siteId) || StringUtils.isEmpty(id)) {
            return null;
        }
        User user = UserInfoService.getUser();
        TpPagesRequest request = new TpPagesRequest();
        request.setId(id);
        request.setSiteId(siteId);
        request.setPageType(YqmDefine.PageType.navigation.getValue());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        request.setUserId(user.getId());
        return pagesCommonService.getOne(request);
    }

    /**
     * 保存/修改 导航栏
     *
     * @param request
     * @return
     */
    public TpPagesDTO saveNavigation(TpPagesRequest request) {
        request.setPageType(YqmDefine.PageType.navigation.getValue());
        request.setPageBelongs(YqmDefine.PageBelongsType.user.getValue());
        return pagesCommonService.savePages(request);
    }

    /**
     * 查询系统页面
     * 排除导航
     *
     * @param request
     * @return
     */
    public List<TpPagesDTO> getSystemPages(String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            return new ArrayList<>();
        }
        User user = UserInfoService.getUser();
        TpPagesRequest request = new TpPagesRequest();
        request.setSiteId(siteId);
        request.setUserId(user.getId());
        request.setPageBelongs(YqmDefine.PageBelongsType.system.getValue());
        request.setIsNullPageType(Boolean.TRUE);
        return pagesCommonService.baseListPages(request);
    }

}
