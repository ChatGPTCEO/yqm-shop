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
import com.yqm.common.dto.TpHonorDTO;
import com.yqm.common.request.TpHonorRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.HonorService;
import org.springframework.web.bind.annotation.*;

/**
 *
 * 管理端-荣誉证书
 *
 * @Author: weiximei
 * @Date: 2021/11/8 19:57
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RestController
@RequestMapping("/admin/honor")
public class HonorController {

    private final HonorService honorService;

    public HonorController(HonorService honorService) {
        this.honorService = honorService;
    }

    /**
     * 添加荣誉证书
     * 
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpHonorDTO> addRecruitment(@RequestBody TpHonorRequest request) {
        TpHonorDTO dto = honorService.saveHonor(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改荣誉证书
     * 
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpHonorDTO> updateRecruitment(@RequestBody TpHonorRequest request) {
        TpHonorDTO dto = honorService.saveHonor(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除荣誉证书
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removeHonor(@PathVariable("id") String id) {
        String removeId = honorService.removeHonor(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询荣誉证书
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpHonorDTO> getById(@PathVariable("id") String id) {
        TpHonorDTO dto = honorService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询荣誉证书
     * 
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpHonorDTO>> pageRecruitment(TpHonorRequest request) {
        IPage<TpHonorDTO> page = honorService.pageHonor(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 荣誉证书
     * 
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpHonorRequest request) {
        String enableId = honorService.enableHonor(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 荣誉证书
     * 
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpHonorRequest request) {
        String enableId = honorService.top(request.getId());
        return ResponseBean.success(enableId);
    }

}
