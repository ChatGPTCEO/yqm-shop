package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmBrandToDTO;
import com.yqm.common.dto.YqmBrandDTO;
import com.yqm.common.entity.YqmBrand;
import com.yqm.common.request.YqmBrandRequest;
import com.yqm.common.service.IYqmBrandService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 管理端-品牌
 *
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

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
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

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmBrandDTO save(YqmBrandRequest request) {
        User user = UserInfoService.getUser();
        YqmBrand entity = YqmBrandToDTO.toYqmBrand(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setCreatedTime(LocalDateTime.now());
        return YqmBrandToDTO.toYqmBrandDTO(entity);
    }

}
