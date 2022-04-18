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

package com.yqm.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yqm.common.dto.YqmOrderDTO;
import com.yqm.common.request.YqmOrderRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminOrderService;
import com.yqm.module.admin.service.aggregation.AdminOrderAggregation;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-订单
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:37
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/api/admin/order")
@RestController
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final AdminOrderAggregation adminOrderAggregation;

    public AdminOrderController(AdminOrderService adminOrderService, AdminOrderAggregation adminOrderAggregation) {
        this.adminOrderService = adminOrderService;
        this.adminOrderAggregation = adminOrderAggregation;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean list(YqmOrderRequest request) {
        return ResponseBean.success(adminOrderService.list(request));
    }

    /**
     * 查询分页
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public ResponseBean getPage(YqmOrderRequest request) {
        IPage page = adminOrderService.page(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean getById(@PathVariable String id) {
        YqmOrderDTO dto = adminOrderAggregation.orderInfo(id);
        return ResponseBean.success(dto);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public ResponseBean info(@PathVariable String id) {
        YqmOrderDTO dto = adminOrderAggregation.orderInfo(id);
        return ResponseBean.success(dto);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean deleteById(@PathVariable String id) {
        String removeId = adminOrderService.deleteById(id);
        return ResponseBean.success(removeId);
    }

//    /**
//     * 保存
//     *
//     * @param request
//     * @return
//     */
//    @PostMapping("")
//    public ResponseBean save(@RequestBody YqmOrderRequest request) {
//        YqmOrderDTO dto = adminOrderService.save(request);
//        return ResponseBean.success(dto);
//    }
//
//    /**
//     * 修改
//     *
//     * @param request
//     * @return
//     */
//    @PutMapping("")
//    public ResponseBean update(@RequestBody YqmOrderRequest request) {
//        YqmOrderDTO dto = adminOrderService.save(request);
//        return ResponseBean.success(dto);
//    }

}
