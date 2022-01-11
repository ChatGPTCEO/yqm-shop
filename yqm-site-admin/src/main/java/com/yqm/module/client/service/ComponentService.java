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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.TpComponentToDTO;
import com.yqm.common.dto.TpComponentDTO;
import com.yqm.common.entity.TpComponent;
import com.yqm.common.request.TpComponentRequest;
import com.yqm.common.service.ITpComponentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2022/1/11 20:57
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
public class ComponentService {

    private final ITpComponentService iTpComponentService;

    public ComponentService(ITpComponentService iTpComponentService) {
        this.iTpComponentService = iTpComponentService;
    }

    /**
     * 查询模块
     *
     * @param request
     * @return
     */
    public List<TpComponentDTO> getComponentList(TpComponentRequest request) {
        List<TpComponent> componentList = iTpComponentService.list(iTpComponentService.queryWrapper(request));
        return TpComponentToDTO.toTpComponentDTOList(componentList);
    }

    /**
     * 查询模块 分页
     *
     * @param request
     * @return
     */
    public IPage<TpComponentDTO> getComponentPage(TpComponentRequest request) {
        Page<TpComponent> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());
        IPage pageList = iTpComponentService.page(page, iTpComponentService.queryWrapper(request));
        List<TpComponent> list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpComponentToDTO.toTpComponentDTOList(list));
        }
        return pageList;
    }
}
