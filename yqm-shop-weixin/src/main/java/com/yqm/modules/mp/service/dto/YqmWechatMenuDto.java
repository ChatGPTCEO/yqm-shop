/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.mp.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
* @author weiximei
* @date 2020-05-12
*/
@Data
public class YqmWechatMenuDto implements Serializable {

    private String key;

    /** 缓存数据 */
    private String result;

    /** 缓存时间 */
    private Integer addTime;
}
