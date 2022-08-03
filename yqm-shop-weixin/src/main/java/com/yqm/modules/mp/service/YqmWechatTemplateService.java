/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.mp.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.mp.service.dto.YqmWechatTemplateQueryCriteria;
import com.yqm.modules.mp.domain.YqmWechatTemplate;
import com.yqm.modules.mp.service.dto.YqmWechatTemplateDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmWechatTemplateService  extends BaseService<YqmWechatTemplate>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmWechatTemplateQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmWechatTemplateDto>
    */
    List<YqmWechatTemplate> queryAll(YqmWechatTemplateQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmWechatTemplateDto> all, HttpServletResponse response) throws IOException;

    YqmWechatTemplate findByTempkey(String recharge_success_key);
}
