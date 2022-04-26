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
import com.yqm.common.dto.YqmRefundPayDTO;
import com.yqm.common.request.YqmRefundPayRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminRefundPayService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端-退款
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:37
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/api/admin/refundPay")
@RestController
public class AdminRefundPayController {

    private final AdminRefundPayService adminRefundPayService;

    public AdminRefundPayController(AdminRefundPayService adminRefundPayService) {
        this.adminRefundPayService = adminRefundPayService;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean list(YqmRefundPayRequest request) {
        return ResponseBean.success(adminRefundPayService.list(request));
    }

    /**
     * 查询分页
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public ResponseBean getPage(YqmRefundPayRequest request) {
        IPage page = adminRefundPayService.page(request);
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
        YqmRefundPayDTO dto = adminRefundPayService.getById(id);
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
        String removeId = adminRefundPayService.deleteById(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 保存
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean save(@RequestBody YqmRefundPayRequest request) {
        YqmRefundPayDTO dto = adminRefundPayService.save(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean update(@RequestBody YqmRefundPayRequest request) {
        YqmRefundPayDTO dto = adminRefundPayService.save(request);
        return ResponseBean.success(dto);
    }


    /**
     * 列表统计
     *
     * @return
     */
    @GetMapping("/getRefundPayListStatistics")
    public ResponseBean getRefundPayListStatistics() {
        Map<String, Long> dto = adminRefundPayService.getRefundPayListStatistics();
        return ResponseBean.success(dto);
    }

}
