package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmOrderItemToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmOrderItemDTO;
import com.yqm.common.entity.YqmOrderItem;
import com.yqm.common.request.YqmOrderItemRequest;
import com.yqm.common.service.IYqmOrderItemService;
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
 * 管理端-订单子表
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminOrderItemService {


    private final IYqmOrderItemService iYqmOrderItemService;

    public AdminOrderItemService(IYqmOrderItemService iYqmOrderItemService) {
        this.iYqmOrderItemService = iYqmOrderItemService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmOrderItemDTO> page(YqmOrderItemRequest request) {
        Page<YqmOrderItem> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmOrderItemService.page(page, iYqmOrderItemService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmOrderItemToDTO.toYqmOrderItemDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmOrderItemDTO> list(YqmOrderItemRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmOrderItem> list = iYqmOrderItemService.list(iYqmOrderItemService.getQuery(request));
        return YqmOrderItemToDTO.toYqmOrderItemDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmOrderItemDTO getById(String id) {
        YqmOrderItem entity = iYqmOrderItemService.getById(id);
        return YqmOrderItemToDTO.toYqmOrderItemDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmOrderItemDTO save(YqmOrderItemRequest request) {
        User user = UserInfoService.getUser();
        YqmOrderItem entity = YqmOrderItemToDTO.toYqmOrderItem(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmOrderItemService.saveOrUpdate(entity);
        return YqmOrderItemToDTO.toYqmOrderItemDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmOrderItem brand = iYqmOrderItemService.getById(id);
        if (Objects.isNull(brand)) {
            return id;
        }
        brand.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmOrderItemToDTO.toYqmOrderItemRequest(brand));
        return id;
    }


}
