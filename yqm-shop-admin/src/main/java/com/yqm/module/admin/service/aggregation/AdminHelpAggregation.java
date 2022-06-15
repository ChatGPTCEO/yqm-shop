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

package com.yqm.module.admin.service.aggregation;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmHelpToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmHelpClassificationDTO;
import com.yqm.common.dto.YqmHelpDTO;
import com.yqm.common.dto.YqmUserDTO;
import com.yqm.common.entity.YqmHelp;
import com.yqm.common.request.YqmHelpClassificationRequest;
import com.yqm.common.request.YqmHelpRequest;
import com.yqm.common.request.YqmUserRequest;
import com.yqm.common.service.IYqmHelpService;
import com.yqm.module.admin.service.AdminClientUserService;
import com.yqm.module.admin.service.AdminHelpClassificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2022/6/14 22:33
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminHelpAggregation {

    private final IYqmHelpService iYqmHelpService;
    private final AdminClientUserService adminClientUserService;
    private final AdminHelpClassificationService adminHelpClassificationService;

    public AdminHelpAggregation(IYqmHelpService iYqmHelpService, AdminClientUserService adminClientUserService, AdminHelpClassificationService adminHelpClassificationService) {
        this.iYqmHelpService = iYqmHelpService;
        this.adminClientUserService = adminClientUserService;
        this.adminHelpClassificationService = adminHelpClassificationService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmHelpDTO> page(YqmHelpRequest request) {
        Page<YqmHelp> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmHelpService.page(page, iYqmHelpService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmHelpDTO> projectDTOS = YqmHelpToDTO.toYqmHelpDTOList(pageList.getRecords());
            List<String> idList = projectDTOS.stream().map(e -> e.getClassificationId()).collect(Collectors.toList());

            YqmHelpClassificationRequest classificationRequest = new YqmHelpClassificationRequest();
            classificationRequest.setInIdList(idList);
            List<YqmHelpClassificationDTO> classificationDTOS = adminHelpClassificationService.list(classificationRequest);
            if (CollectionUtils.isNotEmpty(classificationDTOS)) {
                Map<String, YqmHelpClassificationDTO> classificationDTOMap = classificationDTOS.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
                for (YqmHelpDTO projectDTO : projectDTOS) {
                    projectDTO.setHelpClassificationDTO(classificationDTOMap.get(projectDTO.getClassificationId()));
                }
            }

            List<String> userIdList = projectDTOS.stream().map(e -> e.getCreatedBy()).collect(Collectors.toList());
            YqmUserRequest userRequest = new YqmUserRequest();
            userRequest.setInStatusList(userIdList);
            List<YqmUserDTO> userDTOList = adminClientUserService.list(userRequest);
            if (CollectionUtils.isNotEmpty(userDTOList)) {
                Map<String, YqmUserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
                for (YqmHelpDTO projectDTO : projectDTOS) {
                    projectDTO.setUserDTO(userDTOMap.get(projectDTO.getCreatedBy()));
                }
            }


            pageList.setRecords(projectDTOS);
        }
        return pageList;
    }


    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmHelpDTO getById(String id) {
        YqmHelp entity = iYqmHelpService.getById(id);
        YqmHelpClassificationDTO classificationDTO = adminHelpClassificationService.getById(entity.getClassificationId());
        YqmUserDTO userDTO = adminClientUserService.getById(entity.getCreatedBy());


        YqmHelpDTO topicDTO = YqmHelpToDTO.toYqmHelpDTO(entity);
        topicDTO.setHelpClassificationDTO(classificationDTO);
        topicDTO.setUserDTO(userDTO);

        return topicDTO;
    }


}
