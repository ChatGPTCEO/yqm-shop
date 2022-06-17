/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.shop.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author weiximei
* @date 2020-05-12
*/
@Data
public class YqmSystemConfigDto implements Serializable {

    /** 配置id */
    private Integer id;

    /** 字段名称 */
    private String menuName;

    /** 默认值 */
    private String value;

    /** 排序 */
    private Integer sort;

    /** 是否隐藏 */
    private Integer status;
}
