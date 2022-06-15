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
import com.yqm.common.conversion.YqmTopicToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmTopicClassificationDTO;
import com.yqm.common.dto.YqmTopicDTO;
import com.yqm.common.dto.YqmUserDTO;
import com.yqm.common.entity.YqmTopic;
import com.yqm.common.request.YqmTopicClassificationRequest;
import com.yqm.common.request.YqmTopicRequest;
import com.yqm.common.request.YqmUserRequest;
import com.yqm.common.service.IYqmTopicService;
import com.yqm.module.admin.service.AdminClientUserService;
import com.yqm.module.admin.service.AdminTopicClassificationService;
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
public class AdminTopicAggregation {

    private final IYqmTopicService iYqmTopicService;
    private final AdminClientUserService adminClientUserService;
    private final AdminTopicClassificationService adminTopicClassificationService;

    public AdminTopicAggregation(IYqmTopicService iYqmTopicService, AdminClientUserService adminClientUserService, AdminTopicClassificationService adminTopicClassificationService) {
        this.iYqmTopicService = iYqmTopicService;
        this.adminClientUserService = adminClientUserService;
        this.adminTopicClassificationService = adminTopicClassificationService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmTopicDTO> page(YqmTopicRequest request) {
        Page<YqmTopic> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmTopicService.page(page, iYqmTopicService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmTopicDTO> projectDTOS = YqmTopicToDTO.toYqmTopicDTOList(pageList.getRecords());
            List<String> idList = projectDTOS.stream().map(e -> e.getClassificationId()).collect(Collectors.toList());

            YqmTopicClassificationRequest classificationRequest = new YqmTopicClassificationRequest();
            classificationRequest.setInIdList(idList);
            List<YqmTopicClassificationDTO> classificationDTOS = adminTopicClassificationService.list(classificationRequest);
            if (CollectionUtils.isNotEmpty(classificationDTOS)) {
                Map<String, YqmTopicClassificationDTO> classificationDTOMap = classificationDTOS.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
                for (YqmTopicDTO projectDTO : projectDTOS) {
                    projectDTO.setTopicClassificationDTO(classificationDTOMap.get(projectDTO.getClassificationId()));
                }
            }

            List<String> userIdList = projectDTOS.stream().map(e -> e.getCreatedBy()).collect(Collectors.toList());
            YqmUserRequest userRequest = new YqmUserRequest();
            userRequest.setInStatusList(userIdList);
            List<YqmUserDTO> userDTOList = adminClientUserService.list(userRequest);
            if (CollectionUtils.isNotEmpty(userDTOList)) {
                Map<String, YqmUserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
                for (YqmTopicDTO projectDTO : projectDTOS) {
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
    public YqmTopicDTO getById(String id) {
        YqmTopic entity = iYqmTopicService.getById(id);
        YqmTopicClassificationDTO classificationDTO = adminTopicClassificationService.getById(entity.getClassificationId());
        YqmUserDTO userDTO = adminClientUserService.getById(entity.getCreatedBy());


        YqmTopicDTO topicDTO = YqmTopicToDTO.toYqmTopicDTO(entity);
        topicDTO.setTopicClassificationDTO(classificationDTO);
        topicDTO.setUserDTO(userDTO);

        return topicDTO;
    }


}
