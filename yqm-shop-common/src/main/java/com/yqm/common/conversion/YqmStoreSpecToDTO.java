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

package com.yqm.common.conversion;

import com.yqm.common.dto.YqmStoreSpecDTO;
import com.yqm.common.entity.YqmStoreSpec;
import com.yqm.common.request.YqmStoreSpecRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2021/10/18 19:37
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class YqmStoreSpecToDTO {

    public static YqmStoreSpecDTO toYqmStoreSpecDTO(YqmStoreSpec entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreSpecDTO dto = new YqmStoreSpecDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmStoreSpec toYqmStoreSpec(YqmStoreSpecDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmStoreSpec entity = new YqmStoreSpec();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmStoreSpecDTO requestToYqmStoreSpec(YqmStoreSpecRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreSpecDTO dto = new YqmStoreSpecDTO();
        BeanUtils.copyProperties(request, dto);
        if (CollectionUtils.isNotEmpty(request.getInputValue())) {
            dto.setInputValue(request.getInputValue().stream().map(e -> requestToYqmStoreSpec(e)).collect(Collectors.toList()));
        }
        return dto;
    }

    public static YqmStoreSpecRequest toYqmStoreSpecRequest(YqmStoreSpec entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreSpecRequest brandRequest = new YqmStoreSpecRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmStoreSpec toYqmStoreSpec(YqmStoreSpecRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreSpec entity = new YqmStoreSpec();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmStoreSpecDTO> toYqmStoreSpecDTOList(List<YqmStoreSpec> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmStoreSpecDTO> dtoList = entityList.stream().map(e -> toYqmStoreSpecDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmStoreSpec> toYqmStoreSpecList(List<YqmStoreSpecDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmStoreSpec> entityList = dtoList.stream().map(e -> toYqmStoreSpec(e)).collect(Collectors.toList());
        return entityList;
    }

    public static List<YqmStoreSpec> requestToYqmStoreSpecList(List<YqmStoreSpecRequest> requestList) {
        if (CollectionUtils.isEmpty(requestList)) {
            return null;
        }
        List<YqmStoreSpec> entityList = requestList.stream().map(e -> toYqmStoreSpec(e)).collect(Collectors.toList());
        return entityList;
    }

    public static List<YqmStoreSpecDTO> entityToYqmStoreSpecDTOList(List<YqmStoreSpec> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<YqmStoreSpecDTO> dtoList = list.stream().map(e -> toYqmStoreSpecDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

}
