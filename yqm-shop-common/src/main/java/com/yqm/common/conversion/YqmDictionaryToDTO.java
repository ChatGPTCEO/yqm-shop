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

import com.yqm.common.dto.YqmDictionaryDTO;
import com.yqm.common.entity.YqmDictionary;
import com.yqm.common.request.YqmDictionaryRequest;
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
public class YqmDictionaryToDTO {

    public static YqmDictionaryDTO toYqmDictionaryDTO(YqmDictionary entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmDictionaryDTO dto = new YqmDictionaryDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmDictionary toYqmDictionary(YqmDictionaryDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmDictionary entity = new YqmDictionary();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmDictionaryRequest toYqmDictionaryRequest(YqmDictionary entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmDictionaryRequest brandRequest = new YqmDictionaryRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmDictionary toYqmDictionary(YqmDictionaryRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmDictionary entity = new YqmDictionary();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmDictionaryDTO> toYqmDictionaryDTOList(List<YqmDictionary> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmDictionaryDTO> dtoList = entityList.stream().map(e -> toYqmDictionaryDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmDictionary> toYqmDictionaryList(List<YqmDictionaryDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmDictionary> entityList = dtoList.stream().map(e -> toYqmDictionary(e)).collect(Collectors.toList());
        return entityList;
    }

}
