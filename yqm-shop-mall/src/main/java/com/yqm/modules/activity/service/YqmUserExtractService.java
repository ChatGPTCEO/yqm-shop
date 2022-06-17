/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.activity.domain.YqmUserExtract;
import com.yqm.modules.activity.param.UserExtParam;
import com.yqm.modules.activity.service.dto.YqmUserExtractDto;
import com.yqm.modules.activity.service.dto.YqmUserExtractQueryCriteria;
import com.yqm.modules.user.domain.YqmUser;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-13
*/
public interface YqmUserExtractService  extends BaseService<YqmUserExtract>{

    /**
     * 开始提现
     * @param userInfo 用户
     * @param param UserExtParam
     */
    void userExtract(YqmUser userInfo, UserExtParam param);

    /**
     * 累计提现金额
     * @param uid uid
     * @return double
     */
    double extractSum(Long uid);


    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmUserExtractQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmUserExtractDto>
    */
    List<YqmUserExtract> queryAll(YqmUserExtractQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmUserExtractDto> all, HttpServletResponse response) throws IOException;


    /**
     * 操作提现
     * @param resources YqmUserExtract
     */
    void doExtract(YqmUserExtract resources);
}
