package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.request.TpLinkClassifyRequest;

/**
* <p>
    * 链接分类 服务类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
public interface ITpLinkClassifyService extends IService<TpLinkClassify> {

    QueryWrapper<TpLinkClassify> queryWrapper(TpLinkClassifyRequest request);
}
