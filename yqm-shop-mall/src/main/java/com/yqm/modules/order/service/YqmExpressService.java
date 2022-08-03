/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.order.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.order.domain.YqmExpress;
import com.yqm.modules.order.service.dto.YqmExpressDto;
import com.yqm.modules.order.service.dto.YqmExpressQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmExpressService  extends BaseService<YqmExpress>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmExpressQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmExpressDto>
    */
    List<YqmExpress> queryAll(YqmExpressQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmExpressDto> all, HttpServletResponse response) throws IOException;
}
