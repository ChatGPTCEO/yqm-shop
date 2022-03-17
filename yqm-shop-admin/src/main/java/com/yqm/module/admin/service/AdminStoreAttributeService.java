package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmStoreAttributeToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreAttributeDTO;
import com.yqm.common.entity.YqmStoreAttribute;
import com.yqm.common.entity.YqmStoreType;
import com.yqm.common.event.attribute.AttributeAddEvent;
import com.yqm.common.event.attribute.AttributeDeleteEvent;
import com.yqm.common.event.attribute.AttributeUpdateEvent;
import com.yqm.common.request.YqmStoreAttributeRequest;
import com.yqm.common.request.YqmStoreTypeRequest;
import com.yqm.common.service.IYqmStoreAttributeService;
import com.yqm.common.service.IYqmStoreTypeService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 管理端-商品属性
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminStoreAttributeService {

    private final ApplicationContext applicationContext;
    private final IYqmStoreAttributeService iYqmStoreAttributeService;
    private final IYqmStoreTypeService iYqmStoreTypeService;

    public AdminStoreAttributeService(ApplicationContext applicationContext, IYqmStoreAttributeService iYqmStoreAttributeService,
          IYqmStoreTypeService iYqmStoreTypeService) {
        this.applicationContext = applicationContext;
        this.iYqmStoreAttributeService = iYqmStoreAttributeService;
        this.iYqmStoreTypeService = iYqmStoreTypeService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmStoreAttributeDTO> page(YqmStoreAttributeRequest request) {
        Page<YqmStoreAttribute> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmStoreAttributeService.page(page, iYqmStoreAttributeService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmStoreAttributeDTO> dtoList = YqmStoreAttributeToDTO.toYqmStoreAttributeDTOList(pageList.getRecords());
            this.getOther(dtoList);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    private List<YqmStoreAttributeDTO> getOther(List<YqmStoreAttributeDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        List<String> typeIdList = dtoList.stream().map(e -> e.getStoreTypeId()).collect(Collectors.toList());
        YqmStoreTypeRequest typeRequest = new YqmStoreTypeRequest();
        typeRequest.setInIdList(typeIdList);
        List<YqmStoreType> storeTypeList = iYqmStoreTypeService.list(iYqmStoreTypeService.getQuery(typeRequest));
        Map<String, YqmStoreType> objectMap = storeTypeList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        dtoList.forEach(e -> {
            YqmStoreType entity = objectMap.get(e.getStoreTypeId());
            if (Objects.nonNull(entity)) {
                e.setStoreTypeName(entity.getTypeName());
            }
        });

        return dtoList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmStoreAttributeDTO> list(YqmStoreAttributeRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreAttribute> list = iYqmStoreAttributeService.list(iYqmStoreAttributeService.getQuery(request));
        return YqmStoreAttributeToDTO.toYqmStoreAttributeDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmStoreAttributeDTO getById(String id) {
        YqmStoreAttribute entity = iYqmStoreAttributeService.getById(id);
        return YqmStoreAttributeToDTO.toYqmStoreAttributeDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmStoreAttributeDTO save(YqmStoreAttributeRequest request) {
        User user = UserInfoService.getUser();
        YqmStoreAttribute entity = YqmStoreAttributeToDTO.toYqmStoreAttribute(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmStoreAttributeService.saveOrUpdate(entity);

        YqmStoreAttributeDTO dto = YqmStoreAttributeToDTO.toYqmStoreAttributeDTO(entity);
        if (StringUtils.isEmpty(request.getId())) {
            applicationContext.publishEvent(new AttributeAddEvent(dto));
        } else {
            applicationContext.publishEvent(new AttributeUpdateEvent(dto));
        }
        return dto;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmStoreAttribute entity = iYqmStoreAttributeService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmStoreAttributeToDTO.toYqmStoreAttributeRequest(entity));
        YqmStoreAttributeDTO dto = YqmStoreAttributeToDTO.toYqmStoreAttributeDTO(entity);
        applicationContext.publishEvent(new AttributeDeleteEvent(dto));
        return id;
    }


}
