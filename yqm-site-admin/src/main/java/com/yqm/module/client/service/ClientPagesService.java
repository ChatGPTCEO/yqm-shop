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
     * 查询 导航
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
        request.setPageBelongs(YqmDefine.PageBelongsType.system.getValue());
        request.setUserId(user.getId());
        return pagesCommonService.baseListPages(request);
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


}
