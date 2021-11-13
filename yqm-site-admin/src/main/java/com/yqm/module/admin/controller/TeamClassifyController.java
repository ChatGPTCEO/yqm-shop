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
import com.yqm.common.dto.TpTeamClassifyDTO;
import com.yqm.common.request.TpTeamClassifyRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.TeamClassifyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-团队分类
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/teamClassify")
@RestController
public class TeamClassifyController {

    private final TeamClassifyService teamClassifyService;

    public TeamClassifyController(TeamClassifyService teamClassifyService) {
        this.teamClassifyService = teamClassifyService;
    }

    /**
     * 添加团队分类
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean addRecruitment(@RequestBody TpTeamClassifyRequest request) {
        TpTeamClassifyDTO dto = teamClassifyService.saveTeamClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改团队分类
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean updateRecruitment(@RequestBody TpTeamClassifyRequest request) {
        TpTeamClassifyDTO dto = teamClassifyService.saveTeamClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除团队分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean removeTeamClassify(@PathVariable("id") String  id) {
        String removeId = teamClassifyService.removeTeamClassify(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询团队分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean getById(@PathVariable("id") String  id) {
        TpTeamClassifyDTO dto = teamClassifyService.getById(id);
        return ResponseBean.success(dto);
    }


    /**
     * 分页查询团队分类
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean pageRecruitment(TpTeamClassifyRequest request) {
        IPage<TpTeamClassifyDTO> page = teamClassifyService.pageTeamClassify(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询团队分类
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean listTeamClassify(TpTeamClassifyRequest request) {
        List<TpTeamClassifyDTO> list = teamClassifyService.listTeamClassify(request);
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 团队分类
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean enableRecruitment(@RequestBody TpTeamClassifyRequest request) {
        String enableId = teamClassifyService.enableTeamClassify(request);
        return ResponseBean.success(enableId);
    }


}
