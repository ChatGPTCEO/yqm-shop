/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.order.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.order.domain.YqmStoreOrderCartInfo;
import com.yqm.modules.order.service.dto.YqmStoreOrderCartInfoDto;
import com.yqm.modules.order.service.dto.YqmStoreOrderCartInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmStoreOrderCartInfoService  extends BaseService<YqmStoreOrderCartInfo>{

    /**
     * 添加购物车商品信息
     * @param oid 订单id
     * @param orderId 订单号
     * @param cartInfo 购物车信息
     */
    void saveCartInfo(Long oid, String orderId,List<YqmStoreCartQueryVo> cartInfo);

    YqmStoreOrderCartInfo findByUni(String unique);


    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmStoreOrderCartInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmStoreOrderCartInfoDto>
    */
    List<YqmStoreOrderCartInfo> queryAll(YqmStoreOrderCartInfoQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmStoreOrderCartInfoDto> all, HttpServletResponse response) throws IOException;
}
