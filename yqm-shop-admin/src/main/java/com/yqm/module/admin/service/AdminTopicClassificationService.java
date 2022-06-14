package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmTopicClassificationToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmTopicClassificationDTO;
import com.yqm.common.entity.YqmTopicClassification;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmTopicClassificationRequest;
import com.yqm.common.request.YqmTopicRequest;
import com.yqm.common.service.IYqmTopicClassificationService;
import com.yqm.common.service.IYqmTopicService;
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
 * 管理端-话题分类
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminTopicClassificationService {

    private final IYqmTopicClassificationService iYqmTopicClassificationService;
    private final IYqmTopicService iYqmTopicService;

    public AdminTopicClassificationService(IYqmTopicClassificationService iYqmTopicClassificationService, IYqmTopicService iYqmTopicService) {
        this.iYqmTopicClassificationService = iYqmTopicClassificationService;
        this.iYqmTopicService = iYqmTopicService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmTopicClassificationDTO> page(YqmTopicClassificationRequest request) {
        Page<YqmTopicClassification> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmTopicClassificationService.page(page, iYqmTopicClassificationService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {

            List<YqmTopicClassificationDTO> projectClassificationDTOS = YqmTopicClassificationToDTO.toYqmTopicClassificationDTOList(pageList.getRecords());
            pageList.setRecords(projectClassificationDTOS);

            if (CollectionUtils.isNotEmpty(projectClassificationDTOS)) {
                for (YqmTopicClassificationDTO dto : projectClassificationDTOS) {
                    YqmTopicRequest projectRequest = new YqmTopicRequest();
                    projectRequest.setClassificationId(dto.getId());
                    projectRequest.setInStatusList(YqmDefine.includeStatus);
                    long count = iYqmTopicService.count(iYqmTopicService.getQuery(projectRequest));
                    dto.setTopicNum(count);
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
    public YqmTopicClassificationDTO getById(String id) {
        YqmTopicClassification entity = iYqmTopicClassificationService.getById(id);
        return YqmTopicClassificationToDTO.toYqmTopicClassificationDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmTopicClassificationDTO save(YqmTopicClassificationRequest request) {
        User user = UserInfoService.getUser();
        YqmTopicClassification entity = YqmTopicClassificationToDTO.toYqmTopicClassification(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmTopicClassificationService.saveOrUpdate(entity);
        return YqmTopicClassificationToDTO.toYqmTopicClassificationDTO(entity);
    }

    /**
     * 是否显示
     *
     * @param request
     * @return
     */
    public YqmTopicClassificationDTO isShow(YqmTopicClassificationRequest request) {
        YqmTopicClassification classification = iYqmTopicClassificationService.getById(request.getId());
        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            throw new YqmException("状态错误");
        }
        classification.setStatus(request.getStatus());
        return this.save(YqmTopicClassificationToDTO.toYqmTopicClassificationRequest(classification));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmTopicClassification entity = iYqmTopicClassificationService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmTopicClassificationToDTO.toYqmTopicClassificationRequest(entity));
        return id;
    }

    /**
     * 集合
     *
     * @param request
     * @return
     */
    public List<YqmTopicClassificationDTO> list(YqmTopicClassificationRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmTopicClassification> list = iYqmTopicClassificationService.list(iYqmTopicClassificationService.getQuery(request));
        return YqmTopicClassificationToDTO.toYqmTopicClassificationDTOList(list);
    }
}
