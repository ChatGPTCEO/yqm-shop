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

import com.yqm.common.dto.YqmStoreSkuDTO;
import com.yqm.common.entity.YqmStoreSku;
import com.yqm.common.request.YqmStoreSkuRequest;
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
public class YqmStoreSkuToDTO {

    public static YqmStoreSkuDTO toYqmStoreSkuDTO(YqmStoreSku entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreSkuDTO dto = new YqmStoreSkuDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmStoreSkuDTO requestToYqmStoreSkuDTO(YqmStoreSkuRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreSkuDTO dto = new YqmStoreSkuDTO();
        BeanUtils.copyProperties(request, dto);
        return dto;
    }

    public static YqmStoreSku toYqmStoreSku(YqmStoreSkuDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmStoreSku entity = new YqmStoreSku();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmStoreSkuRequest toYqmStoreSkuRequest(YqmStoreSku entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreSkuRequest brandRequest = new YqmStoreSkuRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmStoreSku toYqmStoreSku(YqmStoreSkuRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreSku entity = new YqmStoreSku();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmStoreSkuDTO> toYqmStoreSkuDTOList(List<YqmStoreSku> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmStoreSkuDTO> dtoList = entityList.stream().map(e -> toYqmStoreSkuDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmStoreSku> toYqmStoreSkuList(List<YqmStoreSkuDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmStoreSku> entityList = dtoList.stream().map(e -> toYqmStoreSku(e)).collect(Collectors.toList());
        return entityList;
    }

}
