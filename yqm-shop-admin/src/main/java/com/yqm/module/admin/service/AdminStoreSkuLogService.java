package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmStoreSkuLogToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreSkuLogDTO;
import com.yqm.common.entity.YqmStoreSkuLog;
import com.yqm.common.request.YqmStoreSkuLogRequest;
import com.yqm.common.service.IYqmStoreSkuLogService;
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
 * 管理端-商品类型
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminStoreSkuLogService {


    private final IYqmStoreSkuLogService iYqmStoreSkuLogService;

    public AdminStoreSkuLogService(IYqmStoreSkuLogService iYqmStoreSkuLogService) {
        this.iYqmStoreSkuLogService = iYqmStoreSkuLogService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmStoreSkuLogDTO> page(YqmStoreSkuLogRequest request) {
        Page<YqmStoreSkuLog> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmStoreSkuLogService.page(page, iYqmStoreSkuLogService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmStoreSkuLogToDTO.toYqmStoreSkuLogDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmStoreSkuLogDTO> list(YqmStoreSkuLogRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmStoreSkuLog> list = iYqmStoreSkuLogService.list(iYqmStoreSkuLogService.getQuery(request));
        return YqmStoreSkuLogToDTO.toYqmStoreSkuLogDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmStoreSkuLogDTO getById(String id) {
        YqmStoreSkuLog entity = iYqmStoreSkuLogService.getById(id);
        return YqmStoreSkuLogToDTO.toYqmStoreSkuLogDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmStoreSkuLogDTO save(YqmStoreSkuLogRequest request) {
        User user = UserInfoService.getUser();
        YqmStoreSkuLog entity = YqmStoreSkuLogToDTO.toYqmStoreSkuLog(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmStoreSkuLogService.saveOrUpdate(entity);
        return YqmStoreSkuLogToDTO.toYqmStoreSkuLogDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmStoreSkuLog brand = iYqmStoreSkuLogService.getById(id);
        if (Objects.isNull(brand)) {
            return id;
        }
        brand.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmStoreSkuLogToDTO.toYqmStoreSkuLogRequest(brand));
        return id;
    }


}
