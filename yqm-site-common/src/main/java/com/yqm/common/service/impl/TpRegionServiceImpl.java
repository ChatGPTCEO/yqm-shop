package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpRegion;
import com.yqm.common.mapper.TpRegionMapper;
import com.yqm.common.request.TpRegionRequest;
import com.yqm.common.service.ITpRegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* <p>
    * 地区表 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-24
*/
@Service
public class TpRegionServiceImpl extends ServiceImpl<TpRegionMapper, TpRegion> implements ITpRegionService {

    @Override
    public QueryWrapper<TpRegion> queryWrapper(TpRegionRequest request) {
        QueryWrapper<TpRegion> queryWrapper = new QueryWrapper<>();
        if (null != request.getPCode()) {
            queryWrapper.eq("p_code", request.getPCode());
        }
        return queryWrapper;
    }

    @Override
    public List<TpRegion> getProvinces(Integer pCode) {
        TpRegionRequest request = new TpRegionRequest();
        request.setPCode(pCode);
        return this.list(this.queryWrapper(request));
    }
}
