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
import com.yqm.common.dto.TpNewsClassifyDTO;
import com.yqm.common.request.TpNewsClassifyRequest;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.NewsClassifyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-新闻分类
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/newsClassify")
@RestController
public class AdminNewsClassifyController {

    private final NewsClassifyService newsClassifyClassifyService;

    public AdminNewsClassifyController(NewsClassifyService newsClassifyClassifyService) {
        this.newsClassifyClassifyService = newsClassifyClassifyService;
    }

    /**
     * 添加新闻分类
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpNewsClassifyDTO> addRecruitment(@RequestBody TpNewsClassifyRequest request) {
        TpNewsClassifyDTO dto = newsClassifyClassifyService.saveNewsClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改新闻分类
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpNewsClassifyDTO> updateRecruitment(@RequestBody TpNewsClassifyRequest request) {
        TpNewsClassifyDTO dto = newsClassifyClassifyService.saveNewsClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除新闻分类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removeNewsClassify(@PathVariable("id") String id) {
        String removeId = newsClassifyClassifyService.removeNewsClassify(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询新闻分类
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpNewsClassifyDTO> getById(@PathVariable("id") String id) {
        TpNewsClassifyDTO dto = newsClassifyClassifyService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询新闻分类
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpNewsClassifyDTO>> pageRecruitment(TpNewsClassifyRequest request) {
        IPage<TpNewsClassifyDTO> page = newsClassifyClassifyService.pageNewsClassify(request);
        return ResponseBean.success(page);
    }

    /**
     * 分页查询新闻分类 直接列表形式
     *
     * @param request
     * @return
     */
    @GetMapping("/page/list")
    public ResponseBean<IPage<TpNewsClassifyDTO>> pageNewsClassifyList(TpNewsClassifyRequest request) {
        IPage<TpNewsClassifyDTO> page = newsClassifyClassifyService.pageNewsClassifyList(request);
        return ResponseBean.success(page);
    }

    /**
     * 分页查询新闻分类
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean<List<TpNewsClassifyDTO>> listRecruitment(TpNewsClassifyRequest request) {
        List<TpNewsClassifyDTO> list = newsClassifyClassifyService.getNewsClassify();
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 新闻分类
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpNewsClassifyRequest request) {
        String enableId = newsClassifyClassifyService.enableNewsClassify(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 新闻分类
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpNewsClassifyRequest request) {
        String enableId = newsClassifyClassifyService.top(request.getId());
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
        String enableId = newsClassifyClassifyService.updateSEO(request);
        return ResponseBean.success(enableId);
    }

}
