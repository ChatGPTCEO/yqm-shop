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
import com.yqm.common.dto.TpPhotoAlbumClassifyDTO;
import com.yqm.common.request.TpPhotoAlbumClassifyRequest;
import com.yqm.common.response.ResponseBean;
import com.yqm.module.admin.service.PhotoAlbumClassifyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端-相册分类
 * @Author: weiximei
 * @Date: 2021/11/7 19:09
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@RequestMapping("/admin/photoAlbumClassify")
@RestController
public class PhotoAlbumClassifyController {

    private final PhotoAlbumClassifyService photoAlbumClassifyService;

    public PhotoAlbumClassifyController(PhotoAlbumClassifyService photoAlbumClassifyService) {
        this.photoAlbumClassifyService = photoAlbumClassifyService;
    }

    /**
     * 添加相册分类
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseBean addRecruitment(@RequestBody TpPhotoAlbumClassifyRequest request) {
        TpPhotoAlbumClassifyDTO dto = photoAlbumClassifyService.savePhotoAlbumClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 修改相册分类
     * @param request
     * @return
     */
    @PutMapping("")
    public ResponseBean updateRecruitment(@RequestBody TpPhotoAlbumClassifyRequest request) {
        TpPhotoAlbumClassifyDTO dto = photoAlbumClassifyService.savePhotoAlbumClassify(request);
        return ResponseBean.success(dto);
    }

    /**
     * 删除相册分类
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseBean removePhotoAlbumClassify(@PathVariable("id") String  id) {
        String removeId = photoAlbumClassifyService.removePhotoAlbumClassify(id);
        return ResponseBean.success(removeId);
    }

    /**
     * 根据id查询相册分类
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseBean getById(@PathVariable("id") String  id) {
        TpPhotoAlbumClassifyDTO dto = photoAlbumClassifyService.getById(id);
        return ResponseBean.success(dto);
    }


    /**
     * 分页查询相册分类
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseBean pageRecruitment(TpPhotoAlbumClassifyRequest request) {
        IPage<TpPhotoAlbumClassifyDTO> page = photoAlbumClassifyService.pagePhotoAlbumClassify(request);
        return ResponseBean.success(page);
    }

    /**
     * 查询相册分类
     * @param request
     * @return
     */
    @GetMapping("/list")
    public ResponseBean listPhotoAlbumClassify(TpPhotoAlbumClassifyRequest request) {
        List<TpPhotoAlbumClassifyDTO> list = photoAlbumClassifyService.listPhotoAlbumClassify(request);
        return ResponseBean.success(list);
    }

    /**
     * 查询相册分类 层级
     * @return
     */
    @GetMapping("/getPhotoAlbumClassify")
    public ResponseBean getPhotoAlbumClassify() {
        List<TpPhotoAlbumClassifyDTO> list = photoAlbumClassifyService.getPhotoAlbumClassify();
        return ResponseBean.success(list);
    }

    /**
     * 停用/启用 相册分类
     * @param request
     * @return
     */
    @PutMapping("/enable")
    public ResponseBean enableRecruitment(@RequestBody TpPhotoAlbumClassifyRequest request) {
        String enableId = photoAlbumClassifyService.enablePhotoAlbumClassify(request);
        return ResponseBean.success(enableId);
    }


}
