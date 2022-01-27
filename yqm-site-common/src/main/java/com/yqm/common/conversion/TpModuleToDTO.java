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
import com.yqm.common.dto.TpModuleDTO;
import com.yqm.common.entity.TpModule;
import com.yqm.common.request.TpModuleRequest;
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
public class TpModuleToDTO {

    public static TpModuleDTO toTpModuleDTO(TpModule entity) {
        TpModuleDTO dto = new TpModuleDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static TpModule toTpModule(TpModuleRequest request) {
        TpModule entity = new TpModule();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }

    public static TpModule toTpModule(TpModuleDTO dto) {
        TpModule entity = new TpModule();
        BeanUtil.copyProperties(dto, entity);
        return entity;
    }

    public static List<TpModuleDTO> toTpModuleDTOList(List<TpModule> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<TpModuleDTO> dtoList = entityList.stream().map(e -> TpModuleToDTO.toTpModuleDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<TpModule> toTpModuleList(List<TpModuleDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<TpModule> entityList = dtoList.stream().map(e -> TpModuleToDTO.toTpModule(e)).collect(Collectors.toList());
        return entityList;
    }
}
