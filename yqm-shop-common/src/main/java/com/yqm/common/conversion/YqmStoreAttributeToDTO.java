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

import com.yqm.common.dto.YqmStoreAttributeDTO;
import com.yqm.common.entity.YqmStoreAttribute;
import com.yqm.common.request.YqmStoreAttributeRequest;
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
public class YqmStoreAttributeToDTO {

    public static YqmStoreAttributeDTO toYqmStoreAttributeDTO(YqmStoreAttribute entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreAttributeDTO dto = new YqmStoreAttributeDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmStoreAttribute toYqmStoreAttribute(YqmStoreAttributeDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmStoreAttribute entity = new YqmStoreAttribute();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmStoreAttributeRequest toYqmStoreAttributeRequest(YqmStoreAttribute entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreAttributeRequest brandRequest = new YqmStoreAttributeRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmStoreAttribute toYqmStoreAttribute(YqmStoreAttributeRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreAttribute entity = new YqmStoreAttribute();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmStoreAttributeDTO> toYqmStoreAttributeDTOList(List<YqmStoreAttribute> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmStoreAttributeDTO> dtoList = entityList.stream().map(e -> toYqmStoreAttributeDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmStoreAttribute> toYqmStoreAttributeList(List<YqmStoreAttributeDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmStoreAttribute> entityList = dtoList.stream().map(e -> toYqmStoreAttribute(e)).collect(Collectors.toList());
        return entityList;
    }

}
