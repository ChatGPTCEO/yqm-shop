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

import com.yqm.common.dto.YqmHelpClassificationDTO;
import com.yqm.common.entity.YqmHelpClassification;
import com.yqm.common.request.YqmHelpClassificationRequest;
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
public class YqmHelpClassificationToDTO {

    public static YqmHelpClassificationDTO toYqmHelpClassificationDTO(YqmHelpClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmHelpClassificationDTO dto = new YqmHelpClassificationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmHelpClassification toYqmHelpClassification(YqmHelpClassificationDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmHelpClassification entity = new YqmHelpClassification();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmHelpClassificationRequest toYqmHelpClassificationRequest(YqmHelpClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmHelpClassificationRequest brandRequest = new YqmHelpClassificationRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmHelpClassification toYqmHelpClassification(YqmHelpClassificationRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmHelpClassification entity = new YqmHelpClassification();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmHelpClassificationDTO> toYqmHelpClassificationDTOList(List<YqmHelpClassification> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmHelpClassificationDTO> dtoList = entityList.stream().map(e -> toYqmHelpClassificationDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmHelpClassification> toYqmHelpClassificationList(List<YqmHelpClassificationDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmHelpClassification> entityList = dtoList.stream().map(e -> toYqmHelpClassification(e)).collect(Collectors.toList());
        return entityList;
    }

}
