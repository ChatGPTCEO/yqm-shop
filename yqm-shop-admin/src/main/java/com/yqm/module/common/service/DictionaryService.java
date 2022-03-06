/*
 *  Copyright  2022 Wei xi mei
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.yqm.module.common.service;

import com.yqm.common.conversion.YqmDictionaryToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmDictionaryDTO;
import com.yqm.common.entity.YqmDictionary;
import com.yqm.common.request.YqmDictionaryRequest;
import com.yqm.common.service.IYqmDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2022/3/6 16:53
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class DictionaryService {

    private final IYqmDictionaryService iYqmDictionaryService;

    public DictionaryService(IYqmDictionaryService iYqmDictionaryService) {
        this.iYqmDictionaryService = iYqmDictionaryService;
    }

    public List<YqmDictionaryDTO> list(YqmDictionaryRequest request) {
        request.setStatusList(YqmDefine.includeStatus);
        List<YqmDictionary> list = iYqmDictionaryService.list(iYqmDictionaryService.getQuery(request));
        return YqmDictionaryToDTO.toYqmDictionaryDTOList(list);
    }

    /**
     * 运费模板-费用计算方式
     *
     * @return
     */
    public List<YqmDictionaryDTO> priceTypeList() {
        YqmDictionaryRequest request = new YqmDictionaryRequest();
        request.setPcode(YqmDefine.DictionaryType.priceType.getValue());
        return this.list(request);
    }
}
