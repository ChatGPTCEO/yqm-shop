/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.order.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.order.domain.YqmStoreOrderStatus;
import com.yqm.modules.order.service.dto.YqmStoreOrderStatusDto;
import com.yqm.modules.order.service.dto.YqmStoreOrderStatusQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmStoreOrderStatusService  extends BaseService<YqmStoreOrderStatus>{

    /**
     * 添加订单操作记录
     * @param oid 订单id
     * @param changetype 操作状态
     * @param changeMessage 操作内容
     */
    void create(Long oid,String changetype,String changeMessage);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmStoreOrderStatusQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmStoreOrderStatusDto>
    */
    List<YqmStoreOrderStatus> queryAll(YqmStoreOrderStatusQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmStoreOrderStatusDto> all, HttpServletResponse response) throws IOException;
}
