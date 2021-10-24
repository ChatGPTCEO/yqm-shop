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

package com.yqm.module.common;

import com.yqm.common.conversion.TpRegionToDTO;
import com.yqm.common.dto.TpRegionDTO;
import com.yqm.common.entity.TpRegion;
import com.yqm.common.response.ResponseBean;
import com.yqm.common.service.ITpRegionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 省市区 控制器
 *
 * @Author: weiximei
 * @Date: 2021/10/24 21:38
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private ITpRegionService iTpRegionService;

    /**
     * 获取省市区
     * @param pCode
     * @return
     */
    @GetMapping("/provinces")
    public ResponseBean provinces(@RequestParam("pCode") Integer pCode) {
        List<TpRegion> list = iTpRegionService.getProvinces(pCode);
        List<TpRegionDTO> regionDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            regionDTOList = list.stream().map(e -> TpRegionToDTO.toTpCompanyDTO(e)).collect(Collectors.toList());
        }
        return ResponseBean.success(regionDTOList);
    }

}
