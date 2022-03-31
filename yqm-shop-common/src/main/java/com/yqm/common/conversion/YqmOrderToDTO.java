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

import com.yqm.common.dto.YqmOrderDTO;
import com.yqm.common.entity.YqmOrder;
import com.yqm.common.request.YqmOrderRequest;
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
public class YqmOrderToDTO {

    public static YqmOrderDTO toYqmOrderDTO(YqmOrder entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmOrderDTO dto = new YqmOrderDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmOrder toYqmOrder(YqmOrderDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmOrder entity = new YqmOrder();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmOrderRequest toYqmOrderRequest(YqmOrder entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmOrderRequest brandRequest = new YqmOrderRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmOrder toYqmOrder(YqmOrderRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmOrder entity = new YqmOrder();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmOrderDTO> toYqmOrderDTOList(List<YqmOrder> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmOrderDTO> dtoList = entityList.stream().map(e -> toYqmOrderDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmOrder> toYqmOrderList(List<YqmOrderDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmOrder> entityList = dtoList.stream().map(e -> toYqmOrder(e)).collect(Collectors.toList());
        return entityList;
    }

}
