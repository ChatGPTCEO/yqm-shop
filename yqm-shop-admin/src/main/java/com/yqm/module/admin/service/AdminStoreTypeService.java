package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmStoreTypeToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreTypeDTO;
import com.yqm.common.entity.YqmStoreType;
import com.yqm.common.request.YqmStoreTypeRequest;
import com.yqm.common.service.IYqmStoreTypeService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-商品类型
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminStoreTypeService {


    private final IYqmStoreTypeService iYqmStoreTypeService;

    public AdminStoreTypeService(IYqmStoreTypeService iYqmStoreTypeService) {
        this.iYqmStoreTypeService = iYqmStoreTypeService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmStoreTypeDTO> page(YqmStoreTypeRequest request) {
        Page<YqmStoreType> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmStoreTypeService.page(page, iYqmStoreTypeService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmStoreTypeToDTO.toYqmStoreTypeDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmStoreTypeDTO> list(YqmStoreTypeRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreType> list = iYqmStoreTypeService.list(iYqmStoreTypeService.getQuery(request));
        return YqmStoreTypeToDTO.toYqmStoreTypeDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmStoreTypeDTO getById(String id) {
        YqmStoreType entity = iYqmStoreTypeService.getById(id);
        return YqmStoreTypeToDTO.toYqmStoreTypeDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmStoreTypeDTO save(YqmStoreTypeRequest request) {
        User user = UserInfoService.getUser();
        YqmStoreType entity = YqmStoreTypeToDTO.toYqmStoreType(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmStoreTypeService.saveOrUpdate(entity);
        return YqmStoreTypeToDTO.toYqmStoreTypeDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmStoreType brand = iYqmStoreTypeService.getById(id);
        if (Objects.isNull(brand)) {
            return id;
        }
        brand.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmStoreTypeToDTO.toYqmStoreTypeRequest(brand));
        return id;
    }


}
