/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.dict.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.dict.domain.DictDetail;
import com.yqm.modules.dict.service.dto.DictDetailDto;
import com.yqm.modules.dict.service.dto.DictDetailQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-14
*/
public interface DictDetailService  extends BaseService<DictDetail>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(DictDetailQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<DictDetailDto>
    */
    List<DictDetail> queryAll(DictDetailQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<DictDetailDto> all, HttpServletResponse response) throws IOException;


    /**
     * 按名称查询字典值返回label
     *
     * @param dictName dict类型名称
     * @return {@link Map}<{@link String}, {@link String}>
     */
    Map<String, String> queryDetailsByName(String dictName);

    /**
     * 按名称查询字典值返回key
     *
     * @param dictName dict类型名称
     * @return {@link Map}<{@link String}, {@link String}>
     */
    Map<String, String> queryDetailsByKey(String dictName);

}
