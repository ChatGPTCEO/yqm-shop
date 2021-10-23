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

package com.yqm.config;

import com.yqm.common.exception.YqmException;
import com.yqm.common.response.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @Author: weiximei
 * @Date: 2021/10/23 22:49
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionHandleAdice {

    @ExceptionHandler(Exception.class)
    public ResponseBean handleException(Exception e){
        log.error(e.getMessage(),e);
        return ResponseBean.error("系统异常,请稍后重试!");
    }


    @ExceptionHandler(YqmException.class)
    public ResponseBean handleException(YqmException e){
        log.error(e.getMessage(),e);
        return ResponseBean.error(e.getCode(), e.getMessage());
    }

}
