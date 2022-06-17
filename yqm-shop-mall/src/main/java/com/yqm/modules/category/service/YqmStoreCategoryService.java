/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.category.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.category.domain.YqmStoreCategory;
import com.yqm.modules.category.service.dto.YqmStoreCategoryDto;
import com.yqm.modules.category.service.dto.YqmStoreCategoryQueryCriteria;
import com.yqm.utils.CateDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmStoreCategoryService  extends BaseService<YqmStoreCategory>{

    List<CateDTO> getList();

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmStoreCategoryQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmStoreCategoryDto>
    */
    List<YqmStoreCategoryDto> queryAll(YqmStoreCategoryQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmStoreCategoryDto> all, HttpServletResponse response) throws IOException;

    /**
     * 构建树形
     * @param categoryDTOS 分类列表
     * @return map
     */
    Map<String,Object> buildTree(List<YqmStoreCategoryDto> categoryDTOS);

    /**
     * 检测分类是否操过二级
     * @param pid 父级id
     * @return boolean
     */
    boolean checkCategory(int pid);

    /**
     * 检测商品分类必选选择二级
     * @param id 分类id
     * @return boolean
     */
    boolean checkProductCategory(int id);
}
