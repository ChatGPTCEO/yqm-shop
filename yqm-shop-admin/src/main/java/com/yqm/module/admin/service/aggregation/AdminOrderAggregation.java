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

package com.yqm.module.admin.service.aggregation;

import com.yqm.common.dto.YqmOrderDTO;
import com.yqm.common.dto.YqmOrderItemDTO;
import com.yqm.common.request.YqmOrderItemRequest;
import com.yqm.module.admin.service.AdminOrderItemService;
import com.yqm.module.admin.service.AdminOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Author: weiximei
 * @Date: 2022/4/2 21:16
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminOrderAggregation {

    private final AdminOrderService orderService;
    private final AdminProductAggregation productAggregation;
    private final AdminOrderItemService orderItemService;

    public AdminOrderAggregation(AdminOrderService orderService, AdminProductAggregation productAggregation, AdminOrderItemService orderItemService) {
        this.orderService = orderService;
        this.productAggregation = productAggregation;
        this.orderItemService = orderItemService;
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    public YqmOrderDTO orderInfo(String id) {
        YqmOrderDTO dto = orderService.getById(id);
        if (Objects.isNull(dto)) {
            return null;
        }

        YqmOrderItemRequest request = new YqmOrderItemRequest();
        request.setOrderId(dto.getId());
        List<YqmOrderItemDTO> orderItemDTOS = orderItemService.list(request);

        for (YqmOrderItemDTO orderItemDTO : orderItemDTOS) {

        }


        return dto;
    }
}
