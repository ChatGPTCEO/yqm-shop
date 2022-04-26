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

package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.yqm.common.conversion.YqmRefundPayToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmRefundPayDTO;
import com.yqm.common.entity.YqmRefundPay;
import com.yqm.common.request.YqmRefundPayRequest;
import com.yqm.common.service.IYqmRefundPayService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 退款
 *
 * @Author: weiximei
 * @Date: 2022/4/26 21:51
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminRefundPayService {


    private final IYqmRefundPayService iYqmRefundPayService;

    public AdminRefundPayService(IYqmRefundPayService iYqmRefundPayService) {
        this.iYqmRefundPayService = iYqmRefundPayService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmRefundPayDTO> page(YqmRefundPayRequest request) {
        Page<YqmRefundPay> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmRefundPayService.page(page, iYqmRefundPayService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmRefundPayToDTO.toYqmRefundPayDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmRefundPayDTO> list(YqmRefundPayRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmRefundPay> list = iYqmRefundPayService.list(iYqmRefundPayService.getQuery(request));
        return YqmRefundPayToDTO.toYqmRefundPayDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmRefundPayDTO getById(String id) {
        YqmRefundPay entity = iYqmRefundPayService.getById(id);
        return YqmRefundPayToDTO.toYqmRefundPayDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmRefundPayDTO save(YqmRefundPayRequest request) {
        User user = UserInfoService.getUser();
        YqmRefundPay entity = YqmRefundPayToDTO.toYqmRefundPay(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmRefundPayService.saveOrUpdate(entity);
        return YqmRefundPayToDTO.toYqmRefundPayDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmRefundPay brand = iYqmRefundPayService.getById(id);
        if (Objects.isNull(brand)) {
            return id;
        }
        brand.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmRefundPayToDTO.toYqmRefundPayRequest(brand));
        return id;
    }


    /**
     * 状态统计
     *
     * @return
     */
    public Map<String, Long> getRefundPayListStatistics() {

        // 所有
        YqmRefundPayRequest allRequest = new YqmRefundPayRequest();
        allRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        Long all = iYqmRefundPayService.count(iYqmRefundPayService.getQuery(allRequest));

        //待处理
        YqmRefundPayRequest waitProcessingRequest = new YqmRefundPayRequest();
        waitProcessingRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        waitProcessingRequest.setRefundStatus(YqmDefine.RefundPayType.wait_processing.getValue());
        Long waitProcessing = iYqmRefundPayService.count(iYqmRefundPayService.getQuery(waitProcessingRequest));

        // 已处理
        YqmRefundPayRequest completeProcessingRequest = new YqmRefundPayRequest();
        completeProcessingRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        completeProcessingRequest.setRefundStatus(YqmDefine.RefundPayType.complete_processing.getValue());
        Long completeProcessing = iYqmRefundPayService.count(iYqmRefundPayService.getQuery(completeProcessingRequest));

        // 已拒绝
        YqmRefundPayRequest refusedRequest = new YqmRefundPayRequest();
        refusedRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        refusedRequest.setRefundStatus(YqmDefine.RefundPayType.refused.getValue());
        Long refused = iYqmRefundPayService.count(iYqmRefundPayService.getQuery(refusedRequest));

        Map<String, Long> returnMap = Maps.newHashMap();
        returnMap.put("all", all);
        returnMap.put("waitProcessing", waitProcessing);
        returnMap.put("completeProcessing", completeProcessing);
        returnMap.put("refused", refused);


        return returnMap;
    }

}
