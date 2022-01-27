package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.entity.TpPartnersClassify;
import com.yqm.common.mapper.TpPartnersClassifyMapper;
import com.yqm.common.request.TpPartnersClassifyRequest;
import com.yqm.common.service.ITpPartnersClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* <p>
    * 合作伙伴分类 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-11-09
*/
@Service
public class TpPartnersClassifyServiceImpl extends ServiceImpl<TpPartnersClassifyMapper, TpPartnersClassify> implements ITpPartnersClassifyService {
    @Override
    public QueryWrapper<TpPartnersClassify> queryWrapper(TpPartnersClassifyRequest request) {
        QueryWrapper<TpPartnersClassify> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("updated_time");
        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        if (StringUtils.isNotBlank(request.getUserId())) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        return queryWrapper;
    }
}
