/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.logging.service.dto;

import com.yqm.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 日志查询类
 * @author weiximei
 * @date 2019-6-4 09:23:07
 */
@Data
public class LogQueryCriteria {

    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;

    @Query
    private String logType;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;


    @Query
    private Integer type;
}
