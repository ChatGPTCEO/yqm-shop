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
import com.yqm.common.dto.TpPartnersClassifyDTO;
import com.yqm.common.request.TpPartnersClassifyRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminPartnersClassifyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-合作伙伴分类
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/api/admin/partnersClassify")
@RestController
public class AdminPartnersClassifyController {

    private final AdminPartnersClassifyService linkClassifyService;

    public AdminPartnersClassifyController(AdminPartnersClassifyService linkClassifyService) {
        this.linkClassifyService = linkClassifyService;
    }

    /**
     * 添加合作伙伴分类
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpPartnersClassifyDTO> addRecruitment(@RequestBody TpPartnersClassifyRequest request) {
        TpPartnersClassifyDTO dto = linkClassifyService.savePartnersClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改合作伙伴分类
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpPartnersClassifyDTO> updateRecruitment(@RequestBody TpPartnersClassifyRequest request) {
        TpPartnersClassifyDTO dto = linkClassifyService.savePartnersClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除合作伙伴分类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removePartnersClassify(@PathVariable("id") String id) {
        String removeId = linkClassifyService.removePartnersClassify(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询合作伙伴分类
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpPartnersClassifyDTO> getById(@PathVariable("id") String id) {
        TpPartnersClassifyDTO dto = linkClassifyService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询合作伙伴分类
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpPartnersClassifyDTO>> pageRecruitment(TpPartnersClassifyRequest request) {
        IPage<TpPartnersClassifyDTO> page = linkClassifyService.pagePartnersClassify(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询合作伙伴分类
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean<List<TpPartnersClassifyDTO>> listPartnersClassify(TpPartnersClassifyRequest request) {
        List<TpPartnersClassifyDTO> list = linkClassifyService.listPartnersClassify(request);
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 合作伙伴分类
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpPartnersClassifyRequest request) {
        String enableId = linkClassifyService.enablePartnersClassify(request);
        return ResponseBean.success(enableId);
    }

}
