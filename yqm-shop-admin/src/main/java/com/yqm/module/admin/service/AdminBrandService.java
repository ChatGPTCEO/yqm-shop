package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmBrandToDTO;
import com.yqm.common.dto.YqmBrandDTO;
import com.yqm.common.entity.YqmBrand;
import com.yqm.common.request.YqmBrandRequest;
import com.yqm.common.service.IYqmBrandService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminBrandService {

    private final IYqmBrandService iYqmBrandService;

    public AdminBrandService(IYqmBrandService iYqmBrandService) {
        this.iYqmBrandService = iYqmBrandService;
    }

    public IPage<YqmBrandDTO> page(YqmBrandRequest request) {
        Page<YqmBrand> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());
        IPage pageList = iYqmBrandService.page(page, iYqmBrandService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmBrandToDTO.toYqmBrandDTOList(pageList.getRecords()));
        }
        return pageList;
    }

}
