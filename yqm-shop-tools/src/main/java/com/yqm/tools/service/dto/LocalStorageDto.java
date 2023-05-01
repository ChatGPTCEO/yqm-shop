/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.tools.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author weiximei
* @date 2020-05-13
*/
@Data
public class LocalStorageDto implements Serializable {
    private Long id;

    private String realName;

    private String name;

    private String suffix;

    private String path;

    private String type;

    private String size;

    private String operate;

    private Timestamp createTime;
}
