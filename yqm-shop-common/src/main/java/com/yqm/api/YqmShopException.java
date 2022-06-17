/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.api;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 * @author weiximei
 * @date 2020-04-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class YqmShopException extends RuntimeException{

    private static final long serialVersionUID = -2470461654663264392L;

    private Integer errorCode;
    private String message;

    public YqmShopException() {
        super();
    }

    public YqmShopException(String message) {
        super(message);
        this.message = message;
    }

    public YqmShopException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public YqmShopException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.errorCode = apiCode.getCode();
        this.message = apiCode.getMessage();
    }

    public YqmShopException(String message, Throwable cause) {
        super(message, cause);
    }

    public YqmShopException(Throwable cause) {
        super(cause);
    }

}
