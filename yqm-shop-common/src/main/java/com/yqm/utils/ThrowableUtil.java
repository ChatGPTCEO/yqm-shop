/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.validation.ConstraintViolationException;
import com.yqm.exception.BadRequestException;

/**
 * 异常工具 2019-01-06
 * @author weiximei
 */
public class ThrowableUtil {

	/**
	 * 获取堆栈信息
	 */
	public static String getStackTrace(Throwable throwable){
		StringWriter sw = new StringWriter();
		try (PrintWriter pw = new PrintWriter(sw)) {
			throwable.printStackTrace(pw);
			return sw.toString();
		}
	}

	public static void throwForeignKeyException(Throwable e, String msg){
		Throwable t = e.getCause();
		while ((t != null) && !(t instanceof ConstraintViolationException)) {
			t = t.getCause();
		}
		if (t != null) {
			throw new BadRequestException(msg);
		}
		assert false;
		// throw new BadRequestException("删除失败：" + t.getMessage());
	}
}
