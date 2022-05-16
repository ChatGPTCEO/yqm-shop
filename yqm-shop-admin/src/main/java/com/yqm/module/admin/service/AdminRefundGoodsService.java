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
import com.yqm.common.conversion.YqmRefundGoodsToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmRefundGoodsDTO;
import com.yqm.common.entity.YqmRefundGoods;
import com.yqm.common.event.refundGoods.ConfirmRefundGoodsEvent;
import com.yqm.common.event.refundGoods.RefusedRefundGoodsEvent;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmRefundGoodsRequest;
import com.yqm.common.service.IYqmRefundGoodsService;
import com.yqm.module.admin.service.aggregation.AdminProductAggregation;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 退货
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
public class AdminRefundGoodsService {


    private final IYqmRefundGoodsService iYqmRefundGoodsService;

    private final ApplicationContext applicationContext;

    private final AdminProductAggregation adminProductAggregation;

    public AdminRefundGoodsService(IYqmRefundGoodsService iYqmRefundGoodsService, ApplicationContext applicationContext, AdminProductAggregation adminProductAggregation) {
        this.iYqmRefundGoodsService = iYqmRefundGoodsService;
        this.applicationContext = applicationContext;
        this.adminProductAggregation = adminProductAggregation;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmRefundGoodsDTO> page(YqmRefundGoodsRequest request) {
        Page<YqmRefundGoods> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmRefundGoodsService.page(page, iYqmRefundGoodsService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmRefundGoodsToDTO.toYqmRefundGoodsDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmRefundGoodsDTO> list(YqmRefundGoodsRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmRefundGoods> list = iYqmRefundGoodsService.list(iYqmRefundGoodsService.getQuery(request));
        return YqmRefundGoodsToDTO.toYqmRefundGoodsDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmRefundGoodsDTO getById(String id) {
        YqmRefundGoods entity = iYqmRefundGoodsService.getById(id);
        YqmRefundGoodsDTO refundGoodsDTO = YqmRefundGoodsToDTO.toYqmRefundGoodsDTO(entity);
        return refundGoodsDTO;
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmRefundGoodsDTO save(YqmRefundGoodsRequest request) {
        User user = UserInfoService.getUser();
        YqmRefundGoods entity = YqmRefundGoodsToDTO.toYqmRefundGoods(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmRefundGoodsService.saveOrUpdate(entity);
        return YqmRefundGoodsToDTO.toYqmRefundGoodsDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmRefundGoods refundPay = iYqmRefundGoodsService.getById(id);
        if (Objects.isNull(refundPay)) {
            return id;
        }
        refundPay.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmRefundGoodsToDTO.toYqmRefundGoodsRequest(refundPay));
        return id;
    }


    /**
     * 状态统计
     *
     * @return
     */
    public Map<String, Long> getRefundGoodsListStatistics() {

        // 所有
        YqmRefundGoodsRequest allRequest = new YqmRefundGoodsRequest();
        allRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        Long all = iYqmRefundGoodsService.count(iYqmRefundGoodsService.getQuery(allRequest));

        //待处理
        YqmRefundGoodsRequest waitProcessingRequest = new YqmRefundGoodsRequest();
        waitProcessingRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        waitProcessingRequest.setRefundStatus(YqmDefine.RefundGoodsType.wait_processing.getValue());
        Long waitProcessing = iYqmRefundGoodsService.count(iYqmRefundGoodsService.getQuery(waitProcessingRequest));

        // 已处理
        YqmRefundGoodsRequest completeProcessingRequest = new YqmRefundGoodsRequest();
        completeProcessingRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        completeProcessingRequest.setRefundStatus(YqmDefine.RefundGoodsType.complete_processing.getValue());
        Long completeProcessing = iYqmRefundGoodsService.count(iYqmRefundGoodsService.getQuery(completeProcessingRequest));

        // 已拒绝
        YqmRefundGoodsRequest refusedRequest = new YqmRefundGoodsRequest();
        refusedRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        refusedRequest.setRefundStatus(YqmDefine.RefundGoodsType.refused.getValue());
        Long refused = iYqmRefundGoodsService.count(iYqmRefundGoodsService.getQuery(refusedRequest));

        // 处理中
        YqmRefundGoodsRequest processingRequest = new YqmRefundGoodsRequest();
        processingRequest.setStatus(YqmDefine.StatusType.effective.getValue());
        processingRequest.setRefundStatus(YqmDefine.RefundGoodsType.refused.getValue());
        Long processing = iYqmRefundGoodsService.count(iYqmRefundGoodsService.getQuery(processingRequest));

        Map<String, Long> returnMap = Maps.newHashMap();
        returnMap.put("all", all);
        returnMap.put("waitProcessing", waitProcessing);
        returnMap.put("completeProcessing", completeProcessing);
        returnMap.put("refused", refused);
        returnMap.put("processing", processing);


        return returnMap;
    }

    /**
     * 确认退货
     *
     * @param request
     * @return
     */
    public YqmRefundGoodsDTO confirmRefundGoods(YqmRefundGoodsRequest request) {
        User user = UserInfoService.getUser();
        YqmRefundGoods refundPay = iYqmRefundGoodsService.getById(request.getId());
        if (Objects.isNull(refundPay)) {
            throw new YqmException("订单不存在");
        }
        // TODO: 退货逻辑

        refundPay.setRefundStatus(YqmDefine.RefundGoodsType.complete_processing.getValue());
        refundPay.setProcessingNote(request.getProcessingNote());
        refundPay.setProcessingTime(LocalDateTime.now());
        refundPay.setProcessingUserId(user.getId());
        refundPay.setProcessingUserName(user.getUserName());

        iYqmRefundGoodsService.saveOrUpdate(refundPay);

        YqmRefundGoodsDTO dto = YqmRefundGoodsToDTO.toYqmRefundGoodsDTO(refundPay);
        applicationContext.publishEvent(new ConfirmRefundGoodsEvent(dto));
        return dto;
    }

    /**
     * 拒绝退货
     *
     * @param request
     * @return
     */
    public YqmRefundGoodsDTO refusedRefundGoods(YqmRefundGoodsRequest request) {
        User user = UserInfoService.getUser();
        YqmRefundGoods refundPay = iYqmRefundGoodsService.getById(request.getId());
        if (Objects.isNull(refundPay)) {
            throw new YqmException("订单不存在");
        }
        // TODO: 拒绝退货逻辑

        refundPay.setRefundStatus(YqmDefine.RefundGoodsType.refused.getValue());
        refundPay.setProcessingNote(request.getProcessingNote());
        refundPay.setProcessingTime(LocalDateTime.now());
        refundPay.setProcessingUserId(user.getId());
        refundPay.setProcessingUserName(user.getUserName());

        iYqmRefundGoodsService.saveOrUpdate(refundPay);

        YqmRefundGoodsDTO dto = YqmRefundGoodsToDTO.toYqmRefundGoodsDTO(refundPay);
        applicationContext.publishEvent(new RefusedRefundGoodsEvent(dto));
        return dto;
    }


}
