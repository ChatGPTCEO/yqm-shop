/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.shop.domain.YqmSystemGroupData;
import com.yqm.modules.shop.service.dto.YqmSystemGroupDataDto;
import com.yqm.modules.shop.service.dto.YqmSystemGroupDataQueryCriteria;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmSystemGroupDataService  extends BaseService<YqmSystemGroupData>{

    /**
     * 获取配置数据
     * @param name 配置名称
     * @return List
     */
    List<JSONObject> getDatas(String name);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmSystemGroupDataQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmSystemGroupDataDto>
    */
    List<YqmSystemGroupData> queryAll(YqmSystemGroupDataQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmSystemGroupDataDto> all, HttpServletResponse response) throws IOException;
}
