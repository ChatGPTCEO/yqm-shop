/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.activity.service.dto;

import com.yqm.annotation.Query;
import lombok.Data;

/**
* @author weiximei
* @date 2020-05-13
*/
@Data
public class YqmStoreSeckillQueryCriteria{


    // 模糊
    @Query(type = Query.Type.INNER_LIKE)
    private String title;
}
