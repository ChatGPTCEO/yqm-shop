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

package com.yqm.module.common.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmOrderLogToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmOrderLogDTO;
import com.yqm.common.entity.YqmOrderLog;
import com.yqm.common.request.YqmOrderLogRequest;
import com.yqm.common.service.IYqmOrderLogService;
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
 * @Author: weiximei
 * @Date: 2022/4/3 15:48
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class OrderLogService {


    private final IYqmOrderLogService iYqmOrderLogService;

    public OrderLogService(IYqmOrderLogService iYqmOrderLogService) {
        this.iYqmOrderLogService = iYqmOrderLogService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmOrderLogDTO> page(YqmOrderLogRequest request) {
        Page<YqmOrderLog> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmOrderLogService.page(page, iYqmOrderLogService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmOrderLogToDTO.toYqmOrderLogDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmOrderLogDTO> list(YqmOrderLogRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmOrderLog> list = iYqmOrderLogService.list(iYqmOrderLogService.getQuery(request));
        return YqmOrderLogToDTO.toYqmOrderLogDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmOrderLogDTO getById(String id) {
        YqmOrderLog entity = iYqmOrderLogService.getById(id);
        return YqmOrderLogToDTO.toYqmOrderLogDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmOrderLogDTO save(YqmOrderLogRequest request) {
        User user = UserInfoService.getUser();
        YqmOrderLog entity = YqmOrderLogToDTO.toYqmOrderLog(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmOrderLogService.saveOrUpdate(entity);
        return YqmOrderLogToDTO.toYqmOrderLogDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmOrderLog brand = iYqmOrderLogService.getById(id);
        if (Objects.isNull(brand)) {
            return id;
        }
        brand.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmOrderLogToDTO.toYqmOrderLogRequest(brand));
        return id;
    }

}
