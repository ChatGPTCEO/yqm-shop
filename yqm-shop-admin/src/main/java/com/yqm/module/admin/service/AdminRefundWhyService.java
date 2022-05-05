package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmRefundWhyToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmRefundWhyDTO;
import com.yqm.common.entity.YqmRefundWhy;
import com.yqm.common.request.YqmRefundWhyRequest;
import com.yqm.common.service.IYqmRefundWhyService;
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
 * 管理端-退款原因
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminRefundWhyService {


    private final IYqmRefundWhyService iYqmRefundWhyService;

    public AdminRefundWhyService(IYqmRefundWhyService iYqmRefundWhyService) {
        this.iYqmRefundWhyService = iYqmRefundWhyService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmRefundWhyDTO> page(YqmRefundWhyRequest request) {
        Page<YqmRefundWhy> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmRefundWhyService.page(page, iYqmRefundWhyService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmRefundWhyToDTO.toYqmRefundWhyDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmRefundWhyDTO> list(YqmRefundWhyRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmRefundWhy> list = iYqmRefundWhyService.list(iYqmRefundWhyService.getQuery(request));
        return YqmRefundWhyToDTO.toYqmRefundWhyDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmRefundWhyDTO getById(String id) {
        YqmRefundWhy entity = iYqmRefundWhyService.getById(id);
        return YqmRefundWhyToDTO.toYqmRefundWhyDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmRefundWhyDTO save(YqmRefundWhyRequest request) {
        User user = UserInfoService.getUser();
        YqmRefundWhy entity = YqmRefundWhyToDTO.toYqmRefundWhy(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmRefundWhyService.saveOrUpdate(entity);
        return YqmRefundWhyToDTO.toYqmRefundWhyDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmRefundWhy refundWhy = iYqmRefundWhyService.getById(id);
        if (Objects.isNull(refundWhy)) {
            return id;
        }
        refundWhy.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmRefundWhyToDTO.toYqmRefundWhyRequest(refundWhy));
        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enable(String id) {
        YqmRefundWhy refundWhy = iYqmRefundWhyService.getById(id);
        if (Objects.isNull(refundWhy)) {
            return id;
        }
        if (YqmDefine.StatusType.effective.getValue().equals(refundWhy.getStatus())) {
            refundWhy.setStatus(YqmDefine.StatusType.failure.getValue());
        } else if (YqmDefine.StatusType.failure.getValue().equals(refundWhy.getStatus())) {
            refundWhy.setStatus(YqmDefine.StatusType.effective.getValue());
        }

        this.save(YqmRefundWhyToDTO.toYqmRefundWhyRequest(refundWhy));
        return id;
    }

}
