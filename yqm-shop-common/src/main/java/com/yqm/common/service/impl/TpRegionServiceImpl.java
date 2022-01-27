package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.conversion.TpRegionToDTO;
import com.yqm.common.dto.TpRegionDTO;
import com.yqm.common.entity.TpRegion;
import com.yqm.common.mapper.TpRegionMapper;
import com.yqm.common.request.TpRegionRequest;
import com.yqm.common.service.ITpRegionService;
import org.apache.commons.collections4.CollectionUtils;
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
        if (null != request.getPid()) {
            queryWrapper.eq("pid", request.getPid());
        }

        return queryWrapper;
    }

    @Override
    public List<TpRegion> getProvinces(String pid) {
        TpRegionRequest request = new TpRegionRequest();
        request.setPid(pid);
        return this.list(this.queryWrapper(request));
    }

    @Override
    public List<TpRegionDTO> getProvinces() {
        List<TpRegion> regionList = this.getProvinces("100000");
        List<TpRegionDTO> dtoList = TpRegionToDTO.toTpCompanyDTOList(regionList);
        for (TpRegionDTO dto : dtoList) {
            List<TpRegionDTO> cityDtoList = TpRegionToDTO.toTpCompanyDTOList(this.getProvinces(dto.getId()));
            dto.setChildren(cityDtoList);
            if (CollectionUtils.isEmpty(cityDtoList)) {
                continue;
            }
            for (TpRegionDTO cityDto : cityDtoList) {
                List<TpRegionDTO> areaDtoList = TpRegionToDTO.toTpCompanyDTOList(this.getProvinces(cityDto.getId()));
                cityDto.setChildren(areaDtoList);
            }

        }
        return dtoList;
    }
}
