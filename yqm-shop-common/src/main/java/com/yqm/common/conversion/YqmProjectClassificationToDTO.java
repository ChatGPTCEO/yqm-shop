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

import com.yqm.common.dto.YqmProjectClassificationDTO;
import com.yqm.common.entity.YqmProjectClassification;
import com.yqm.common.request.YqmProjectClassificationRequest;
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
public class YqmProjectClassificationToDTO {

    public static YqmProjectClassificationDTO toYqmProjectClassificationDTO(YqmProjectClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmProjectClassificationDTO dto = new YqmProjectClassificationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmProjectClassification toYqmProjectClassification(YqmProjectClassificationDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmProjectClassification entity = new YqmProjectClassification();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmProjectClassificationRequest toYqmProjectClassificationRequest(YqmProjectClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmProjectClassificationRequest brandRequest = new YqmProjectClassificationRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmProjectClassification toYqmProjectClassification(YqmProjectClassificationRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmProjectClassification entity = new YqmProjectClassification();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmProjectClassificationDTO> toYqmProjectClassificationDTOList(List<YqmProjectClassification> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmProjectClassificationDTO> dtoList = entityList.stream().map(e -> toYqmProjectClassificationDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmProjectClassification> toYqmProjectClassificationList(List<YqmProjectClassificationDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmProjectClassification> entityList = dtoList.stream().map(e -> toYqmProjectClassification(e)).collect(Collectors.toList());
        return entityList;
    }

}
