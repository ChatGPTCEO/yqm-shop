/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.api;

/**
 * 认证异常
 * @author weiximei
 * @date 2020-04-30
 */
public class UnAuthenticatedException extends YqmShopException {
    public UnAuthenticatedException(String message) {
        super(message);
    }

    public UnAuthenticatedException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public UnAuthenticatedException(ApiCode apiCode) {
        super(apiCode);
    }
}
