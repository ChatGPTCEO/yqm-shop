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
import com.yqm.common.dto.TpPhotoAlbumDTO;
import com.yqm.common.request.TpPhotoAlbumRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.AdminPhotoAlbumService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-相册
 *
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/api/admin/photoAlbum")
@RestController
public class AdminPhotoAlbumController {

    private final AdminPhotoAlbumService adminPhotoAlbumService;

    public AdminPhotoAlbumController(AdminPhotoAlbumService adminPhotoAlbumService) {
        this.adminPhotoAlbumService = adminPhotoAlbumService;
    }

    /**
     * 添加相册
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean<TpPhotoAlbumDTO> addRecruitment(@RequestBody TpPhotoAlbumRequest request) {
        TpPhotoAlbumDTO dto = adminPhotoAlbumService.savePhotoAlbum(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改相册
     *
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean<TpPhotoAlbumDTO> updateRecruitment(@RequestBody TpPhotoAlbumRequest request) {
        TpPhotoAlbumDTO dto = adminPhotoAlbumService.savePhotoAlbum(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除相册
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean<String> removePhotoAlbum(@PathVariable("id") String id) {
        String removeId = adminPhotoAlbumService.removePhotoAlbum(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询相册
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean<TpPhotoAlbumDTO> getById(@PathVariable("id") String id) {
        TpPhotoAlbumDTO dto = adminPhotoAlbumService.getById(id);
        return ResponseBean.success(dto);
    }

    /**
     * 分页查询相册
     *
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean<IPage<TpPhotoAlbumDTO>> pageRecruitment(TpPhotoAlbumRequest request) {
        IPage<TpPhotoAlbumDTO> page = adminPhotoAlbumService.pagePhotoAlbum(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询相册
     *
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean<List<TpPhotoAlbumDTO>> listPhotoAlbum(TpPhotoAlbumRequest request) {
        List<TpPhotoAlbumDTO> list = adminPhotoAlbumService.listPhotoAlbum(request);
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 相册
     *
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean<String> enableRecruitment(@RequestBody TpPhotoAlbumRequest request) {
        String enableId = adminPhotoAlbumService.enablePhotoAlbum(request);
        return ResponseBean.success(enableId);
    }

}
