/*
 *  Copyright  2022 Wei xi mei
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmFreightTemplateToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmFreightTemplateDTO;
import com.yqm.common.entity.YqmFreightTemplate;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmFreightTemplateRequest;
import com.yqm.common.service.IYqmFreightTemplateService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-运费模板
 *
 * @Author: weiximei
 * @Date: 2022/3/6 15:02
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminFreightTemplateService {

    private final IYqmFreightTemplateService iYqmFreightTemplateService;

    public AdminFreightTemplateService(IYqmFreightTemplateService iYqmFreightTemplateService) {
        this.iYqmFreightTemplateService = iYqmFreightTemplateService;
    }


    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmFreightTemplateDTO> page(YqmFreightTemplateRequest request) {
        Page<YqmFreightTemplate> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmFreightTemplateService.page(page, iYqmFreightTemplateService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmFreightTemplateToDTO.toYqmFreightTemplateDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmFreightTemplateDTO> list(YqmFreightTemplateRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmFreightTemplate> list = iYqmFreightTemplateService.list(iYqmFreightTemplateService.getQuery(request));
        return YqmFreightTemplateToDTO.toYqmFreightTemplateDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmFreightTemplateDTO getById(String id) {
        YqmFreightTemplate entity = iYqmFreightTemplateService.getById(id);
        return YqmFreightTemplateToDTO.toYqmFreightTemplateDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmFreightTemplateDTO save(YqmFreightTemplateRequest request) {
        User user = UserInfoService.getUser();
        YqmFreightTemplate entity = YqmFreightTemplateToDTO.toYqmFreightTemplate(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmFreightTemplateService.saveOrUpdate(entity);
        return YqmFreightTemplateToDTO.toYqmFreightTemplateDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmFreightTemplate entity = iYqmFreightTemplateService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmFreightTemplateToDTO.toYqmFreightTemplateRequest(entity));
        return id;
    }

    /**
     * 停用/启用
     *
     * @param id
     * @return
     */
    public String enable(String id) {
        YqmFreightTemplate entity = iYqmFreightTemplateService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        if (YqmDefine.StatusType.delete.getValue().equals(entity.getStatus())) {
            log.error("运费模板->停用/启用 状态异常！[now={}]", entity.getStatus());
            throw new YqmException("状态异常");
        }
        if (YqmDefine.StatusType.effective.getValue().equals(entity.getStatus())) {
            entity.setStatus(YqmDefine.StatusType.failure.getValue());
        } else {
            entity.setStatus(YqmDefine.StatusType.effective.getValue());
        }
        this.save(YqmFreightTemplateToDTO.toYqmFreightTemplateRequest(entity));
        return id;
    }
}
