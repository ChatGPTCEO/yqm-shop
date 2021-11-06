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
import com.yqm.common.service.ITpRecruitmentService;
import com.yqm.module.admin.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-招聘
 * @Author: weiximei
 * @Date: 2021/11/6 14:19
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/recruitment")
@RestController
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    public RecruitmentController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    /**
     * 添加招聘
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean addRecruitment(@RequestBody TpRecruitmentRequest request) {
       TpRecruitmentDTO dto = recruitmentService.saveRecruitment(request);
       return ResponseBean.success(dto);
    }

    /**
     * 添加招聘
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean updateRecruitment(@RequestBody TpRecruitmentRequest request) {
        TpRecruitmentDTO dto = recruitmentService.saveRecruitment(request);
        return ResponseBean.success(dto);
    }

    /**
     * 添加招聘
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean getById(@PathVariable("id") String  id) {
        TpRecruitmentDTO dto = recruitmentService.getById(id);
        return ResponseBean.success(dto);
    }


    /**
     * 分页查询招聘
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean pageRecruitment(TpRecruitmentRequest request) {
        IPage<TpRecruitmentDTO> page = recruitmentService.pageRecruitment(request);
        return ResponseBean.success(page);
    }



}
