/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.user.service.dto;

import com.yqm.annotation.Query;
import lombok.Data;

/**
* @author weiximei
* @date 2020-05-12
*/
@Data
public class YqmUserBillQueryCriteria{
    @Query(type = Query.Type.EQUAL)
    private String nickname = "";
    @Query(type = Query.Type.EQUAL)
    private String category = "";
    @Query(type = Query.Type.EQUAL)
    private String type  = "";
    @Query(type = Query.Type.EQUAL)
    private Integer pm;
    @Query(type = Query.Type.EQUAL)
    private String title = "";
    private String startTime;

    private String endTime;
}
