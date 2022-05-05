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

import com.yqm.common.dto.YqmRefundWhyDTO;
import com.yqm.common.entity.YqmRefundWhy;
import com.yqm.common.request.YqmRefundWhyRequest;
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
public class YqmRefundWhyToDTO {

    public static YqmRefundWhyDTO toYqmRefundWhyDTO(YqmRefundWhy entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmRefundWhyDTO dto = new YqmRefundWhyDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmRefundWhy toYqmRefundWhy(YqmRefundWhyDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmRefundWhy entity = new YqmRefundWhy();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmRefundWhyRequest toYqmRefundWhyRequest(YqmRefundWhy entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmRefundWhyRequest brandRequest = new YqmRefundWhyRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmRefundWhy toYqmRefundWhy(YqmRefundWhyRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmRefundWhy entity = new YqmRefundWhy();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmRefundWhyDTO> toYqmRefundWhyDTOList(List<YqmRefundWhy> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmRefundWhyDTO> dtoList = entityList.stream().map(e -> toYqmRefundWhyDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmRefundWhy> toYqmRefundWhyList(List<YqmRefundWhyDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmRefundWhy> entityList = dtoList.stream().map(e -> toYqmRefundWhy(e)).collect(Collectors.toList());
        return entityList;
    }

}
