package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmHelpClassificationToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmHelpClassificationDTO;
import com.yqm.common.entity.YqmHelpClassification;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmHelpClassificationRequest;
import com.yqm.common.request.YqmHelpRequest;
import com.yqm.common.service.IYqmHelpClassificationService;
import com.yqm.common.service.IYqmHelpService;
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
 * 管理端-帮助分类
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminHelpClassificationService {

    private final IYqmHelpClassificationService iYqmHelpClassificationService;
    private final IYqmHelpService iYqmHelpService;

    public AdminHelpClassificationService(IYqmHelpClassificationService iYqmHelpClassificationService, IYqmHelpService iYqmHelpService) {
        this.iYqmHelpClassificationService = iYqmHelpClassificationService;
        this.iYqmHelpService = iYqmHelpService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmHelpClassificationDTO> page(YqmHelpClassificationRequest request) {
        Page<YqmHelpClassification> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmHelpClassificationService.page(page, iYqmHelpClassificationService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {

            List<YqmHelpClassificationDTO> projectClassificationDTOS = YqmHelpClassificationToDTO.toYqmHelpClassificationDTOList(pageList.getRecords());
            pageList.setRecords(projectClassificationDTOS);

            if (CollectionUtils.isNotEmpty(projectClassificationDTOS)) {
                for (YqmHelpClassificationDTO dto : projectClassificationDTOS) {
                    YqmHelpRequest projectRequest = new YqmHelpRequest();
                    projectRequest.setClassificationId(dto.getId());
                    projectRequest.setInStatusList(YqmDefine.includeStatus);
                    long count = iYqmHelpService.count(iYqmHelpService.getQuery(projectRequest));
                    dto.setHelpNum(count);
                }

            }


        }
        return pageList;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmHelpClassificationDTO getById(String id) {
        YqmHelpClassification entity = iYqmHelpClassificationService.getById(id);
        return YqmHelpClassificationToDTO.toYqmHelpClassificationDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmHelpClassificationDTO save(YqmHelpClassificationRequest request) {
        User user = UserInfoService.getUser();
        YqmHelpClassification entity = YqmHelpClassificationToDTO.toYqmHelpClassification(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmHelpClassificationService.saveOrUpdate(entity);
        return YqmHelpClassificationToDTO.toYqmHelpClassificationDTO(entity);
    }

    /**
     * 是否显示
     *
     * @param request
     * @return
     */
    public YqmHelpClassificationDTO isShow(YqmHelpClassificationRequest request) {
        YqmHelpClassification classification = iYqmHelpClassificationService.getById(request.getId());
        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            throw new YqmException("状态错误");
        }
        classification.setStatus(request.getStatus());
        return this.save(YqmHelpClassificationToDTO.toYqmHelpClassificationRequest(classification));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmHelpClassification entity = iYqmHelpClassificationService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmHelpClassificationToDTO.toYqmHelpClassificationRequest(entity));
        return id;
    }

    /**
     * 集合
     *
     * @param request
     * @return
     */
    public List<YqmHelpClassificationDTO> list(YqmHelpClassificationRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmHelpClassification> list = iYqmHelpClassificationService.list(iYqmHelpClassificationService.getQuery(request));
        return YqmHelpClassificationToDTO.toYqmHelpClassificationDTOList(list);
    }
}
