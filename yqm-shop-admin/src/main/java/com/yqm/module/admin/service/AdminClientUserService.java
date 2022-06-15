package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmUserToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmUserDTO;
import com.yqm.common.entity.YqmUser;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmUserRequest;
import com.yqm.common.service.IYqmProjectService;
import com.yqm.common.service.IYqmUserService;
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
public class AdminClientUserService {

    private final IYqmUserService iYqmUserService;
    private final IYqmProjectService iYqmProjectService;

    public AdminClientUserService(IYqmUserService iYqmUserService, IYqmProjectService iYqmProjectService) {
        this.iYqmUserService = iYqmUserService;
        this.iYqmProjectService = iYqmProjectService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmUserDTO> page(YqmUserRequest request) {
        Page<YqmUser> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmUserService.page(page, iYqmUserService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {

            List<YqmUserDTO> projectClassificationDTOS = YqmUserToDTO.toYqmUserDTOList(pageList.getRecords());
            pageList.setRecords(projectClassificationDTOS);

        }
        return pageList;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmUserDTO getById(String id) {
        YqmUser entity = iYqmUserService.getById(id);
        return YqmUserToDTO.toYqmUserDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmUserDTO save(YqmUserRequest request) {
        User user = UserInfoService.getUser();
        YqmUser entity = YqmUserToDTO.toYqmUser(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmUserService.saveOrUpdate(entity);
        return YqmUserToDTO.toYqmUserDTO(entity);
    }

    /**
     * 是否显示
     *
     * @param request
     * @return
     */
    public YqmUserDTO isShow(YqmUserRequest request) {
        YqmUser classification = iYqmUserService.getById(request.getId());
        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            throw new YqmException("状态错误");
        }
        classification.setStatus(request.getStatus());
        return this.save(YqmUserToDTO.toYqmUserRequest(classification));
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmUser entity = iYqmUserService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmUserToDTO.toYqmUserRequest(entity));
        return id;
    }

    /**
     * 集合
     *
     * @param request
     * @return
     */
    public List<YqmUserDTO> list(YqmUserRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmUser> list = iYqmUserService.list(iYqmUserService.getQuery(request));
        return YqmUserToDTO.toYqmUserDTOList(list);
    }
}
