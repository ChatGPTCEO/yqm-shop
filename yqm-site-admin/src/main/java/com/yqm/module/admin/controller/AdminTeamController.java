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
import com.yqm.common.dto.TpTeamDTO;
import com.yqm.common.request.TpTeamRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminTeamService;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端-团队
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/team")
@RestController
public class AdminTeamController {

    private final AdminTeamService teamClassifyService;

    public AdminTeamController(AdminTeamService teamClassifyService) {
        this.teamClassifyService = teamClassifyService;
    }

    /**
     * 添加团队
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpTeamDTO> addRecruitment(@RequestBody TpTeamRequest request) {
        TpTeamDTO dto = teamClassifyService.saveTeam(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改团队
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpTeamDTO> updateRecruitment(@RequestBody TpTeamRequest request) {
        TpTeamDTO dto = teamClassifyService.saveTeam(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除团队
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removeTeam(@PathVariable("id") String id) {
        String removeId = teamClassifyService.removeTeam(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询团队
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpTeamDTO> getById(@PathVariable("id") String id) {
        TpTeamDTO dto = teamClassifyService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询团队
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpTeamDTO>> pageRecruitment(TpTeamRequest request) {
        IPage<TpTeamDTO> page = teamClassifyService.pageTeam(request);
        return ResponseBean.success(page);
    }

    /**
     * 停用/启用 团队
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpTeamRequest request) {
        String enableId = teamClassifyService.enableTeam(request);
        return ResponseBean.success(enableId);
    }

    /**
     * 置顶 团队
     *
     * @param request
     * @return
     */
    @PutMapping("/top")
    public ResponseBean<String> top(@RequestBody TpTeamRequest request) {
        String enableId = teamClassifyService.top(request.getId());
        return ResponseBean.success(enableId);
    }

}
