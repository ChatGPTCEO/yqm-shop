package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.entity.TpTeamClassify;
import com.yqm.common.request.TpLinkClassifyRequest;
import com.yqm.common.request.TpTeamClassifyRequest;

/**
* <p>
    * 团队分类 服务类
    * </p>
*
* @author weiximei
* @since 2021-11-09
*/
public interface ITpTeamClassifyService extends IService<TpTeamClassify> {


    QueryWrapper<TpTeamClassify> queryWrapper(TpTeamClassifyRequest request);

}
