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

package com.yqm.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yqm.common.dto.TpRecruitmentDTO;
import com.yqm.common.request.TpRecruitmentRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminRecruitmentService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-招聘
 *
 * @Author: weiximei
 * @Date: 2021/11/6 14:19
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/recruitment")
@RestController
public class AdminRecruitmentController {

    private final AdminRecruitmentService adminRecruitmentService;

    public AdminRecruitmentController(AdminRecruitmentService adminRecruitmentService) {
        this.adminRecruitmentService = adminRecruitmentService;
    }

    /**
     * 添加招聘
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpRecruitmentDTO> addRecruitment(@RequestBody TpRecruitmentRequest request) {
        TpRecruitmentDTO dto = adminRecruitmentService.saveRecruitment(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改招聘
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpRecruitmentDTO> updateRecruitment(@RequestBody TpRecruitmentRequest request) {
        TpRecruitmentDTO dto = adminRecruitmentService.saveRecruitment(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除招聘
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> deleteRecruitment(@PathVariable("id") String id) {
        String removeId = adminRecruitmentService.removeRecruitment(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询招聘
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpRecruitmentDTO> getById(@PathVariable("id") String id) {
        TpRecruitmentDTO dto = adminRecruitmentService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询招聘
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpRecruitmentDTO>> pageRecruitment(TpRecruitmentRequest request) {
        IPage<TpRecruitmentDTO> page = adminRecruitmentService.pageRecruitment(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 招聘
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpRecruitmentRequest request) {
        String enableId = adminRecruitmentService.enableRecruitment(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 招聘
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpRecruitmentRequest request) {
        String enableId = adminRecruitmentService.top(request.getId());
        return ResponseBean.success(enableId);
    }

}
