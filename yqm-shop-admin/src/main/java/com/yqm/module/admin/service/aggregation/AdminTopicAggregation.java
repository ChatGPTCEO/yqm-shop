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
import com.yqm.common.entity.YqmTopic;
import com.yqm.common.request.YqmTopicClassificationRequest;
import com.yqm.common.request.YqmTopicRequest;
import com.yqm.common.service.IYqmTopicService;
import com.yqm.module.admin.service.AdminTopicClassificationService;
import com.yqm.module.admin.service.AdminUserService;
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
    private final AdminTopicClassificationService adminTopicClassificationService;

    public AdminTopicAggregation(IYqmTopicService iYqmTopicService, AdminTopicClassificationService adminTopicClassificationService, AdminUserService adminUserService) {
        this.iYqmTopicService = iYqmTopicService;
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


            pageList.setRecords(projectDTOS);
        }
        return pageList;
    }


}
