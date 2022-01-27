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
import com.yqm.common.dto.CustomerSysConfigDTO;
import com.yqm.common.entity.CustomerSysConfig;
import com.yqm.common.request.CustomerSysConfigRequest;
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
public class TpCustomerSysConfigToDTO {

    public static CustomerSysConfigDTO toCustomerSysConfigDTO(CustomerSysConfig entity) {
        CustomerSysConfigDTO dto = new CustomerSysConfigDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static CustomerSysConfig toCustomerSysConfig(CustomerSysConfigRequest request) {
        CustomerSysConfig entity = new CustomerSysConfig();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }

    public static CustomerSysConfig toCustomerSysConfig(CustomerSysConfigDTO dto) {
        CustomerSysConfig entity = new CustomerSysConfig();
        BeanUtil.copyProperties(dto, entity);
        return entity;
    }

    public static List<CustomerSysConfigDTO> toCustomerSysConfigDTOList(List<CustomerSysConfig> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<CustomerSysConfigDTO> dtoList = entityList.stream().map(e -> TpCustomerSysConfigToDTO.toCustomerSysConfigDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<CustomerSysConfig> toCustomerSysConfigList(List<CustomerSysConfigDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<CustomerSysConfig> entityList = dtoList.stream().map(e -> TpCustomerSysConfigToDTO.toCustomerSysConfig(e)).collect(Collectors.toList());
        return entityList;
    }
}
