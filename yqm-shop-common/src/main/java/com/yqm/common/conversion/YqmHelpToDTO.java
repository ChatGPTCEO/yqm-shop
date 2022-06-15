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

import com.yqm.common.dto.YqmHelpDTO;
import com.yqm.common.entity.YqmHelp;
import com.yqm.common.request.YqmHelpRequest;
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
public class YqmHelpToDTO {

    public static YqmHelpDTO toYqmHelpDTO(YqmHelp entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmHelpDTO dto = new YqmHelpDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmHelp toYqmHelp(YqmHelpDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmHelp entity = new YqmHelp();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmHelpRequest toYqmHelpRequest(YqmHelp entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmHelpRequest brandRequest = new YqmHelpRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmHelp toYqmHelp(YqmHelpRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmHelp entity = new YqmHelp();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmHelpDTO> toYqmHelpDTOList(List<YqmHelp> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmHelpDTO> dtoList = entityList.stream().map(e -> toYqmHelpDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmHelp> toYqmHelpList(List<YqmHelpDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmHelp> entityList = dtoList.stream().map(e -> toYqmHelp(e)).collect(Collectors.toList());
        return entityList;
    }

}
