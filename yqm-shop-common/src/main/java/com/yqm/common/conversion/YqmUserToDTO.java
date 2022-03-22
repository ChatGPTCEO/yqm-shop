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

import com.yqm.common.dto.YqmUserDTO;
import com.yqm.common.entity.YqmUser;
import com.yqm.common.request.YqmUserRequest;
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
public class YqmUserToDTO {

    public static YqmUserDTO toYqmUserDTO(YqmUser entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmUserDTO dto = new YqmUserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static YqmUser toYqmUser(YqmUserDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmUser entity = new YqmUser();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmUserRequest toYqmUserRequest(YqmUser entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmUserRequest brandRequest = new YqmUserRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmUser toYqmUser(YqmUserRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmUser entity = new YqmUser();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmUserDTO> toYqmUserDTOList(List<YqmUser> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmUserDTO> dtoList = entityList.stream().map(e -> toYqmUserDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmUser> toYqmUserList(List<YqmUserDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmUser> entityList = dtoList.stream().map(e -> toYqmUser(e)).collect(Collectors.toList());
        return entityList;
    }

}
