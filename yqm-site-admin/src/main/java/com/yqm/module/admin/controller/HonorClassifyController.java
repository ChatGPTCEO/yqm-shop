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
import com.yqm.common.dto.TpHonorClassifyDTO;
import com.yqm.common.request.TpHonorClassifyRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.HonorClassifyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-荣誉证书分类
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/honorClassify")
@RestController
public class HonorClassifyController {

    private final HonorClassifyService honorClassifyService;

    public HonorClassifyController(HonorClassifyService honorClassifyService) {
        this.honorClassifyService = honorClassifyService;
    }

    /**
     * 添加荣誉证书分类
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean addRecruitment(@RequestBody TpHonorClassifyRequest request) {
        TpHonorClassifyDTO dto = honorClassifyService.saveHonorClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改荣誉证书分类
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean updateRecruitment(@RequestBody TpHonorClassifyRequest request) {
        TpHonorClassifyDTO dto = honorClassifyService.saveHonorClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除荣誉证书分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean removeHonorClassify(@PathVariable("id") String  id) {
        String removeId = honorClassifyService.removeHonorClassify(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询荣誉证书分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean getById(@PathVariable("id") String  id) {
        TpHonorClassifyDTO dto = honorClassifyService.getById(id);
        return ResponseBean.success(dto);
    }


    /**
     * 分页查询荣誉证书分类
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean pageRecruitment(TpHonorClassifyRequest request) {
        IPage<TpHonorClassifyDTO> page = honorClassifyService.pageHonorClassify(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询荣誉证书分类
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean listHonorClassify(TpHonorClassifyRequest request) {
        List<TpHonorClassifyDTO> list = honorClassifyService.listHonorClassify(request);
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 荣誉证书分类
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean enableRecruitment(@RequestBody TpHonorClassifyRequest request) {
        String enableId = honorClassifyService.enableHonorClassify(request);
        return ResponseBean.success(enableId);
    }


}
