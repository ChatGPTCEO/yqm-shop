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
import com.yqm.module.admin.service.PagesService;
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
@RequestMapping("/admin/pages")
@RestController
public class PagesController {

    private final PagesService pagesService;

    public PagesController(PagesService pagesService) {
        this.pagesService = pagesService;
    }

    /**
     * 添加页面
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean addRecruitment(@RequestBody TpPagesRequest request) {
        TpPagesDTO dto = pagesService.savePages(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改页面
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean updateRecruitment(@RequestBody TpPagesRequest request) {
        TpPagesDTO dto = pagesService.savePages(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除页面
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean removePages(@PathVariable("id") String id) {
        String removeId = pagesService.removePages(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询页面
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean getById(@PathVariable("id") String id) {
        TpPagesDTO dto = pagesService.getById(id);
        return ResponseBean.success(dto);
    }


    /**
     * 分页查询页面
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean pageRecruitment(TpPagesRequest request) {
        IPage<TpPagesDTO> page = pagesService.pagePages(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 页面
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean enableRecruitment(@RequestBody TpPagesRequest request) {
        String enableId = pagesService.enablePages(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 页面
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean top(@RequestBody TpPagesRequest request) {
        String enableId = pagesService.top(request.getId());
        return ResponseBean.success(enableId);
    }

    /**
     * 修改 页面 SEO
     *
     * @param request
     * @return
     */
    @PutMapping("/seo")
    public ResponseBean seo(@RequestBody TpPagesRequest request) {
        String enableId = pagesService.updateSEO(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 导航
     *
     * @param request
     * @return
     */
    @GetMapping("/navigator/page")
    public ResponseBean pagePagesNavigator(TpPagesRequest request) {
        IPage<TpPagesDTO> page = pagesService.pagePagesNavigator(request);
        return ResponseBean.success(page);
    }
}
