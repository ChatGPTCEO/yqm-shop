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
import com.yqm.common.dto.TpLinkClassifyDTO;
import com.yqm.common.request.TpLinkClassifyRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.LinkClassifyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-友情链接分类
 * 
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/linkClassify")
@RestController
public class AdminLinkClassifyController {

    private final LinkClassifyService linkClassifyService;

    public AdminLinkClassifyController(LinkClassifyService linkClassifyService) {
        this.linkClassifyService = linkClassifyService;
    }

    /**
     * 添加友情链接分类
     * 
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpLinkClassifyDTO> addRecruitment(@RequestBody TpLinkClassifyRequest request) {
        TpLinkClassifyDTO dto = linkClassifyService.saveLinkClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改友情链接分类
     * 
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpLinkClassifyDTO> updateRecruitment(@RequestBody TpLinkClassifyRequest request) {
        TpLinkClassifyDTO dto = linkClassifyService.saveLinkClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除友情链接分类
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removeLinkClassify(@PathVariable("id") String id) {
        String removeId = linkClassifyService.removeLinkClassify(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询友情链接分类
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpLinkClassifyDTO> getById(@PathVariable("id") String id) {
        TpLinkClassifyDTO dto = linkClassifyService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询友情链接分类
     * 
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpLinkClassifyDTO>> pageRecruitment(TpLinkClassifyRequest request) {
        IPage<TpLinkClassifyDTO> page = linkClassifyService.pageLinkClassify(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询友情链接分类
     * 
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean<List<TpLinkClassifyDTO>> listLinkClassify(TpLinkClassifyRequest request) {
        List<TpLinkClassifyDTO> list = linkClassifyService.listLinkClassify(request);
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 友情链接分类
     * 
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpLinkClassifyRequest request) {
        String enableId = linkClassifyService.enableLinkClassify(request);
        return ResponseBean.success(enableId);
    }

}
