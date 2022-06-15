package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmHelpToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmHelpDTO;
import com.yqm.common.entity.YqmHelp;
import com.yqm.common.request.YqmHelpRequest;
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
public class AdminHelpService {

    private final IYqmHelpService iYqmHelpService;


    public AdminHelpService(IYqmHelpService iYqmHelpService) {
        this.iYqmHelpService = iYqmHelpService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmHelpDTO> page(YqmHelpRequest request) {
        Page<YqmHelp> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmHelpService.page(page, iYqmHelpService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmHelpDTO> projectDTOS = YqmHelpToDTO.toYqmHelpDTOList(pageList.getRecords());
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
    public YqmHelpDTO getById(String id) {
        YqmHelp entity = iYqmHelpService.getById(id);
        return YqmHelpToDTO.toYqmHelpDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmHelpDTO save(YqmHelpRequest request) {
        User user = UserInfoService.getUser();
        YqmHelp entity = YqmHelpToDTO.toYqmHelp(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());

        iYqmHelpService.saveOrUpdate(entity);

        return YqmHelpToDTO.toYqmHelpDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmHelp entity = iYqmHelpService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmHelpToDTO.toYqmHelpRequest(entity));
        return id;
    }

    /**
     * 集合
     *
     * @param request
     * @return
     */
    public List<YqmHelpDTO> list(YqmHelpRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmHelp> list = iYqmHelpService.list(iYqmHelpService.getQuery(request));
        return YqmHelpToDTO.toYqmHelpDTOList(list);
    }
}
