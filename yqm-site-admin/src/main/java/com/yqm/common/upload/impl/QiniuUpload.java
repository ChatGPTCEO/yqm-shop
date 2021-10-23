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

package com.yqm.common.upload.impl;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.yqm.common.define.SysConfigDefine;
import com.yqm.common.entity.SysConfig;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.SysConfigRequest;
import com.yqm.common.service.ISysConfigService;
import com.yqm.common.upload.IUpload;
import com.yqm.common.upload.UploadConfig;
import com.yqm.common.upload.UploadAbstractImg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 七牛云上传
 *
 * @Author: weiximei
 * @Date: 2021/10/23 18:22
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Component
@Slf4j
public class QiniuUpload extends UploadAbstractImg implements IUpload {


    @Autowired
    private ISysConfigService iSysConfigService;

    @Override
    public Object upload(byte[] bytes) {

        SysConfigRequest request = new SysConfigRequest();
        request.setConfigName(SysConfigDefine.QI_NIU_UPLOAD);
        SysConfig sysConfig = iSysConfigService.getOne(iSysConfigService.queryWrapper(request));
        UploadConfig config = JSONObject.parseObject(sysConfig.getConfigValue(), UploadConfig.class);

        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        //  String localFilePath = "/home/qiniu/test.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        String upToken = auth.uploadToken(config.getBucket());
        try {
            Response response = uploadManager.put(bytes, null, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSONObject.parseObject(response.bodyString(),DefaultPutRet.class);
            log.info("上传文件响应：{}, {}", putRet.key, putRet.hash);
            return config.getHost() + "/" +putRet.key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            log.error("上传文件Error: {}", r.toString());
            try {
                log.error("上传文件Error: {}", r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }

        return null;
    }

    @Override
    protected void beforeUpload() throws YqmException {
        log.info("七牛云上传前...");
    }

    @Override
    protected void afterUpload() throws YqmException {
        log.info("七牛云上传后...");
    }

    @Override
    public String getValue() {
        return "qiniu-upload";
    }
}
