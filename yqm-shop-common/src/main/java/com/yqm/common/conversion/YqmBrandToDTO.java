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

import com.yqm.common.dto.YqmBrandDTO;
import com.yqm.common.entity.YqmBrand;
import com.yqm.common.request.YqmBrandRequest;
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
public class YqmBrandToDTO {

    public static YqmBrandDTO toYqmBrandDTO(YqmBrand entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmBrandDTO dto = new YqmBrandDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmBrand toYqmBrand(YqmBrandDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmBrand entity = new YqmBrand();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmBrandRequest toYqmBrandRequest(YqmBrand entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmBrandRequest brandRequest = new YqmBrandRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmBrand toYqmBrand(YqmBrandRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmBrand entity = new YqmBrand();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmBrandDTO> toYqmBrandDTOList(List<YqmBrand> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmBrandDTO> dtoList = entityList.stream().map(e -> toYqmBrandDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmBrand> toYqmBrandList(List<YqmBrandDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmBrand> entityList = dtoList.stream().map(e -> toYqmBrand(e)).collect(Collectors.toList());
        return entityList;
    }

}
