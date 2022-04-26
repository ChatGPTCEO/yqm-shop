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

import com.yqm.common.dto.YqmRefundPayDTO;
import com.yqm.common.entity.YqmRefundPay;
import com.yqm.common.request.YqmRefundPayRequest;
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
public class YqmRefundPayToDTO {

    public static YqmRefundPayDTO toYqmRefundPayDTO(YqmRefundPay entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmRefundPayDTO dto = new YqmRefundPayDTO();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    public static YqmRefundPay toYqmRefundPay(YqmRefundPayDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmRefundPay entity = new YqmRefundPay();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmRefundPayRequest toYqmRefundPayRequest(YqmRefundPay entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmRefundPayRequest brandRequest = new YqmRefundPayRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmRefundPay toYqmRefundPay(YqmRefundPayRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmRefundPay entity = new YqmRefundPay();
        BeanUtils.copyProperties(request, entity);
        
        return entity;
    }

    public static List<YqmRefundPayDTO> toYqmRefundPayDTOList(List<YqmRefundPay> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmRefundPayDTO> dtoList = entityList.stream().map(e -> toYqmRefundPayDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmRefundPay> toYqmRefundPayList(List<YqmRefundPayDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmRefundPay> entityList = dtoList.stream().map(e -> toYqmRefundPay(e)).collect(Collectors.toList());
        return entityList;
    }

}
