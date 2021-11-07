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
import com.yqm.common.dto.TpLinkClassifyDTO;
import com.yqm.common.dto.TpLinkDTO;
import com.yqm.common.entity.TpLink;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.request.TpLinkRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2021/11/7 19:43
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class TpLinkToDTO {

    public static TpLinkDTO toTpLinkDTO(TpLink entity) {
        TpLinkDTO dto = new TpLinkDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static TpLink toTpLink(TpLinkRequest request) {
        TpLink entity = new TpLink();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }


    public static List<TpLinkDTO> toTpLinkDTOList(List<TpLink> entityList) {
        List<TpLinkDTO> dtoList = entityList.stream().map(e -> TpLinkToDTO.toTpLinkDTO(e)).collect(Collectors.toList());
        return dtoList;
    }
}
