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

import cn.hutool.core.bean.BeanUtil;
import com.yqm.common.dto.TpComponentDTO;
import com.yqm.common.entity.TpComponent;
import com.yqm.common.request.TpComponentRequest;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2021/11/7 18:48
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class TpComponentToDTO {

    public static TpComponentDTO toTpComponentDTO(TpComponent entity) {
        TpComponentDTO dto = new TpComponentDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static TpComponent toTpComponent(TpComponentRequest request) {
        TpComponent entity = new TpComponent();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }

    public static TpComponent toTpComponent(TpComponentDTO dto) {
        TpComponent entity = new TpComponent();
        BeanUtil.copyProperties(dto, entity);
        return entity;
    }

    public static List<TpComponentDTO> toTpComponentDTOList(List<TpComponent> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<TpComponentDTO> dtoList = entityList.stream().map(e -> TpComponentToDTO.toTpComponentDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<TpComponent> toTpComponentList(List<TpComponentDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<TpComponent> entityList = dtoList.stream().map(e -> TpComponentToDTO.toTpComponent(e)).collect(Collectors.toList());
        return entityList;
    }
}
