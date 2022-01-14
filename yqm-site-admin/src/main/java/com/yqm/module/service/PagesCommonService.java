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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

}
