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

import com.yqm.common.dto.YqmClassificationDTO;
import com.yqm.common.entity.YqmClassification;
import com.yqm.common.request.YqmClassificationRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
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
public class YqmClassificationToDTO {

    public static YqmClassificationDTO toYqmClassificationDTO(YqmClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmClassificationDTO dto = new YqmClassificationDTO();
        BeanUtils.copyProperties(entity, dto);

        if (StringUtils.isNotBlank(entity.getPids())) {
            dto.setPidsList(Arrays.asList(StringUtils.split(entity.getPids(), ",")));
        }

        return dto;
    }

    public static YqmClassification toYqmClassification(YqmClassificationDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmClassification entity = new YqmClassification();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmClassificationRequest toYqmClassificationRequest(YqmClassification entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmClassificationRequest brandRequest = new YqmClassificationRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmClassification toYqmClassification(YqmClassificationRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmClassification entity = new YqmClassification();
        BeanUtils.copyProperties(request, entity);

        if (CollectionUtils.isNotEmpty(request.getPidsList())) {
            entity.setPids(StringUtils.join(request.getPidsList().toArray(),","));
            entity.setPid(request.getPidsList().get(request.getPidsList().size() - 1));
        }

        return entity;
    }

    public static List<YqmClassificationDTO> toYqmClassificationDTOList(List<YqmClassification> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmClassificationDTO> dtoList = entityList.stream().map(e -> toYqmClassificationDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmClassification> toYqmClassificationList(List<YqmClassificationDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmClassification> entityList = dtoList.stream().map(e -> toYqmClassification(e)).collect(Collectors.toList());
        return entityList;
    }

}
