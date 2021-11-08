package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpHonorClassify;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.mapper.TpHonorClassifyMapper;
import com.yqm.common.request.TpHonorClassifyRequest;
import com.yqm.common.service.ITpHonorClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

/**
* <p>
    * 荣誉分类 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpHonorClassifyServiceImpl extends ServiceImpl<TpHonorClassifyMapper, TpHonorClassify> implements ITpHonorClassifyService {

    @Override
    public QueryWrapper<TpHonorClassify> queryWrapper(TpHonorClassifyRequest request) {
        QueryWrapper<TpHonorClassify> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("updated_time");
        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        return queryWrapper;
    }
}
