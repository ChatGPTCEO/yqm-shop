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

package com.yqm.module.admin.service;

import com.yqm.common.define.SysConfigDefine;
import com.yqm.common.entity.SysConfig;
import com.yqm.common.request.SysConfigRequest;
import com.yqm.common.service.ISysConfigService;
import com.yqm.common.upload.UploadAbstractImg;
import com.yqm.common.upload.UploadImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 上传业务
 *
 * @Author: weiximei
 * @Date: 2021/10/23 22:38
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
public class UploadService {

    @Autowired
    private UploadImg uploadImg;

    @Autowired
    private ISysConfigService iSysConfigService;

    /**
     * 上传图片
     * @param bytes
     * @return
     */
    public String uploadImg(byte []bytes) {

        SysConfigRequest request = new SysConfigRequest();
        request.setConfigName(SysConfigDefine.UPLOAD);
        SysConfig sysConfig = iSysConfigService.getOne(iSysConfigService.queryWrapper(request));

        UploadAbstractImg baseUpload = uploadImg.getUploadImg(sysConfig.getConfigValue());
        return baseUpload.run(bytes);
    }

}
