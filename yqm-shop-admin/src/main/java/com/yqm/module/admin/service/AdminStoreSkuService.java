package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmStoreSkuToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreSkuDTO;
import com.yqm.common.entity.YqmStoreSku;
import com.yqm.common.event.sku.SkuAddEvent;
import com.yqm.common.event.sku.SkuDeleteEvent;
import com.yqm.common.event.sku.SkuUpdateEvent;
import com.yqm.common.request.YqmStoreSkuRequest;
import com.yqm.common.service.IYqmStoreSkuService;
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
import java.util.Objects;

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
public class AdminStoreSkuService {

    private final ApplicationContext applicationContext;
    private final IYqmStoreSkuService iYqmStoreSkuService;
    private final IYqmStoreTypeService iYqmStoreTypeService;

    public AdminStoreSkuService(ApplicationContext applicationContext, IYqmStoreSkuService iYqmStoreSkuService,
                                IYqmStoreTypeService iYqmStoreTypeService) {
        this.applicationContext = applicationContext;
        this.iYqmStoreSkuService = iYqmStoreSkuService;
        this.iYqmStoreTypeService = iYqmStoreTypeService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmStoreSkuDTO> page(YqmStoreSkuRequest request) {
        Page<YqmStoreSku> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmStoreSkuService.page(page, iYqmStoreSkuService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmStoreSkuDTO> dtoList = YqmStoreSkuToDTO.toYqmStoreSkuDTOList(pageList.getRecords());
            this.getOther(dtoList);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    private List<YqmStoreSkuDTO> getOther(List<YqmStoreSkuDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }

        return dtoList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmStoreSkuDTO> list(YqmStoreSkuRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreSku> list = iYqmStoreSkuService.list(iYqmStoreSkuService.getQuery(request));
        return YqmStoreSkuToDTO.toYqmStoreSkuDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmStoreSkuDTO getById(String id) {
        YqmStoreSku entity = iYqmStoreSkuService.getById(id);
        return YqmStoreSkuToDTO.toYqmStoreSkuDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmStoreSkuDTO save(YqmStoreSkuRequest request) {
        User user = UserInfoService.getUser();
        YqmStoreSku entity = YqmStoreSkuToDTO.toYqmStoreSku(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmStoreSkuService.saveOrUpdate(entity);

        YqmStoreSkuDTO dto = YqmStoreSkuToDTO.toYqmStoreSkuDTO(entity);
        if (StringUtils.isEmpty(request.getId())) {
            applicationContext.publishEvent(new SkuAddEvent(dto));
        } else if (YqmDefine.StatusType.delete.getValue().equals(request.getStatus())) {
            applicationContext.publishEvent(new SkuDeleteEvent(YqmStoreSkuToDTO.requestToYqmStoreSkuDTO(request)));
        } else {
            applicationContext.publishEvent(new SkuUpdateEvent(dto));
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
        YqmStoreSku entity = iYqmStoreSkuService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmStoreSkuToDTO.toYqmStoreSkuRequest(entity));
        YqmStoreSkuDTO dto = YqmStoreSkuToDTO.toYqmStoreSkuDTO(entity);
        applicationContext.publishEvent(new SkuDeleteEvent(dto));
        return id;
    }


}
