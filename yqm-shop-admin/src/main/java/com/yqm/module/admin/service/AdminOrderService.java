package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yqm.common.conversion.YqmOrderToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpRegionDTO;
import com.yqm.common.dto.YqmOrderDTO;
import com.yqm.common.entity.YqmOrder;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.YqmOrderLogRequest;
import com.yqm.common.request.YqmOrderRequest;
import com.yqm.common.service.ITpRegionService;
import com.yqm.common.service.IYqmOrderService;
import com.yqm.common.utils.BigDecimalUtils;
import com.yqm.module.common.service.OrderLogService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 管理端-订单
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminOrderService {


    private final IYqmOrderService iYqmOrderService;
    private final ITpRegionService iTpRegionService;
    private final OrderLogService orderLogService;

    public AdminOrderService(IYqmOrderService iYqmOrderService, ITpRegionService iTpRegionService, OrderLogService orderLogService) {
        this.iYqmOrderService = iYqmOrderService;
        this.iTpRegionService = iTpRegionService;
        this.orderLogService = orderLogService;
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
     * 保存/修改
     *
     * @param requestList
     * @return
     */
    public List<YqmOrderDTO> saveList(List<YqmOrderRequest> requestList) {
        User user = UserInfoService.getUser();

        List<YqmOrder> entityList = requestList.stream().map(request -> {
            YqmOrder entity = YqmOrderToDTO.toYqmOrder(request);
            if (StringUtils.isEmpty(request.getId())) {
                entity.setCreatedTime(LocalDateTime.now());
                entity.setCreatedBy(user.getId());
            }

            entity.setUpdatedBy(user.getId());
            entity.setUpdatedTime(LocalDateTime.now());

            return entity;
        }).collect(Collectors.toList());


        iYqmOrderService.saveOrUpdateBatch(entityList);
        return YqmOrderToDTO.toYqmOrderDTOList(entityList);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmOrder entity = iYqmOrderService.getById(id);
        if (Objects.isNull(entity)) {
            throw new YqmException("订单不存在");
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmOrderToDTO.toYqmOrderRequest(entity));
        return id;
    }

    /**
     * 更新收件人信息
     *
     * @param request
     * @return
     */
    public String updateAddress(YqmOrderRequest request) {
        if (StringUtils.isEmpty(request.getId())) {
            throw new YqmException("订单id不能为空");
        }
        YqmOrder entity = iYqmOrderService.getById(request.getId());
        if (Objects.isNull(entity)) {
            throw new YqmException("订单不存在");
        }

        List<Integer> orderStatusList = Lists.newArrayList(YqmDefine.OrderStatus.wait_delivery.getValue(), YqmDefine.OrderStatus.already_payment.getValue(), YqmDefine.OrderStatus.wait_payment.getValue());
        if (!orderStatusList.contains(entity.getOrderStatus())) {
            throw new YqmException("订单状态错误");
        }

        entity.setProvinceId(request.getProvinceId());
        entity.setAreaId(request.getAreaId());
        entity.setCityId(request.getCityId());
        entity.setShippingAddress(request.getShippingAddress());
        entity.setShippingName(request.getShippingName());
        entity.setShippingPhone(request.getShippingPhone());

        List<TpRegionDTO> regionDTOList = iTpRegionService.getIdList(Arrays.asList(request.getProvinceId(), request.getAreaId(), request.getCityId()));
        if (CollectionUtils.isNotEmpty(regionDTOList)) {
            Map<String, TpRegionDTO> regionDTOMap = regionDTOList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
            TpRegionDTO provinceRegionDTO = regionDTOMap.get(request.getProvinceId());
            if (Objects.nonNull(provinceRegionDTO)) {
                entity.setProvinceName(provinceRegionDTO.getName());
            }
            TpRegionDTO areaRegionDTO = regionDTOMap.get(request.getAreaId());
            if (Objects.nonNull(areaRegionDTO)) {
                entity.setAreaName(areaRegionDTO.getName());
            }
            TpRegionDTO cityRegionDTO = regionDTOMap.get(request.getCityId());
            if (Objects.nonNull(cityRegionDTO)) {
                entity.setCityName(cityRegionDTO.getName());
            }
        }

        this.save(YqmOrderToDTO.toYqmOrderRequest(entity));


        YqmOrderLogRequest orderLogRequest = YqmOrderLogRequest.builder()
                .orderId(entity.getId())
                .orderStatus(entity.getOrderStatus())
                .userType(YqmDefine.UserType.admin.getValue())
                .note(YqmDefine.OrderLogNote.update_address.getValue())
                .build();
        orderLogService.insertLog(orderLogRequest);
        return request.getId();
    }

    /**
     * 更新订单金额
     *
     * @param request
     * @return
     */
    public String updateAmount(YqmOrderRequest request) {
        if (StringUtils.isEmpty(request.getId())) {
            throw new YqmException("订单id不能为空");
        }
        YqmOrder entity = iYqmOrderService.getById(request.getId());
        if (Objects.isNull(entity)) {
            throw new YqmException("订单不存在");
        }

        List<Integer> orderStatusList = Lists.newArrayList(YqmDefine.OrderStatus.wait_delivery.getValue(), YqmDefine.OrderStatus.already_payment.getValue(), YqmDefine.OrderStatus.wait_payment.getValue());
        if (!orderStatusList.contains(entity.getOrderStatus())) {
            throw new YqmException("订单状态错误");
        }


        // 抵扣金额
        if (StringUtils.isNotEmpty(request.getDiscountAmount())) {
            BigDecimal newDiscountAmount = new BigDecimal(request.getDiscountAmount());
            BigDecimal oldDiscountAmount = new BigDecimal(entity.getDiscountAmount());
            BigDecimal amountPayable = new BigDecimal(entity.getAmountPayable());

            if (newDiscountAmount.compareTo(BigDecimal.ZERO) == 0) {
                entity.setAmountPayable(BigDecimalUtils.addRoundHalfUp(amountPayable, oldDiscountAmount).toString());
            } else {
                BigDecimal discountAmount = BigDecimalUtils.subtractionRoundHalfUp(newDiscountAmount, oldDiscountAmount);
                entity.setAmountPayable(BigDecimalUtils.subtractionRoundHalfUp(amountPayable, discountAmount).toString());
            }

        }

        // 运费
        if (StringUtils.isNotEmpty(request.getFreight())) {
            BigDecimal newFreight = new BigDecimal(request.getFreight());
            BigDecimal oldFreight = new BigDecimal(entity.getFreight());
            BigDecimal amountPayable = new BigDecimal(entity.getAmountPayable());

            if (newFreight.compareTo(BigDecimal.ZERO) == 0) {
                entity.setAmountPayable(BigDecimalUtils.subtractionRoundHalfUp(amountPayable, oldFreight).toString());
            } else {
                BigDecimal freight = BigDecimalUtils.subtractionRoundHalfUp(newFreight, oldFreight);
                entity.setAmountPayable(BigDecimalUtils.addRoundHalfUp(amountPayable, freight).toString());
            }

        }

        entity.setDiscountAmount(request.getDiscountAmount());
        entity.setFreight(request.getFreight());
        this.save(YqmOrderToDTO.toYqmOrderRequest(entity));


        YqmOrderLogRequest orderLogRequest = YqmOrderLogRequest.builder()
                .orderId(entity.getId())
                .orderStatus(entity.getOrderStatus())
                .userType(YqmDefine.UserType.admin.getValue())
                .note(String.format(YqmDefine.OrderLogNote.update_amount.getValue(), request.getFreight(), request.getDiscountAmount()))
                .build();
        orderLogService.insertLog(orderLogRequest);
        return request.getId();
    }

    /**
     * 关闭订单
     *
     * @param request
     * @return
     */
    public String closeOrder(YqmOrderRequest request) {
        if (StringUtils.isEmpty(request.getId())) {
            throw new YqmException("订单id不能为空");
        }
        if (StringUtils.isEmpty(request.getNote())) {
            throw new YqmException("订单备注不能为空");
        }
        YqmOrder entity = iYqmOrderService.getById(request.getId());
        if (Objects.isNull(entity)) {
            throw new YqmException("订单不存在");
        }

        List<Integer> orderStatusList = Lists.newArrayList(YqmDefine.OrderStatus.close.getValue());
        if (orderStatusList.contains(entity.getOrderStatus())) {
            throw new YqmException("订单状态错误");
        }


        entity.setOrderStatus(YqmDefine.OrderStatus.close.getValue());
        this.save(YqmOrderToDTO.toYqmOrderRequest(entity));

        YqmOrderLogRequest orderLogRequest = YqmOrderLogRequest.builder()
                .orderId(entity.getId())
                .orderStatus(entity.getOrderStatus())
                .userType(YqmDefine.UserType.admin.getValue())
                .note(String.format(YqmDefine.OrderLogNote.close.getValue(), request.getNote()))
                .build();
        orderLogService.insertLog(orderLogRequest);
        return request.getId();
    }

    /**
     * 关闭订单
     *
     * @param request
     * @return
     */
    public String orderNote(YqmOrderRequest request) {
        if (StringUtils.isEmpty(request.getId())) {
            throw new YqmException("订单id不能为空");
        }
        if (StringUtils.isEmpty(request.getNote())) {
            throw new YqmException("订单备注不能为空");
        }
        YqmOrder entity = iYqmOrderService.getById(request.getId());
        if (Objects.isNull(entity)) {
            throw new YqmException("订单不存在");
        }

//        this.save(YqmOrderToDTO.toYqmOrderRequest(entity));

        YqmOrderLogRequest orderLogRequest = YqmOrderLogRequest.builder()
                .orderId(entity.getId())
                .orderStatus(entity.getOrderStatus())
                .userType(YqmDefine.UserType.admin.getValue())
                .note(String.format(YqmDefine.OrderLogNote.note.getValue(), request.getNote()))
                .build();
        orderLogService.insertLog(orderLogRequest);
        return request.getId();
    }

    /**
     * 确认订单
     * @param request
     * @return
     */
    public List<String> confirmOrder(YqmOrderRequest request) {
        if (CollectionUtils.isEmpty(request.getInIdList())) {
            throw new YqmException("订单Id不能为空");
        }

        List<YqmOrder> orderList = iYqmOrderService.list(iYqmOrderService.getQuery(request));
        if (CollectionUtils.isEmpty(orderList)) {
            throw new YqmException("订单不存在");
        }

        List<YqmOrderRequest> orderRequests = orderList.stream().map(e -> {
            if (YqmDefine.OrderStatus.already_delivery.getValue().equals(e.getOrderStatus())) {
                throw new YqmException("订单状态错误，请刷新重试");
            }
            e.setOrderStatus(YqmDefine.OrderStatus.completed_delivery.getValue());
            return YqmOrderToDTO.toYqmOrderRequest(e);
        }).collect(Collectors.toList());

        this.saveList(orderRequests);

        return request.getInIdList();
    }
}
