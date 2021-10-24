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

import com.yqm.common.response.ResponseBean;
import com.yqm.common.upload.UploadImg;
import com.yqm.module.admin.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 上传
 *
 * @Author: weiximei
 * @Date: 2021/10/23 22:37
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 图片上传
     * @param file
     * @return
     */
    @PostMapping("/img")
    public ResponseBean uploadImg(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseBean.success(uploadService.uploadImg(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseBean.error("上传失败");
    }

}
