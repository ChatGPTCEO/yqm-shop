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
import com.yqm.common.conversion.YqmProjectToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmProjectClassificationDTO;
import com.yqm.common.dto.YqmProjectDTO;
import com.yqm.common.entity.YqmProject;
import com.yqm.common.request.YqmProjectClassificationRequest;
import com.yqm.common.request.YqmProjectRequest;
import com.yqm.common.service.IYqmProjectService;
import com.yqm.module.admin.service.AdminProjectClassificationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2022/5/31 20:35
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminProjectAggregation {

    private final IYqmProjectService iYqmProjectService;
    private final AdminProjectClassificationService adminProjectClassificationService;

    public AdminProjectAggregation(IYqmProjectService iYqmProjectService, AdminProjectClassificationService adminProjectClassificationService) {
        this.iYqmProjectService = iYqmProjectService;
        this.adminProjectClassificationService = adminProjectClassificationService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmProjectDTO> page(YqmProjectRequest request) {
        Page<YqmProject> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmProjectService.page(page, iYqmProjectService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmProjectDTO> projectDTOS = YqmProjectToDTO.toYqmProjectDTOList(pageList.getRecords());
            List<String> idList = projectDTOS.stream().map(e -> e.getClassificationId()).collect(Collectors.toList());

            YqmProjectClassificationRequest classificationRequest = new YqmProjectClassificationRequest();
            classificationRequest.setInIdList(idList);
            List<YqmProjectClassificationDTO> classificationDTOS = adminProjectClassificationService.list(classificationRequest);
            if (CollectionUtils.isNotEmpty(classificationDTOS)) {
                Map<String, YqmProjectClassificationDTO> classificationDTOMap = classificationDTOS.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
                for (YqmProjectDTO projectDTO : projectDTOS) {
                    projectDTO.setClassificationDTO(classificationDTOMap.get(projectDTO.getClassificationId()));
                }
            }


            pageList.setRecords(projectDTOS);
        }
        return pageList;
    }

}
