package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.entity.TpTeamClassify;
import com.yqm.common.mapper.TpTeamClassifyMapper;
import com.yqm.common.request.TpTeamClassifyRequest;
import com.yqm.common.service.ITpTeamClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
* <p>
    * 团队分类 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-11-09
*/
@Service
public class TpTeamClassifyServiceImpl extends ServiceImpl<TpTeamClassifyMapper, TpTeamClassify> implements ITpTeamClassifyService {
    @Override
    public QueryWrapper<TpTeamClassify> queryWrapper(TpTeamClassifyRequest request) {
        QueryWrapper<TpTeamClassify> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("updated_time");
        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        return queryWrapper;
    }
}
