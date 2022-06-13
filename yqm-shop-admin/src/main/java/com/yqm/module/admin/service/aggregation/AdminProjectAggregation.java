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
import com.yqm.common.dto.YqmStoreProductDTO;
import com.yqm.common.entity.YqmProject;
import com.yqm.common.entity.YqmProjectGoods;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmProjectClassificationRequest;
import com.yqm.common.request.YqmProjectGoodsRequest;
import com.yqm.common.request.YqmProjectRequest;
import com.yqm.common.request.YqmStoreProductRequest;
import com.yqm.common.service.IYqmProjectGoodsService;
import com.yqm.common.service.IYqmProjectService;
import com.yqm.module.admin.service.AdminProjectClassificationService;
import com.yqm.module.admin.service.AdminStoreProductService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final AdminStoreProductService adminStoreProductService;
    private final IYqmProjectGoodsService iYqmProjectGoodsService;

    public AdminProjectAggregation(IYqmProjectService iYqmProjectService, AdminProjectClassificationService adminProjectClassificationService, AdminStoreProductService adminStoreProductService, IYqmProjectGoodsService iYqmProjectGoodsService) {
        this.iYqmProjectService = iYqmProjectService;
        this.adminProjectClassificationService = adminProjectClassificationService;
        this.adminStoreProductService = adminStoreProductService;
        this.iYqmProjectGoodsService = iYqmProjectGoodsService;
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


    /**
     * 集合
     *
     * @return
     */
    public YqmProjectDTO getById(String id) {
        YqmProject info = iYqmProjectService.getById(id);
        if (Objects.isNull(info)) {
            throw new YqmException("数据错误！");
        }

        YqmProjectDTO projectDTO = YqmProjectToDTO.toYqmProjectDTO(info);

        YqmProjectGoodsRequest projectGoodsRequest = new YqmProjectGoodsRequest();
        projectGoodsRequest.setProjectId(id);
        projectGoodsRequest.setInStatusList(YqmDefine.includeStatus);
        List<YqmProjectGoods> projectGoodsList = iYqmProjectGoodsService.list(iYqmProjectGoodsService.getQuery(projectGoodsRequest));

        List<String> goodsIdList = projectGoodsList.stream().map(YqmProjectGoods::getProjectGoodsId).collect(Collectors.toList());

        YqmProjectClassificationDTO classificationDTO = adminProjectClassificationService.getById(info.getClassificationId());
        if (Objects.nonNull(classificationDTO)) {
            projectDTO.setClassificationDTO(classificationDTO);
        }

        if (CollectionUtils.isNotEmpty(goodsIdList)) {
            YqmStoreProductRequest storeProductRequest = new YqmStoreProductRequest();
            storeProductRequest.setInIdList(goodsIdList);
            List<YqmStoreProductDTO> storeProductDTOList = adminStoreProductService.list(storeProductRequest);

            projectDTO.setProductList(storeProductDTOList);
        }

        return projectDTO;
    }
}
