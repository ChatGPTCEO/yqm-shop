package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmStoreSpecToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreSpecDTO;
import com.yqm.common.entity.YqmStoreSpec;
import com.yqm.common.request.YqmStoreSpecRequest;
import com.yqm.common.service.IYqmStoreSpecService;
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
public class AdminStoreSpecService {

    private final ApplicationContext applicationContext;
    private final IYqmStoreSpecService iYqmStoreSpecService;
    private final IYqmStoreTypeService iYqmStoreTypeService;

    public AdminStoreSpecService(ApplicationContext applicationContext, IYqmStoreSpecService iYqmStoreSpecService,
                                 IYqmStoreTypeService iYqmStoreTypeService) {
        this.applicationContext = applicationContext;
        this.iYqmStoreSpecService = iYqmStoreSpecService;
        this.iYqmStoreTypeService = iYqmStoreTypeService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmStoreSpecDTO> page(YqmStoreSpecRequest request) {
        Page<YqmStoreSpec> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmStoreSpecService.page(page, iYqmStoreSpecService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmStoreSpecDTO> dtoList = YqmStoreSpecToDTO.toYqmStoreSpecDTOList(pageList.getRecords());
            this.getOther(dtoList);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    private List<YqmStoreSpecDTO> getOther(List<YqmStoreSpecDTO> dtoList) {
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
    public List<YqmStoreSpecDTO> list(YqmStoreSpecRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreSpec> list = iYqmStoreSpecService.list(iYqmStoreSpecService.getQuery(request));
        return YqmStoreSpecToDTO.toYqmStoreSpecDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmStoreSpecDTO getById(String id) {
        YqmStoreSpec entity = iYqmStoreSpecService.getById(id);
        return YqmStoreSpecToDTO.toYqmStoreSpecDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmStoreSpecDTO save(YqmStoreSpecRequest request) {
        User user = UserInfoService.getUser();
        YqmStoreSpec entity = YqmStoreSpecToDTO.toYqmStoreSpec(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmStoreSpecService.saveOrUpdate(entity);

        YqmStoreSpecDTO dto = YqmStoreSpecToDTO.toYqmStoreSpecDTO(entity);
        return dto;
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public List<YqmStoreSpecDTO> saveList(List<YqmStoreSpecRequest> request) {
        User user = UserInfoService.getUser();
        List<YqmStoreSpec> entityList = YqmStoreSpecToDTO.requestToYqmStoreSpecList(request);
        entityList.forEach(e -> {
            if (StringUtils.isEmpty(e.getId())) {
                e.setCreatedTime(LocalDateTime.now());
                e.setCreatedBy(user.getId());
            }

            e.setUpdatedBy(user.getId());
            e.setUpdatedTime(LocalDateTime.now());
        });

        iYqmStoreSpecService.saveOrUpdateBatch(entityList);

        List<YqmStoreSpecDTO> dtoList = YqmStoreSpecToDTO.entityToYqmStoreSpecDTOList(entityList);
        return dtoList;
    }

    /**
     * 批量
     *
     * @param request
     * @return
     */
    public YqmStoreSpecDTO saveSpecChild(YqmStoreSpecRequest request) {
        request.setIsParent(YqmDefine.SpecType.attr.getValue());
        this.save(request);
        List<YqmStoreSpecDTO> storeSpecDTOList = null;
        if (CollectionUtils.isNotEmpty(request.getInputValue())) {
            request.getInputValue().forEach(e -> {
                e.setPid(request.getId());
                e.setIsParent(YqmDefine.SpecType.spec.getValue());
                e.setProductId(request.getProductId());
            });

            storeSpecDTOList = this.saveList(request.getInputValue());
        }

        YqmStoreSpecDTO storeSpecDTO = YqmStoreSpecToDTO.requestToYqmStoreSpec(request);
        storeSpecDTO.setInputValue(storeSpecDTOList);
        return storeSpecDTO;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmStoreSpec entity = iYqmStoreSpecService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmStoreSpecToDTO.toYqmStoreSpecRequest(entity));
        YqmStoreSpecDTO dto = YqmStoreSpecToDTO.toYqmStoreSpecDTO(entity);
        return id;
    }


}
