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
import com.yqm.common.dto.TpPagesDTO;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminPagesService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-页面
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/api/admin/pages")
@RestController
public class AdminPagesController {

    private final AdminPagesService adminPagesService;

    public AdminPagesController(AdminPagesService adminPagesService) {
        this.adminPagesService = adminPagesService;
    }

    /**
     * 添加页面
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpPagesDTO> addRecruitment(@RequestBody TpPagesRequest request) {
        TpPagesDTO dto = adminPagesService.savePages(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改页面
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpPagesDTO> updateRecruitment(@RequestBody TpPagesRequest request) {
        TpPagesDTO dto = adminPagesService.savePages(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除页面
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removePages(@PathVariable("id") String id) {
        String removeId = adminPagesService.removePages(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询页面
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpPagesDTO> getById(@PathVariable("id") String id) {
        TpPagesDTO dto = adminPagesService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询页面
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpPagesDTO>> pageRecruitment(TpPagesRequest request) {
        IPage<TpPagesDTO> page = adminPagesService.pagePages(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 页面
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpPagesRequest request) {
        String enableId = adminPagesService.enablePages(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 页面
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpPagesRequest request) {
        String enableId = adminPagesService.top(request.getId());
        return ResponseBean.success(enableId);
    }

    /**
     * 修改 页面 SEO
     *
     * @param request
     * @return
     */
    @PutMapping("/seo")
    public ResponseBean<String> seo(@RequestBody TpPagesRequest request) {
        String enableId = adminPagesService.updateSEO(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 导航
     *
     * @param request
     * @return
     */
    @GetMapping("/navigator/page")
    public ResponseBean<IPage<TpPagesDTO>> pagePagesNavigator(TpPagesRequest request) {
        IPage<TpPagesDTO> page = adminPagesService.pagePagesNavigator(request);
        return ResponseBean.success(page);
    }
}
