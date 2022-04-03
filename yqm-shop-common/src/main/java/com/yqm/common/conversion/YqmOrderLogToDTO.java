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

import com.yqm.common.dto.YqmOrderLogDTO;
import com.yqm.common.entity.YqmOrderLog;
import com.yqm.common.request.YqmOrderLogRequest;
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
public class YqmOrderLogToDTO {

    public static YqmOrderLogDTO toYqmOrderLogDTO(YqmOrderLog entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmOrderLogDTO dto = new YqmOrderLogDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmOrderLog toYqmOrderLog(YqmOrderLogDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmOrderLog entity = new YqmOrderLog();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmOrderLogRequest toYqmOrderLogRequest(YqmOrderLog entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmOrderLogRequest brandRequest = new YqmOrderLogRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmOrderLog toYqmOrderLog(YqmOrderLogRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmOrderLog entity = new YqmOrderLog();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmOrderLogDTO> toYqmOrderLogDTOList(List<YqmOrderLog> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmOrderLogDTO> dtoList = entityList.stream().map(e -> toYqmOrderLogDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmOrderLog> toYqmOrderLogList(List<YqmOrderLogDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmOrderLog> entityList = dtoList.stream().map(e -> toYqmOrderLog(e)).collect(Collectors.toList());
        return entityList;
    }

}
