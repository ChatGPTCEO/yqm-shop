/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiximei
 * 通用枚举
 */
@Getter
@AllArgsConstructor
public enum CommonEnum {

	DEL_STATUS_0(0,"未删除"),
	DEL_STATUS_1(1,"已删除"),

	SHOW_STATUS_0(0,"未显示"),
	SHOW_STATUS_1(1, "显示"),

	IS_INTEGRAL_0(0, "不是积分"),
	IS_INTEGRAL_1(1, "是积分");


	private Integer value;
	private String desc;


}
