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

import com.yqm.common.dto.YqmTopicDTO;
import com.yqm.common.entity.YqmTopic;
import com.yqm.common.request.YqmTopicRequest;
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
public class YqmTopicToDTO {

    public static YqmTopicDTO toYqmTopicDTO(YqmTopic entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmTopicDTO dto = new YqmTopicDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmTopic toYqmTopic(YqmTopicDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmTopic entity = new YqmTopic();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmTopicRequest toYqmTopicRequest(YqmTopic entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmTopicRequest brandRequest = new YqmTopicRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmTopic toYqmTopic(YqmTopicRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmTopic entity = new YqmTopic();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmTopicDTO> toYqmTopicDTOList(List<YqmTopic> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmTopicDTO> dtoList = entityList.stream().map(e -> toYqmTopicDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmTopic> toYqmTopicList(List<YqmTopicDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmTopic> entityList = dtoList.stream().map(e -> toYqmTopic(e)).collect(Collectors.toList());
        return entityList;
    }

}
