/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.shop.service;
import com.yqm.common.service.BaseService;
import com.yqm.modules.shop.domain.YqmAppVersion;
import com.yqm.modules.shop.service.dto.YqmAppVersionDto;
import com.yqm.modules.shop.service.dto.YqmAppVersionQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.yqm.domain.PageResult;
/**
* @author lioncity
* @date 2020-12-09
*/
public interface YqmAppVersionService  extends BaseService<YqmAppVersion>{

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<YqmAppVersionDto>  queryAll(YqmAppVersionQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmAppVersionDto>
    */
    List<YqmAppVersion> queryAll(YqmAppVersionQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmAppVersionDto> all, HttpServletResponse response) throws IOException;
}
