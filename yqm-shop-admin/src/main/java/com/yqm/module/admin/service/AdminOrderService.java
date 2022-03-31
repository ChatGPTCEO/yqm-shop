package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmOrderToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmOrderDTO;
import com.yqm.common.entity.YqmOrder;
import com.yqm.common.request.YqmOrderRequest;
import com.yqm.common.service.IYqmOrderService;
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
 * 管理端-订单
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminOrderService {


    private final IYqmOrderService iYqmOrderService;

    public AdminOrderService(IYqmOrderService iYqmOrderService) {
        this.iYqmOrderService = iYqmOrderService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmOrderDTO> page(YqmOrderRequest request) {
        Page<YqmOrder> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmOrderService.page(page, iYqmOrderService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmOrderToDTO.toYqmOrderDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmOrderDTO> list(YqmOrderRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmOrder> list = iYqmOrderService.list(iYqmOrderService.getQuery(request));
        return YqmOrderToDTO.toYqmOrderDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmOrderDTO getById(String id) {
        YqmOrder entity = iYqmOrderService.getById(id);
        return YqmOrderToDTO.toYqmOrderDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmOrderDTO save(YqmOrderRequest request) {
        User user = UserInfoService.getUser();
        YqmOrder entity = YqmOrderToDTO.toYqmOrder(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmOrderService.saveOrUpdate(entity);
        return YqmOrderToDTO.toYqmOrderDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmOrder brand = iYqmOrderService.getById(id);
        if (Objects.isNull(brand)) {
            return id;
        }
        brand.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmOrderToDTO.toYqmOrderRequest(brand));
        return id;
    }


}
