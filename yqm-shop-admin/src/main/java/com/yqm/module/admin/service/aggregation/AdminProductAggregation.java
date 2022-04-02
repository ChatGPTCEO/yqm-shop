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

import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreProductDTO;
import com.yqm.common.dto.YqmStoreSkuDTO;
import com.yqm.common.dto.YqmStoreSpecDTO;
import com.yqm.common.request.YqmStoreProductRequest;
import com.yqm.common.request.YqmStoreSkuRequest;
import com.yqm.common.request.YqmStoreSpecRequest;
import com.yqm.module.admin.service.AdminStoreProductService;
import com.yqm.module.admin.service.AdminStoreSkuService;
import com.yqm.module.admin.service.AdminStoreSpecService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2022/3/20 15:28
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminProductAggregation {


    private final AdminStoreProductService adminStoreProductService;
    private final AdminStoreSpecService adminStoreSpecService;
    private final AdminStoreSkuService adminStoreSkuService;
    private final ApplicationContext applicationContext;

    public AdminProductAggregation(AdminStoreProductService adminStoreProductService, AdminStoreSpecService adminStoreSpecService, AdminStoreSkuService adminStoreSkuService, ApplicationContext applicationContext) {
        this.adminStoreProductService = adminStoreProductService;
        this.adminStoreSpecService = adminStoreSpecService;
        this.adminStoreSkuService = adminStoreSkuService;
        this.applicationContext = applicationContext;
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmStoreProductDTO saveProduct(YqmStoreProductRequest request) {

        YqmStoreProductDTO storeProductDTO = adminStoreProductService.save(request);
        List<YqmStoreSpecDTO> storeSpecDTOList = new ArrayList<>();

        // spec
        if (CollectionUtils.isNotEmpty(request.getSpecList())) {
            for (YqmStoreSpecRequest storeSpecRequest : request.getSpecList()) {
                storeSpecRequest.setProductId(request.getId());
                YqmStoreSpecDTO storeSpecDTO = adminStoreSpecService.saveSpecChild(storeSpecRequest);
                storeSpecDTOList.add(storeSpecDTO);
            }
        }

        // sku
        if (CollectionUtils.isNotEmpty(request.getSkuList())) {

            Map<String, YqmStoreSpecDTO> storeSpecDTOMap = new HashMap<>();
            storeSpecDTOList.forEach(e -> {
                storeSpecDTOMap.put(e.getSpecName(), e);
                e.getInputValue().forEach(chilElement -> {
                    storeSpecDTOMap.put(e.getSpecName() + chilElement.getSpecName(), chilElement);
                });
            });

            for (YqmStoreSkuRequest storeSkuRequest : request.getSkuList()) {
                StringBuilder specId = new StringBuilder();
                StringBuilder prentSpecId = new StringBuilder();
                List<YqmStoreSpecRequest> specRequestList = storeSkuRequest.getSpec();
                int specListSize = specRequestList.size();
                for (int i = 0; i < specListSize; i++) {
                    YqmStoreSpecDTO storeSpecDTO = storeSpecDTOMap.get(specRequestList.get(i).getPrentSpecName() + specRequestList.get(i).getSpecName());
                    if (Objects.nonNull(storeSpecDTO)) {
                        specId.append(storeSpecDTO.getId());
                        if (i < specListSize - 1) {
                            specId.append(":");
                        } else {
                            specId.append(",");
                        }
                    }

                    YqmStoreSpecDTO prentStoreSpecDTO = storeSpecDTOMap.get(specRequestList.get(i).getPrentSpecName());
                    if (Objects.nonNull(prentStoreSpecDTO)) {
                        prentSpecId.append(prentStoreSpecDTO.getId());
                        if (i < specListSize - 1) {
                            prentSpecId.append(":");
                        } else {
                            prentSpecId.append(",");
                        }
                    }
                }
                storeSkuRequest.setSpecIds(specId.toString());
                storeSkuRequest.setPspecIds(prentSpecId.toString());
                storeSkuRequest.setProductId(storeProductDTO.getId());
                adminStoreSkuService.save(storeSkuRequest);

            }
        }

        return storeProductDTO;

    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public YqmStoreProductDTO getById(String id) {

        YqmStoreProductDTO storeProductDTO = adminStoreProductService.getById(id);

        YqmStoreSpecRequest storeSpecRequest = new YqmStoreSpecRequest();
        storeSpecRequest.setProductId(storeProductDTO.getId());
        storeSpecRequest.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreSpecDTO> storeSpecDTOList = adminStoreSpecService.list(storeSpecRequest);
        if (CollectionUtils.isEmpty(storeSpecDTOList)) {
            return storeProductDTO;
        }
        Map<String, YqmStoreSpecDTO> storeSpecDTOMap = storeSpecDTOList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        // 属性
        List<YqmStoreSpecDTO> specList = storeSpecDTOList.stream().filter(e -> YqmDefine.SpecType.attr.getValue().equals(e.getIsParent())).map(e -> {
            List<YqmStoreSpecDTO> chilSpecList = storeSpecDTOList.stream().filter(chilEl -> e.getId().equals(chilEl.getPid())).collect(Collectors.toList());
            e.setInputValue(chilSpecList);
            return e;
        }).collect(Collectors.toList());
        storeProductDTO.setSpecList(specList);

        // sku
        YqmStoreSkuRequest storeSkuRequest = new YqmStoreSkuRequest();
        storeSkuRequest.setProductId(storeProductDTO.getId());
        storeSkuRequest.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreSkuDTO> storeSkuDTOS = adminStoreSkuService.list(storeSkuRequest);
        storeSkuDTOS.forEach(e -> {
            // 组装属性
            this.getSkuSpec(e);
        });
        storeProductDTO.setSkuList(storeSkuDTOS);

        return storeProductDTO;
    }

    private YqmStoreSkuDTO getSkuSpec(YqmStoreSkuDTO e) {

        YqmStoreSpecRequest storeSpecRequest = new YqmStoreSpecRequest();
        storeSpecRequest.setProductId(e.getProductId());
        storeSpecRequest.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreSpecDTO> storeSpecDTOList = adminStoreSpecService.list(storeSpecRequest);

        Map<String, YqmStoreSpecDTO> storeSpecDTOMap = storeSpecDTOList.stream().collect(Collectors.toMap(storeSpec -> storeSpec.getId(), storeSpec -> storeSpec));


        String specIds = e.getSpecIds();
        String[] specIdsArr = specIds.split(",");
        List<YqmStoreSpecDTO> chilSkuList = new ArrayList<>();
        for (String spec : specIdsArr) {
            if (StringUtils.isNotBlank(spec)) {
                String[] chilSpecIdArr = spec.split(":");
                for (String chilSpec : chilSpecIdArr) {
                    YqmStoreSpecDTO storeSpecDTO = storeSpecDTOMap.get(chilSpec);
                    if (Objects.nonNull(storeSpecDTO)) {
                        storeSpecDTO.setPrentSpecName(storeSpecDTOMap.get(storeSpecDTO.getPid()).getSpecName());
                    }
                    chilSkuList.add(storeSpecDTO);
                }
            }
        }
        e.setSpec(chilSkuList);

        return e;
    }
}
