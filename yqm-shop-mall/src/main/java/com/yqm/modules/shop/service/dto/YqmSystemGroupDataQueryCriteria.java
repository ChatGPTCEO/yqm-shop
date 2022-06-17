/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.shop.service.dto;

import com.yqm.annotation.Query;
import lombok.Data;

/**
* @author weiximei
* @date 2020-05-12
*/
@Data
public class YqmSystemGroupDataQueryCriteria{

    // 精确
    @Query
    private String groupName;

    @Query
    private Integer status;
}
