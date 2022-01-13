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
import com.yqm.common.dto.TpDomainInfoDTO;
import com.yqm.common.dto.TpSiteBingDomainDTO;
import com.yqm.common.dto.TpSiteDTO;
import com.yqm.common.request.TpSiteRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.SiteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-站点
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/site")
@RestController
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    /**
     * 添加站点
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpSiteDTO> addRecruitment(@RequestBody TpSiteRequest request) {
        TpSiteDTO dto = siteService.saveSite(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改站点
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpSiteDTO> updateRecruitment(@RequestBody TpSiteRequest request) {
        TpSiteDTO dto = siteService.saveSite(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除站点
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removeSite(@PathVariable("id") String id) {
        String removeId = siteService.removeSite(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询站点
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpSiteDTO> getById(@PathVariable("id") String id) {
        TpSiteDTO dto = siteService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询站点
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpSiteDTO>> pageRecruitment(TpSiteRequest request) {
        IPage<TpSiteDTO> page = siteService.pageSite(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询站点 所有
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean<List<TpSiteBingDomainDTO>> listSiteSelect(TpSiteRequest request) {
        List<TpSiteBingDomainDTO> list = siteService.listSiteSelect(request);
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 站点
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpSiteRequest request) {
        String enableId = siteService.enableSite(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 站点
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpSiteRequest request) {
        String enableId = siteService.top(request.getId());
        return ResponseBean.success(enableId);
    }

    /**
     * 分页 绑定域名
     *
     * @param request
     * @return
     */
    @GetMapping("/page/user/domain")
    public ResponseBean<IPage<TpSiteBingDomainDTO>> getBingDomainSite(TpSiteRequest request) {
        IPage<TpSiteBingDomainDTO> page = siteService.getBingDomainSite(request);
        return ResponseBean.success(page);
    }

    /**
     * 绑定域名
     *
     * @param request
     * @return
     */
    @PostMapping("/ding/domain")
    public ResponseBean<String> bingDomainSite(@RequestBody TpSiteRequest request) {
        String id = siteService.bingDomainSite(request);
        return ResponseBean.success(id);
    }

    /**
     * 删除域名
     *
     * @param id
     * @return
     */
    @DeleteMapping("/ding/domain/{id}")
    public ResponseBean<String> removeBingDomainSite(@PathVariable("id") String id) {
        String removeId = siteService.removeBingDomainSite(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 域名详情
     *
     * @param id
     * @return
     */
    @GetMapping("/ding/domain/{id}")
    public ResponseBean<TpDomainInfoDTO> domainInfo(@PathVariable("id") String id) {
        TpDomainInfoDTO dto = siteService.domainInfo(id);
        return ResponseBean.success(dto);
    }

    /**
     * 修改域名详情
     *
     * @param request
     * @return
     */
    @PutMapping("/ding/domain")
    public ResponseBean<TpDomainInfoDTO> domainInfo(@RequestBody TpSiteRequest request) {
        TpDomainInfoDTO dto = siteService.updateDomainInfo(request);
        return ResponseBean.success(dto);
    }

}
