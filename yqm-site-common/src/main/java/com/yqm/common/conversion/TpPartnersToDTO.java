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
import com.yqm.common.dto.TpPartnersDTO;
import com.yqm.common.entity.TpPartners;
import com.yqm.common.request.TpPartnersRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2021/11/7 18:48
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class TpPartnersToDTO {

    public static TpPartnersDTO toTpPartnersDTO(TpPartners entity) {
        TpPartnersDTO dto = new TpPartnersDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static TpPartners toTpPartners(TpPartnersRequest request) {
        TpPartners entity = new TpPartners();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }

    public static List<TpPartnersDTO> toTpPartnersDTOList(List<TpPartners> entityList) {
        List<TpPartnersDTO> dtoList = entityList.stream().map(e -> TpPartnersToDTO.toTpPartnersDTO(e)).collect(Collectors.toList());
        return dtoList;
    }
}
