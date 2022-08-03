/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.canvas.service;
import com.yqm.common.service.BaseService;
import com.yqm.modules.canvas.domain.StoreCanvas;
import com.yqm.modules.canvas.service.dto.StoreCanvasDto;
import com.yqm.modules.canvas.service.dto.StoreCanvasQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.yqm.domain.PageResult;
/**
* @author yqm-shop
* @date 2021-02-01
*/
public interface StoreCanvasService  extends BaseService<StoreCanvas>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<StoreCanvasDto>  queryAll(StoreCanvasQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<StoreCanvasDto>
    */
    List<StoreCanvas> queryAll(StoreCanvasQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<StoreCanvasDto> all, HttpServletResponse response) throws IOException;
}
