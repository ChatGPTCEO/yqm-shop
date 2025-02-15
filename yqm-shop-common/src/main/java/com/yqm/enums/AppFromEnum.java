/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author weiximei
 * 应用来源相关枚举
 */
@Getter
@AllArgsConstructor
public enum AppFromEnum {

	WEIXIN_H5("weixinh5","weixinh5"),
	H5("h5","H5"),
	WECHAT("wechat","公众号"),
	APP("app","APP"),
	PC("pc","PC"),
	ROUNTINE("routine","小程序"),
	UNIAPPH5("uniappH5","uniappH5"),
	TARO_H5("taroH5","taroH5"),
	TARO_MINI("taroMINI","taro小程序");


	private String value;
	private String desc;


}
