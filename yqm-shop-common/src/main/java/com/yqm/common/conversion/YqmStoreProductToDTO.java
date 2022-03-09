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

import com.alibaba.fastjson.JSONObject;
import com.yqm.common.dto.YqmStoreProductDTO;
import com.yqm.common.entity.YqmStoreProduct;
import com.yqm.common.request.YqmStoreProductRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class YqmStoreProductToDTO {

    public static YqmStoreProductDTO toYqmStoreProductDTO(YqmStoreProduct entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreProductDTO dto = new YqmStoreProductDTO();
        BeanUtils.copyProperties(entity, dto);
        if (StringUtils.isNotBlank(entity.getProductBanner())) {
            dto.setProductBannerList(JSONObject.parseArray(entity.getProductBanner(), String.class));
        }
        return dto;
    }

    public static YqmStoreProduct toYqmStoreProduct(YqmStoreProductDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmStoreProduct entity = new YqmStoreProduct();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmStoreProductRequest toYqmStoreProductRequest(YqmStoreProduct entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreProductRequest brandRequest = new YqmStoreProductRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmStoreProduct toYqmStoreProduct(YqmStoreProductRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreProduct entity = new YqmStoreProduct();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmStoreProductDTO> toYqmStoreProductDTOList(List<YqmStoreProduct> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmStoreProductDTO> dtoList = entityList.stream().map(e -> toYqmStoreProductDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmStoreProduct> toYqmStoreProductList(List<YqmStoreProductDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmStoreProduct> entityList = dtoList.stream().map(e -> toYqmStoreProduct(e)).collect(Collectors.toList());
        return entityList;
    }

}
