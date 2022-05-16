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

import com.alibaba.fastjson.JSONObject;
import com.yqm.common.dto.YqmRefundGoodsDTO;
import com.yqm.common.entity.YqmRefundGoods;
import com.yqm.common.request.YqmRefundGoodsRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
public class YqmRefundGoodsToDTO {

    public static YqmRefundGoodsDTO toYqmRefundGoodsDTO(YqmRefundGoods entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmRefundGoodsDTO dto = new YqmRefundGoodsDTO();
        BeanUtils.copyProperties(entity, dto);
        if (StringUtils.isNotBlank(entity.getImgs())) {
            dto.setImgList(JSONObject.parseArray(entity.getImgs(), String.class));
        }
        return dto;
    }

    public static YqmRefundGoods toYqmRefundGoods(YqmRefundGoodsDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        YqmRefundGoods entity = new YqmRefundGoods();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }

    public static YqmRefundGoodsRequest toYqmRefundGoodsRequest(YqmRefundGoods entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        YqmRefundGoodsRequest brandRequest = new YqmRefundGoodsRequest();
        BeanUtils.copyProperties(entity, brandRequest);
        return brandRequest;
    }

    public static YqmRefundGoods toYqmRefundGoods(YqmRefundGoodsRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        YqmRefundGoods entity = new YqmRefundGoods();
        BeanUtils.copyProperties(request, entity);
        return entity;
    }

    public static List<YqmRefundGoodsDTO> toYqmRefundGoodsDTOList(List<YqmRefundGoods> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        List<YqmRefundGoodsDTO> dtoList = entityList.stream().map(e -> toYqmRefundGoodsDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<YqmRefundGoods> toYqmRefundGoodsList(List<YqmRefundGoodsDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<YqmRefundGoods> entityList = dtoList.stream().map(e -> toYqmRefundGoods(e)).collect(Collectors.toList());
        return entityList;
    }

}
