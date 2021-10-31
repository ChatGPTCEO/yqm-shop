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
import com.yqm.common.dto.TpCompanyDTO;
import com.yqm.common.dto.TpRegionDTO;
import com.yqm.common.entity.TpCompany;
import com.yqm.common.entity.TpRegion;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2021/10/24 22:26
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class TpRegionToDTO {

    public static TpRegionDTO toTpCompanyDTO(TpRegion entity) {
        TpRegionDTO dto = new TpRegionDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static List<TpRegionDTO> toTpCompanyDTOList(List<TpRegion> entityList) {
        if  (CollectionUtils.isEmpty(entityList)) return null;
        List<TpRegionDTO> dtoList = entityList.stream().map(e -> TpRegionToDTO.toTpCompanyDTO(e)).collect(Collectors.toList());
        return dtoList;
    }
}
