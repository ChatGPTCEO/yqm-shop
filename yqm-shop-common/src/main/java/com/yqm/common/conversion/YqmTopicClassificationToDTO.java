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

import com.yqm.common.dto.YqmTopicClassificationDTO;
import com.yqm.common.entity.YqmTopicClassification;
import com.yqm.common.request.YqmTopicClassificationRequest;
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
public class YqmTopicClassificationToDTO {

    public static YqmTopicClassificationDTO toYqmTopicClassificationDTO(YqmTopicClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmTopicClassificationDTO dto = new YqmTopicClassificationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmTopicClassification toYqmTopicClassification(YqmTopicClassificationDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmTopicClassification entity = new YqmTopicClassification();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmTopicClassificationRequest toYqmTopicClassificationRequest(YqmTopicClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmTopicClassificationRequest brandRequest = new YqmTopicClassificationRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmTopicClassification toYqmTopicClassification(YqmTopicClassificationRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmTopicClassification entity = new YqmTopicClassification();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmTopicClassificationDTO> toYqmTopicClassificationDTOList(List<YqmTopicClassification> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmTopicClassificationDTO> dtoList = entityList.stream().map(e -> toYqmTopicClassificationDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmTopicClassification> toYqmTopicClassificationList(List<YqmTopicClassificationDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmTopicClassification> entityList = dtoList.stream().map(e -> toYqmTopicClassification(e)).collect(Collectors.toList());
        return entityList;
    }

}
