package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmTopicToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmTopicDTO;
import com.yqm.common.entity.YqmTopic;
import com.yqm.common.request.YqmTopicRequest;
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
 * 管理端-专题
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminTopicService {

    private final IYqmTopicService iYqmTopicService;


    public AdminTopicService(IYqmTopicService iYqmTopicService) {
        this.iYqmTopicService = iYqmTopicService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmTopicDTO> page(YqmTopicRequest request) {
        Page<YqmTopic> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmTopicService.page(page, iYqmTopicService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmTopicDTO> projectDTOS = YqmTopicToDTO.toYqmTopicDTOList(pageList.getRecords());
            pageList.setRecords(projectDTOS);
        }
        return pageList;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmTopicDTO getById(String id) {
        YqmTopic entity = iYqmTopicService.getById(id);
        return YqmTopicToDTO.toYqmTopicDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmTopicDTO save(YqmTopicRequest request) {
        User user = UserInfoService.getUser();
        YqmTopic entity = YqmTopicToDTO.toYqmTopic(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());

        iYqmTopicService.saveOrUpdate(entity);

        return YqmTopicToDTO.toYqmTopicDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmTopic entity = iYqmTopicService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmTopicToDTO.toYqmTopicRequest(entity));
        return id;
    }

    /**
     * 集合
     *
     * @param request
     * @return
     */
    public List<YqmTopicDTO> list(YqmTopicRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmTopic> list = iYqmTopicService.list(iYqmTopicService.getQuery(request));
        return YqmTopicToDTO.toYqmTopicDTOList(list);
    }
}
