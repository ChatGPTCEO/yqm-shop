package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.entity.TpRecruitment;
import com.yqm.common.mapper.TpLinkClassifyMapper;
import com.yqm.common.request.TpLinkClassifyRequest;
import com.yqm.common.request.TpRecruitmentRequest;
import com.yqm.common.service.ITpLinkClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 链接分类 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpLinkClassifyServiceImpl extends ServiceImpl<TpLinkClassifyMapper, TpLinkClassify> implements ITpLinkClassifyService {

    @Override
    public QueryWrapper<TpLinkClassify> queryWrapper(TpLinkClassifyRequest request) {
        QueryWrapper<TpLinkClassify> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("updated_time");
        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        return queryWrapper;
    }
}
