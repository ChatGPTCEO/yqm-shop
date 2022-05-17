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

import com.yqm.common.dto.YqmStoreSkuLogDTO;
import com.yqm.common.entity.YqmStoreSkuLog;
import com.yqm.common.request.YqmStoreSkuLogRequest;
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
public class YqmStoreSkuLogToDTO {

    public static YqmStoreSkuLogDTO toYqmStoreSkuLogDTO(YqmStoreSkuLog entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreSkuLogDTO dto = new YqmStoreSkuLogDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmStoreSkuLogDTO requestToYqmStoreSkuLogDTO(YqmStoreSkuLogRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreSkuLogDTO dto = new YqmStoreSkuLogDTO();
        BeanUtils.copyProperties(request, dto);
        return dto;
    }

    public static YqmStoreSkuLog toYqmStoreSkuLog(YqmStoreSkuLogDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmStoreSkuLog entity = new YqmStoreSkuLog();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmStoreSkuLogRequest toYqmStoreSkuLogRequest(YqmStoreSkuLog entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmStoreSkuLogRequest brandRequest = new YqmStoreSkuLogRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmStoreSkuLog toYqmStoreSkuLog(YqmStoreSkuLogRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmStoreSkuLog entity = new YqmStoreSkuLog();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmStoreSkuLogDTO> toYqmStoreSkuLogDTOList(List<YqmStoreSkuLog> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmStoreSkuLogDTO> dtoList = entityList.stream().map(e -> toYqmStoreSkuLogDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmStoreSkuLog> toYqmStoreSkuLogList(List<YqmStoreSkuLogDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmStoreSkuLog> entityList = dtoList.stream().map(e -> toYqmStoreSkuLog(e)).collect(Collectors.toList());
        return entityList;
    }

}
