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
import com.yqm.module.admin.service.NewsService;
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
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * 添加新闻
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean addRecruitment(@RequestBody TpNewsRequest request) {
        TpNewsDTO dto = newsService.saveNews(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改新闻
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean updateRecruitment(@RequestBody TpNewsRequest request) {
        TpNewsDTO dto = newsService.saveNews(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除新闻
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean removeNews(@PathVariable("id") String id) {
        String removeId = newsService.removeNews(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询新闻
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean getById(@PathVariable("id") String id) {
        TpNewsDTO dto = newsService.getById(id);
        return ResponseBean.success(dto);
    }


    /**
     * 分页查询新闻
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean pageRecruitment(TpNewsRequest request) {
        IPage<TpNewsDTO> page = newsService.pageNews(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 新闻
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean enableRecruitment(@RequestBody TpNewsRequest request) {
        String enableId = newsService.enableNews(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 新闻
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean top(@RequestBody TpNewsRequest request) {
        String enableId = newsService.top(request.getId());
        return ResponseBean.success(enableId);
    }

    /**
     * 修改 SEO
     *
     * @param request
     * @return
     */
    @PutMapping("/seo")
    public ResponseBean seo(@RequestBody TpPagesRequest request) {
        String enableId = newsService.updateSEO(request);
        return ResponseBean.success(enableId);
    }


}
