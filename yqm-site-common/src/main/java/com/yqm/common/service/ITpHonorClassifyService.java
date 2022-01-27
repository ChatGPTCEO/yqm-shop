package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpHonorClassify;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.request.TpHonorClassifyRequest;
import com.yqm.common.request.TpLinkClassifyRequest;

/**
* <p>
    * 荣誉分类 服务类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
public interface ITpHonorClassifyService extends IService<TpHonorClassify> {

    QueryWrapper<TpHonorClassify> queryWrapper(TpHonorClassifyRequest request);

}
