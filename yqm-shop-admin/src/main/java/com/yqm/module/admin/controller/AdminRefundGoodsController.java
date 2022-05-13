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
import com.yqm.common.dto.YqmRefundGoodsDTO;
import com.yqm.common.request.YqmRefundGoodsRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminRefundGoodsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理端-退货
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:37
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/api/admin/refundGoods")
@RestController
public class AdminRefundGoodsController {

    private final AdminRefundGoodsService adminRefundGoodsService;

    public AdminRefundGoodsController(AdminRefundGoodsService adminRefundGoodsService) {
        this.adminRefundGoodsService = adminRefundGoodsService;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean list(YqmRefundGoodsRequest request) {
        return ResponseBean.success(adminRefundGoodsService.list(request));
    }

    /**
     * 查询分页
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public ResponseBean getPage(YqmRefundGoodsRequest request) {
        IPage page = adminRefundGoodsService.page(request);
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
        YqmRefundGoodsDTO dto = adminRefundGoodsService.getById(id);
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
        String removeId = adminRefundGoodsService.deleteById(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 保存
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean save(@RequestBody YqmRefundGoodsRequest request) {
        YqmRefundGoodsDTO dto = adminRefundGoodsService.save(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean update(@RequestBody YqmRefundGoodsRequest request) {
        YqmRefundGoodsDTO dto = adminRefundGoodsService.save(request);
        return ResponseBean.success(dto);
    }


    /**
     * 列表统计
     *
     * @return
     */
    @GetMapping("/getRefundGoodsListStatistics")
    public ResponseBean getRefundGoodsListStatistics() {
        Map<String, Long> dto = adminRefundGoodsService.getRefundGoodsListStatistics();
        return ResponseBean.success(dto);
    }

    /**
     * 确认退货
     *
     * @param request
     * @return
     */
    @PutMapping("/confirmRefundGoods")
    public ResponseBean confirmRefundGoods(@RequestBody YqmRefundGoodsRequest request) {
        YqmRefundGoodsDTO dto = adminRefundGoodsService.confirmRefundGoods(request);
        return ResponseBean.success(dto);
    }

    /**
     * 拒绝退货
     *
     * @param request
     * @return
     */
    @PutMapping("/refusedRefundGoods")
    public ResponseBean refusedRefundGoods(@RequestBody YqmRefundGoodsRequest request) {
        YqmRefundGoodsDTO dto = adminRefundGoodsService.refusedRefundGoods(request);
        return ResponseBean.success(dto);
    }

}
