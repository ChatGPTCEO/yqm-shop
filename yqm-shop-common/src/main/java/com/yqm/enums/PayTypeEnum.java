/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author weiximei
 * 支付相关枚举
 */
@Getter
@AllArgsConstructor
public enum PayTypeEnum {

	ALI("alipay","支付宝支付"),
	WEIXIN("weixin","微信支付"),
	YUE("yue","余额支付"),
	INTEGRAL("integral","积分兑换");


	private String value;
	private String desc;

	public static PayTypeEnum toType(String value) {
		return Stream.of(PayTypeEnum.values())
				.filter(p -> p.value.equals(value))
				.findAny()
				.orElse(null);
	}


}
