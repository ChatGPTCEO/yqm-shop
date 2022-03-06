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

import com.yqm.common.dto.YqmFreightTemplateDTO;
import com.yqm.common.entity.YqmFreightTemplate;
import com.yqm.common.request.YqmFreightTemplateRequest;
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
public class YqmFreightTemplateToDTO {

    public static YqmFreightTemplateDTO toYqmFreightTemplateDTO(YqmFreightTemplate entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmFreightTemplateDTO dto = new YqmFreightTemplateDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmFreightTemplate toYqmFreightTemplate(YqmFreightTemplateDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmFreightTemplate entity = new YqmFreightTemplate();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmFreightTemplateRequest toYqmFreightTemplateRequest(YqmFreightTemplate entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmFreightTemplateRequest brandRequest = new YqmFreightTemplateRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmFreightTemplate toYqmFreightTemplate(YqmFreightTemplateRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmFreightTemplate entity = new YqmFreightTemplate();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmFreightTemplateDTO> toYqmFreightTemplateDTOList(List<YqmFreightTemplate> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmFreightTemplateDTO> dtoList = entityList.stream().map(e -> toYqmFreightTemplateDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmFreightTemplate> toYqmFreightTemplateList(List<YqmFreightTemplateDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmFreightTemplate> entityList = dtoList.stream().map(e -> toYqmFreightTemplate(e)).collect(Collectors.toList());
        return entityList;
    }

}
