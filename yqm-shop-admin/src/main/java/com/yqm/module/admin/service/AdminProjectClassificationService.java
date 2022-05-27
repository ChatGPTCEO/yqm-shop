package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmProjectClassificationToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmProjectClassificationDTO;
import com.yqm.common.entity.YqmProjectClassification;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmProjectClassificationRequest;
import com.yqm.common.service.IYqmProjectClassificationService;
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
 * 管理端-专题分类
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminProjectClassificationService {

    private final IYqmProjectClassificationService iYqmProjectClassificationService;

    public AdminProjectClassificationService(IYqmProjectClassificationService iYqmProjectClassificationService) {
        this.iYqmProjectClassificationService = iYqmProjectClassificationService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmProjectClassificationDTO> page(YqmProjectClassificationRequest request) {
        Page<YqmProjectClassification> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmProjectClassificationService.page(page, iYqmProjectClassificationService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmProjectClassificationToDTO.toYqmProjectClassificationDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmProjectClassificationDTO getById(String id) {
        YqmProjectClassification entity = iYqmProjectClassificationService.getById(id);
        return YqmProjectClassificationToDTO.toYqmProjectClassificationDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmProjectClassificationDTO save(YqmProjectClassificationRequest request) {
        User user = UserInfoService.getUser();
        YqmProjectClassification entity = YqmProjectClassificationToDTO.toYqmProjectClassification(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmProjectClassificationService.saveOrUpdate(entity);
        return YqmProjectClassificationToDTO.toYqmProjectClassificationDTO(entity);
    }

    /**
     * 是否显示
     *
     * @param request
     * @return
     */
    public YqmProjectClassificationDTO isShow(YqmProjectClassificationRequest request) {
        YqmProjectClassification classification = iYqmProjectClassificationService.getById(request.getId());
        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            throw new YqmException("状态错误");
        }
        classification.setStatus(request.getStatus());
        return this.save(YqmProjectClassificationToDTO.toYqmProjectClassificationRequest(classification));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmProjectClassification entity = iYqmProjectClassificationService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmProjectClassificationToDTO.toYqmProjectClassificationRequest(entity));
        return id;
    }

    /**
     * 集合
     *
     * @param request
     * @return
     */
    public List<YqmProjectClassificationDTO> list(YqmProjectClassificationRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmProjectClassification> list = iYqmProjectClassificationService.list(iYqmProjectClassificationService.getQuery(request));
        return YqmProjectClassificationToDTO.toYqmProjectClassificationDTOList(list);
    }
}
