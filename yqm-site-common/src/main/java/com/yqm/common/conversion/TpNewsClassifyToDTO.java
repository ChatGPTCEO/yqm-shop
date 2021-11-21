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
import com.yqm.common.dto.TpNewsClassifyDTO;
import com.yqm.common.entity.TpNewsClassify;
import com.yqm.common.request.TpNewsClassifyRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2021/11/7 18:48
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class TpNewsClassifyToDTO {

    public static TpNewsClassifyDTO toTpNewsClassifyDTO(TpNewsClassify entity) {
        TpNewsClassifyDTO dto = new TpNewsClassifyDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static TpNewsClassify toTpNewsClassify(TpNewsClassifyRequest request) {
        TpNewsClassify entity = new TpNewsClassify();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }

    public static TpNewsClassify toTpNewsClassify(TpNewsClassifyDTO dto) {
        TpNewsClassify entity = new TpNewsClassify();
        BeanUtil.copyProperties(dto, entity);
        return entity;
    }

    public static List<TpNewsClassifyDTO> toTpNewsClassifyDTOList(List<TpNewsClassify> entityList) {
        List<TpNewsClassifyDTO> dtoList = entityList.stream().map(e -> TpNewsClassifyToDTO.toTpNewsClassifyDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<TpNewsClassify> toTpNewsClassifyList(List<TpNewsClassifyDTO> dtoList) {
        List<TpNewsClassify> entityList = dtoList.stream().map(e -> TpNewsClassifyToDTO.toTpNewsClassify(e)).collect(Collectors.toList());
        return entityList;
    }
}
