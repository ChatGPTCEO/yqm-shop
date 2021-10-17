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

package com.yqm.common.response;

import lombok.Builder;

import java.io.Serializable;

/**
 *
 * 公共响应
 *
 * @Author: weiximei
 * @Date: 2021/10/17 12:05
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class ResponseBean<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;



    private ResponseBean(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private ResponseBean(T data) {
        this(1, "成功", data);
    }

    private ResponseBean(String message) {
        this(0, message, null);
    }

    public static <T> ResponseBean<T> success(T data) {
        return new ResponseBean(data);
    }

    public static ResponseBean error(String message) {
        return new ResponseBean(message);
    }

    public static ResponseBean error(Integer code, String message) {
        return new ResponseBean(code, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
