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

package com.yqm.security.browase.controller;

import com.yqm.security.core.img.ImageCodeGenerator;
import com.yqm.security.core.properties.SecurityProperties;
import com.yqm.security.core.validate.code.ImageCode;
import com.yqm.security.core.validate.code.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author: weiximei
 * @Date: 2021/9/12 13:11
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Controller
public class ValidateCodeController {

    /**
     * image 在Session中标识符
     */
    private static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @Autowired
    private SecurityProperties securityProperties;

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        ImageCode imageCode = createImage(request);
        session.setAttribute(SESSION_KEY, imageCode.getCode());
        response.setContentType("image/jpeg");
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * 生成图片的逻辑
     * @param request
     * @return
     */
    private ImageCode createImage(HttpServletRequest request) {
        ValidateCodeGenerator codeGenerator = new ImageCodeGenerator(securityProperties);
        return (ImageCode) codeGenerator.generate(new ServletWebRequest(request));
    }

}
