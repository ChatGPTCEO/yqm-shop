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
import com.yqm.common.dto.TpPagesDTO;
import com.yqm.common.entity.TpPages;
import com.yqm.common.request.TpPagesRequest;
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
public class TpPagesToDTO {

    public static TpPagesDTO toTpPagesDTO(TpPages entity) {
        TpPagesDTO dto = new TpPagesDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static TpPages toTpPages(TpPagesRequest request) {
        TpPages entity = new TpPages();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }

    public static TpPages toTpPages(TpPagesDTO dto) {
        TpPages entity = new TpPages();
        BeanUtil.copyProperties(dto, entity);
        return entity;
    }

    public static TpPagesRequest toTpPagesRequest(TpPagesDTO dto) {
        TpPagesRequest request = new TpPagesRequest();
        BeanUtil.copyProperties(dto, request);
        return request;
    }

    public static List<TpPagesDTO> toTpPagesDTOList(List<TpPages> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<TpPagesDTO> dtoList = entityList.stream().map(e -> TpPagesToDTO.toTpPagesDTO(e))
                .collect(Collectors.toList());
        return dtoList;
    }

    public static List<TpPages> toTpPagesList(List<TpPagesDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<TpPages> entityList = dtoList.stream().map(e -> TpPagesToDTO.toTpPages(e)).collect(Collectors.toList());
        return entityList;
    }

    public static List<TpPagesRequest> toTpPagesRequestList(List<TpPagesDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<TpPagesRequest> entityList = dtoList.stream().map(e -> TpPagesToDTO.toTpPagesRequest(e))
                .collect(Collectors.toList());
        return entityList;
    }
}
