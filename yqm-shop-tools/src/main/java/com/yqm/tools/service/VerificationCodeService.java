/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.tools.service;

import com.yqm.tools.domain.VerificationCode;
import com.yqm.tools.domain.vo.EmailVo;

/**
 * @author weiximei
 * @date 2018-12-26
 */
public interface VerificationCodeService {

    /**
     * 发送邮件验证码
     * @param code 验证码
     * @return EmailVo
     */
    EmailVo sendEmail(VerificationCode code);

    /**
     * 验证
     * @param code 验证码
     */
    void validated(VerificationCode code);
}
