/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.mp.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.mp.service.dto.YqmArticleDto;
import com.yqm.modules.mp.service.dto.YqmArticleQueryCriteria;
import com.yqm.modules.mp.domain.YqmArticle;
import com.yqm.modules.mp.vo.YqmArticleQueryVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmArticleService  extends BaseService<YqmArticle>{

    /**
     * 获取文章列表
     * @param page 页码
     * @param limit 条数
     * @return List
     */
    List<YqmArticleQueryVo> getList(int page, int limit);

    /**
     * 获取文章详情
     * @param id id
     * @return YqmArticleQueryVo
     */
    YqmArticleQueryVo getDetail(int id);

    void incVisitNum(int id);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmArticleQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmArticleDto>
    */
    List<YqmArticle> queryAll(YqmArticleQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmArticleDto> all, HttpServletResponse response) throws IOException;


}
