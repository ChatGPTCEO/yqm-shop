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
import com.yqm.common.dto.TpPhotoAlbumDTO;
import com.yqm.common.entity.TpPhotoAlbum;
import com.yqm.common.request.TpPhotoAlbumRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: weiximei
 * @Date: 2021/11/7 18:48
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class TpPhotoAlbumToDTO {

    public static TpPhotoAlbumDTO toTpPhotoAlbumDTO(TpPhotoAlbum entity) {
        TpPhotoAlbumDTO dto = new TpPhotoAlbumDTO();
        BeanUtil.copyProperties(entity, dto);
        return dto;
    }

    public static TpPhotoAlbum toTpPhotoAlbum(TpPhotoAlbumRequest request) {
        TpPhotoAlbum entity = new TpPhotoAlbum();
        BeanUtil.copyProperties(request, entity);
        return entity;
    }

    public static TpPhotoAlbum toTpPhotoAlbum(TpPhotoAlbumDTO dto) {
        TpPhotoAlbum entity = new TpPhotoAlbum();
        BeanUtil.copyProperties(dto, entity);
        return entity;
    }

    public static List<TpPhotoAlbumDTO> toTpPhotoAlbumDTOList(List<TpPhotoAlbum> entityList) {
        List<TpPhotoAlbumDTO> dtoList = entityList.stream().map(e -> TpPhotoAlbumToDTO.toTpPhotoAlbumDTO(e)).collect(Collectors.toList());
        return dtoList;
    }

    public static List<TpPhotoAlbum> toTpPhotoAlbumList(List<TpPhotoAlbumDTO> dtoList) {
        List<TpPhotoAlbum> entityList = dtoList.stream().map(e -> TpPhotoAlbumToDTO.toTpPhotoAlbum(e)).collect(Collectors.toList());
        return entityList;
    }
}
