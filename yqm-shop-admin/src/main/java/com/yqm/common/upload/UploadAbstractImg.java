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

package com.yqm.common.upload;

import com.yqm.common.exception.YqmException;

/**
 * 图片文件上传
 *
 * @Author: weiximei
 * @Date: 2021/10/23 18:33
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public abstract class UploadAbstractImg {

    private String value;

    public abstract String getValue();

    /**
     * 上传前
     */
    protected abstract void beforeUpload() throws YqmException;

    /**
     * 上传后
     */
    protected abstract void afterUpload() throws YqmException;

    public String run(byte[] bytes) throws YqmException {
        beforeUpload();
        String url = (String) upload(bytes);
        afterUpload();
        return url;
    }

    /**
     * 执行
     *
     * @param bytes
     * @return
     */
    protected abstract Object upload(byte[] bytes);
}
