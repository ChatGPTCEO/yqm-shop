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
import com.yqm.modules.product.vo.YqmSystemStoreQueryVo;
import com.yqm.modules.shop.domain.YqmSystemStore;
import com.yqm.modules.shop.service.dto.YqmSystemStoreDto;
import com.yqm.modules.shop.service.dto.YqmSystemStoreQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmSystemStoreService  extends BaseService<YqmSystemStore>{

    YqmSystemStoreQueryVo getYqmSystemStoreById(int id);

    /**
     * 获取门店列表
     * @param latitude 纬度
     * @param longitude 经度
     * @param page page
     * @param limit limit
     * @return List
     */
    List<YqmSystemStoreQueryVo> getStoreList(String latitude, String longitude, int page, int limit);

    /**
     * 获取最新单个门店
     * @param latitude 纬度
     * @param longitude 经度
     * @return YqmSystemStoreQueryVo
     */
    YqmSystemStoreQueryVo getStoreInfo(String latitude,String longitude);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmSystemStoreQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmSystemStoreDto>
    */
    List<YqmSystemStore> queryAll(YqmSystemStoreQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmSystemStoreDto> all, HttpServletResponse response) throws IOException;
}
