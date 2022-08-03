/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.activity.domain.YqmStoreCombination;
import com.yqm.modules.activity.service.dto.YqmStoreCombinationDto;
import com.yqm.modules.activity.service.dto.YqmStoreCombinationQueryCriteria;
import com.yqm.modules.activity.vo.CombinationQueryVo;
import com.yqm.modules.activity.vo.StoreCombinationVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-13
*/
public interface YqmStoreCombinationService  extends BaseService<YqmStoreCombination>{



    /**
     * 拼团列表
     * @param page page
     * @param limit limit
     * @return list
     */
    CombinationQueryVo getList(int page, int limit);

    /**
     * 获取拼团详情
     * @param id 拼团产品id
     * @param uid uid
     * @return StoreCombinationVo
     */
    StoreCombinationVo getDetail(Long id, Long uid);

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmStoreCombinationQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmStoreCombinationDto>
    */
    List<YqmStoreCombination> queryAll(YqmStoreCombinationQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmStoreCombinationDto> all, HttpServletResponse response) throws IOException;

    /**
     * 修改状态
     * @param id 拼团产品id
     * @param status ShopCommonEnum
     */
    void onSale(Long id, Integer status);

    boolean saveCombination(YqmStoreCombinationDto resources);
}
