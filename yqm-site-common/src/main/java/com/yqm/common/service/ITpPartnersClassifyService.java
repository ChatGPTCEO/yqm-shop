package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpPartnersClassify;
import com.yqm.common.request.TpPartnersClassifyRequest;

/**
* <p>
    * 合作伙伴分类 服务类
    * </p>
*
* @author weiximei
* @since 2021-11-09
*/
public interface ITpPartnersClassifyService extends IService<TpPartnersClassify> {

    QueryWrapper<TpPartnersClassify> queryWrapper(TpPartnersClassifyRequest request);

}
