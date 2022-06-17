/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.mp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiximei
 * 微信公众号模板枚举
 */
@Getter
@AllArgsConstructor
public enum WechatTempateEnum {
    PAY_SUCCESS("pay_success","支付成功通知"),
    DELIVERY_SUCCESS("delivery_success","发货成功通知"),
    REFUND_SUCCESS("refund_success","退款成功通知"),
    RECHARGE_SUCCESS("recharge_success","充值成功通知");

    private String value; //模板编号
    private String desc; //模板id
}
