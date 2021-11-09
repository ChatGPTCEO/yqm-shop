package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpPartners;
import com.yqm.common.entity.TpPartners;
import com.yqm.common.mapper.TpPartnersMapper;
import com.yqm.common.mapper.TpPartnersMapper;
import com.yqm.common.request.TpPartnersRequest;
import com.yqm.common.service.ITpPartnersService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
* <p>
    * 合作伙伴 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpPartnersServiceImpl extends ServiceImpl<TpPartnersMapper, TpPartners> implements ITpPartnersService {

    private TpPartnersMapper tpPartnersMapper;

    public TpPartnersServiceImpl(TpPartnersMapper tpPartnersMapper) {
        this.tpPartnersMapper = tpPartnersMapper;
    }

    @Override
    public QueryWrapper<TpPartners> queryWrapper(TpPartnersRequest request) {

        QueryWrapper<TpPartners> queryWrapper = new QueryWrapper();
        if (request.isOrderSort()) {
            queryWrapper.orderByAsc("sort");
            queryWrapper.orderByDesc("updated_time");

        } else {
            queryWrapper.orderByDesc(Arrays.asList("sort", "updated_time"));
        }

        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        return queryWrapper;
    }

    @Override
    public int updateAllSortGal(Integer currentSort, String userId) {
        return tpPartnersMapper.updateAllSortGal(currentSort, userId);
    }

    @Override
    public int top(String id, String userId) {
        return tpPartnersMapper.top(id, userId);
    }

    @Override
    public int getMaxSort(String userId) {
        return tpPartnersMapper.getMaxSort(userId);
    }

}
