package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpCompany;
import com.yqm.common.request.TpCompanyRequest;

/**
* <p>
    * 公司 服务类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
public interface ITpCompanyService extends IService<TpCompany> {

    /**
     * 构建查询条件
     * @param request
     * @return
     */
    QueryWrapper<TpCompany> queryWrapper(TpCompanyRequest request);

    /**
     * 根据 用户id
     * @param userId
     * @return
     */
    TpCompany getByUserId(String userId);
}
