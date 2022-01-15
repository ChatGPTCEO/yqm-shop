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
import com.yqm.common.dto.TpNewsDTO;
import com.yqm.common.request.TpNewsRequest;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminNewsService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-新闻
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/news")
@RestController
public class AdminNewsController {

    private final AdminNewsService adminNewsService;

    public AdminNewsController(AdminNewsService adminNewsService) {
        this.adminNewsService = adminNewsService;
    }

    /**
     * 添加新闻
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpNewsDTO> addRecruitment(@RequestBody TpNewsRequest request) {
        TpNewsDTO dto = adminNewsService.saveNews(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改新闻
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpNewsDTO> updateRecruitment(@RequestBody TpNewsRequest request) {
        TpNewsDTO dto = adminNewsService.saveNews(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除新闻
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removeNews(@PathVariable("id") String id) {
        String removeId = adminNewsService.removeNews(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询新闻
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpNewsDTO> getById(@PathVariable("id") String id) {
        TpNewsDTO dto = adminNewsService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询新闻
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpNewsDTO>> pageRecruitment(TpNewsRequest request) {
        IPage<TpNewsDTO> page = adminNewsService.pageNews(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 新闻
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpNewsRequest request) {
        String enableId = adminNewsService.enableNews(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 新闻
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpNewsRequest request) {
        String enableId = adminNewsService.top(request.getId());
        return ResponseBean.success(enableId);
    }

    /**
     * 修改 SEO
     *
     * @param request
     * @return
     */
    @PutMapping("/seo")
    public ResponseBean<String> seo(@RequestBody TpPagesRequest request) {
        String enableId = adminNewsService.updateSEO(request);
        return ResponseBean.success(enableId);
    }

}
