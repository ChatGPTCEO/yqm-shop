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

import com.yqm.common.dto.YqmStoreTypeDTO;
import com.yqm.common.entity.YqmStoreType;
import com.yqm.common.request.YqmStoreTypeRequest;
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
public class YqmStoreTypeToDTO {

    public static YqmStoreTypeDTO toYqmStoreTypeDTO(YqmStoreType entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreTypeDTO dto = new YqmStoreTypeDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmStoreType toYqmStoreType(YqmStoreTypeDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmStoreType entity = new YqmStoreType();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmStoreTypeRequest toYqmStoreTypeRequest(YqmStoreType entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreTypeRequest brandRequest = new YqmStoreTypeRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmStoreType toYqmStoreType(YqmStoreTypeRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreType entity = new YqmStoreType();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmStoreTypeDTO> toYqmStoreTypeDTOList(List<YqmStoreType> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmStoreTypeDTO> dtoList = entityList.stream().map(e -> toYqmStoreTypeDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmStoreType> toYqmStoreTypeList(List<YqmStoreTypeDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmStoreType> entityList = dtoList.stream().map(e -> toYqmStoreType(e)).collect(Collectors.toList());
        return entityList;
    }

}
