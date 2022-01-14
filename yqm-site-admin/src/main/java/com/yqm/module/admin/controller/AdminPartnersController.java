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
import com.yqm.common.dto.TpPartnersDTO;
import com.yqm.common.request.TpPartnersRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.PartnersService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-合作伙伴
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/partners")
@RestController
public class AdminPartnersController {

    private final PartnersService partnersClassifyService;

    public AdminPartnersController(PartnersService partnersClassifyService) {
        this.partnersClassifyService = partnersClassifyService;
    }

    /**
     * 添加合作伙伴
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpPartnersDTO> addRecruitment(@RequestBody TpPartnersRequest request) {
        TpPartnersDTO dto = partnersClassifyService.savePartners(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改合作伙伴
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpPartnersDTO> updateRecruitment(@RequestBody TpPartnersRequest request) {
        TpPartnersDTO dto = partnersClassifyService.savePartners(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除合作伙伴
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removePartners(@PathVariable("id") String id) {
        String removeId = partnersClassifyService.removePartners(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询合作伙伴
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpPartnersDTO> getById(@PathVariable("id") String id) {
        TpPartnersDTO dto = partnersClassifyService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询合作伙伴
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpPartnersDTO>> pageRecruitment(TpPartnersRequest request) {
        IPage<TpPartnersDTO> page = partnersClassifyService.pagePartners(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 合作伙伴
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpPartnersRequest request) {
        String enableId = partnersClassifyService.enablePartners(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 合作伙伴
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpPartnersRequest request) {
        String enableId = partnersClassifyService.top(request.getId());
        return ResponseBean.success(enableId);
    }

}
