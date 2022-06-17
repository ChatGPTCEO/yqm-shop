package com.yqm.modules.sales.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.AfterSalesStatusEnum;
import com.yqm.enums.OrderInfoEnum;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.exception.ErrorRequestException;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.order.domain.YqmStoreOrder;
import com.yqm.modules.order.domain.YqmStoreOrderCartInfo;
import com.yqm.modules.order.service.mapper.StoreOrderCartInfoMapper;
import com.yqm.modules.order.service.mapper.StoreOrderMapper;
import com.yqm.modules.sales.domain.StoreAfterSales;
import com.yqm.modules.sales.domain.StoreAfterSalesItem;
import com.yqm.modules.sales.domain.StoreAfterSalesStatus;
import com.yqm.modules.sales.param.ProsuctParam;
import com.yqm.modules.sales.param.StoreAfterSalesParam;
import com.yqm.modules.sales.param.YqmStoreAfterSalesDto;
import com.yqm.modules.sales.param.YqmStoreAfterSalesQueryCriteria;
import com.yqm.modules.sales.service.StoreAfterSalesService;
import com.yqm.modules.sales.service.mapper.StoreAfterSalesItemMapper;
import com.yqm.modules.sales.service.mapper.StoreAfterSalesMapper;
import com.yqm.modules.sales.service.mapper.StoreAfterSalesStatusMapper;
import com.yqm.modules.sales.service.vo.StoreAfterSalesVo;
import com.yqm.modules.sales.service.vo.YqmStoreOrderCartInfoVo;
import com.yqm.utils.FileUtil;
import com.yqm.utils.SecurityUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : gzlv 2021/6/27 15:56
 */
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class StoreAfterSalesServiceImpl extends BaseServiceImpl<StoreAfterSalesMapper, StoreAfterSales> implements StoreAfterSalesService {

    private final StoreOrderMapper storeOrderMapper;
    private final StoreOrderCartInfoMapper storeOrderCartInfoMapper;
    private final StoreAfterSalesItemMapper storeAfterSalesItemMapper;
    private final StoreAfterSalesStatusMapper storeAfterSalesStatusMapper;
    private final IGenerator generator;

    @Override
    public void applyForAfterSales(Long userId, String nickname, StoreAfterSalesParam storeAfterSalesParam) {

        YqmStoreOrder yqmStoreOrder = storeOrderMapper.selectOne(Wrappers.<YqmStoreOrder>lambdaQuery().eq(YqmStoreOrder::getOrderId, storeAfterSalesParam.getOrderCode()).eq(YqmStoreOrder::getUid, userId));
        checkOrder(yqmStoreOrder);
        //商品除去优惠后的总价格
        BigDecimal totalPrice = BigDecimal.ZERO;
        //拿到所有的商品
        List<YqmStoreOrderCartInfo> yqmStoreOrderCartInfos = storeOrderCartInfoMapper.selectList(Wrappers.<YqmStoreOrderCartInfo>lambdaQuery().eq(YqmStoreOrderCartInfo::getOid, yqmStoreOrder.getId()));
        for (YqmStoreOrderCartInfo yqmStoreOrderCartInfo : yqmStoreOrderCartInfos) {
            YqmStoreCartQueryVo cartInfo = JSONObject.parseObject(yqmStoreOrderCartInfo.getCartInfo(), YqmStoreCartQueryVo.class);
            ProsuctParam prosuctParam = storeAfterSalesParam.getProductParamList().stream().filter(item -> item.getProductId().equals(yqmStoreOrderCartInfo.getProductId())).findFirst().orElse(new ProsuctParam());
            if (prosuctParam.getProductId() != null) {
                //商品优惠前总金额
                BigDecimal totalAmountOfGoods = NumberUtil.mul(cartInfo.getTruePrice(), cartInfo.getCartNum());
                //商品优惠总金额
                BigDecimal commodityDiscountAmount = NumberUtil.mul(NumberUtil.div(totalAmountOfGoods, NumberUtil.sub(yqmStoreOrder.getTotalPrice(), yqmStoreOrder.getPayPostage())), yqmStoreOrder.getCouponPrice());
                //商品优惠后总金额
                totalPrice = NumberUtil.add(totalPrice, NumberUtil.sub(totalAmountOfGoods, commodityDiscountAmount));

                yqmStoreOrderCartInfo.setIsAfterSales(0);
                storeOrderCartInfoMapper.updateById(yqmStoreOrderCartInfo);

            }
        }
        //更新订单状态
        yqmStoreOrder.setStatus(OrderInfoEnum.STATUS_NE1.getValue());
        yqmStoreOrder.setRefundStatus(OrderInfoEnum.REFUND_STATUS_1.getValue());
        yqmStoreOrder.setRefundReasonWap(storeAfterSalesParam.getReasonForApplication());
        yqmStoreOrder.setRefundReasonWapExplain(storeAfterSalesParam.getApplicationInstructions());
        yqmStoreOrder.setRefundReasonTime(new Date());
        storeOrderMapper.updateById(yqmStoreOrder);
        //生成售后订单
        StoreAfterSales storeAfterSales = new StoreAfterSales();
        storeAfterSales.setOrderCode(storeAfterSalesParam.getOrderCode());
        storeAfterSales.setRefundAmount(totalPrice);
        storeAfterSales.setServiceType(storeAfterSalesParam.getServiceType());
        storeAfterSales.setReasons(storeAfterSalesParam.getReasonForApplication());
        storeAfterSales.setExplains(storeAfterSalesParam.getApplicationInstructions());
        storeAfterSales.setExplainImg(storeAfterSalesParam.getApplicationDescriptionPicture());
        storeAfterSales.setState(AfterSalesStatusEnum.STATUS_0.getValue());
        storeAfterSales.setSalesState(0);
        storeAfterSales.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        storeAfterSales.setIsDel(0);
        storeAfterSales.setUserId(userId);
        baseMapper.insert(storeAfterSales);
        //售后商品详情
        for (ProsuctParam prosuctParam : storeAfterSalesParam.getProductParamList()) {
            YqmStoreOrderCartInfo yqmStoreOrderCartInfo = yqmStoreOrderCartInfos.stream().filter(item -> item.getProductId().equals(prosuctParam.getProductId())).findFirst().orElse(new YqmStoreOrderCartInfo());
            StoreAfterSalesItem storeAfterSalesItem = new StoreAfterSalesItem();
            storeAfterSalesItem.setStoreAfterSalesId(storeAfterSales.getId());
            storeAfterSalesItem.setProductId(yqmStoreOrderCartInfo.getProductId());
            storeAfterSalesItem.setCartInfo(yqmStoreOrderCartInfo.getCartInfo());
            storeAfterSalesItem.setIsDel(0);
            storeAfterSalesItemMapper.insert(storeAfterSalesItem);
        }

        //操作记录
        StoreAfterSalesStatus storeAfterSalesStatus = new StoreAfterSalesStatus();
        storeAfterSalesStatus.setStoreAfterSalesId(storeAfterSales.getId());
        storeAfterSalesStatus.setChangeType(0);
        storeAfterSalesStatus.setChangeMessage("售后订单生成");
        storeAfterSalesStatus.setChangeTime(Timestamp.valueOf(LocalDateTime.now()));
        storeAfterSalesStatus.setOperator(nickname);
        storeAfterSalesStatusMapper.insert(storeAfterSalesStatus);

    }

    @Override
    public List<YqmStoreOrderCartInfoVo> checkOrderDetails(String key) {
        List<YqmStoreOrderCartInfo> yqmStoreOrderCartInfos = storeOrderCartInfoMapper.selectList(Wrappers.<YqmStoreOrderCartInfo>lambdaQuery().eq(YqmStoreOrderCartInfo::getOid, key));
        YqmStoreOrder yqmStoreOrder = storeOrderMapper.selectById(key);
        //查询 售后信息
        StoreAfterSales storeAfterSales = baseMapper.selectOne(Wrappers.<StoreAfterSales>lambdaQuery().eq(StoreAfterSales::getOrderCode, yqmStoreOrder.getOrderId()));
        List<YqmStoreOrderCartInfoVo> yqmStoreOrderCartInfoVos = new ArrayList<>();
        for (YqmStoreOrderCartInfo yqmStoreOrderCartInfo : yqmStoreOrderCartInfos) {

            YqmStoreOrderCartInfoVo yqmStoreOrderCartInfoVo = new YqmStoreOrderCartInfoVo();
            yqmStoreOrderCartInfoVo.setId(yqmStoreOrderCartInfo.getId());
            yqmStoreOrderCartInfoVo.setOid(yqmStoreOrderCartInfo.getOid());
            yqmStoreOrderCartInfoVo.setCartId(yqmStoreOrderCartInfo.getCartId());
            yqmStoreOrderCartInfoVo.setProductId(yqmStoreOrderCartInfo.getProductId());
            YqmStoreCartQueryVo cartInfo = JSONObject.parseObject(yqmStoreOrderCartInfo.getCartInfo(), YqmStoreCartQueryVo.class);
            yqmStoreOrderCartInfoVo.setCartInfo(cartInfo);
            yqmStoreOrderCartInfoVo.setUnique(yqmStoreOrderCartInfo.getUnique());
            yqmStoreOrderCartInfoVo.setIsAfterSales(yqmStoreOrderCartInfo.getIsAfterSales() == null ? 0 : yqmStoreOrderCartInfo.getIsAfterSales());

            //商品优惠前总金额
            BigDecimal totalAmountOfGoods = NumberUtil.mul(cartInfo.getTruePrice(), cartInfo.getCartNum());
            //商品优惠总金额
            BigDecimal commodityDiscountAmount = NumberUtil.mul(NumberUtil.div(totalAmountOfGoods, NumberUtil.sub(yqmStoreOrder.getTotalPrice(), yqmStoreOrder.getPayPostage())), yqmStoreOrder.getCouponPrice());

            yqmStoreOrderCartInfoVo.setRefundablePrice(NumberUtil.sub(totalAmountOfGoods, commodityDiscountAmount));

            yqmStoreOrderCartInfoVo.setReasons(storeAfterSales.getReasons());
            yqmStoreOrderCartInfoVos.add(yqmStoreOrderCartInfoVo);
        }

        return yqmStoreOrderCartInfoVos;

    }

    @Override
    public Map<String, Object> salesList(Long uid, Integer status, Integer page, String orderCode, Integer limit) {
        Page<StoreAfterSales> storeAfterSalesPage = new Page<>(page, limit);
        List<Integer> integers = new ArrayList<>();
        if (status == 1) {
            integers.add(0);
            integers.add(1);
            integers.add(2);
        }
        if (status == 2) {
            integers.add(3);
        }
        baseMapper.selectPage(storeAfterSalesPage, Wrappers.<StoreAfterSales>lambdaQuery()
                .eq(uid != null, StoreAfterSales::getUserId, uid).in(status.equals(AfterSalesStatusEnum.STATUS_1.getValue()), StoreAfterSales::getState, integers)
                .in(!status.equals(AfterSalesStatusEnum.STATUS_0.getValue()), StoreAfterSales::getState, integers)
                .eq(StringUtils.isNotBlank(orderCode), StoreAfterSales::getOrderCode, orderCode)
                .orderByDesc(StoreAfterSales::getCreateTime)
                .eq(StoreAfterSales::getIsDel, ShopCommonEnum.DELETE_0.getValue()));
        List<StoreAfterSalesVo> storeAfterSalesVos = generator.convert(storeAfterSalesPage.getRecords(), StoreAfterSalesVo.class);
        Map<String, Object> map = new HashMap<>();
        if (uid == null) {
            map.put("content", storeAfterSalesVos.stream().map(this::handleSales).collect(Collectors.toList()));
            map.put("totalElements", storeAfterSalesPage.getTotal());
        } else {
            map.put("list", storeAfterSalesVos.stream().map(this::handleSales).collect(Collectors.toList()));
            map.put("total", storeAfterSalesPage.getTotal());
            map.put("totalPage", storeAfterSalesPage.getPages());
        }
        return map;
    }

    @Override
    public StoreAfterSalesVo getStoreInfoByOrderCodeAndAfterIdAndUid(String key, Long id, Long uid) {
        StoreAfterSales storeAfterSales = baseMapper.selectOne(Wrappers.<StoreAfterSales>lambdaQuery().eq(id != null, StoreAfterSales::getId, id).eq(StoreAfterSales::getUserId, uid).eq(StoreAfterSales::getOrderCode, key));
        StoreAfterSalesVo salesVo = generator.convert(storeAfterSales, StoreAfterSalesVo.class);
//        salesVo.setCloseAfterSaleTime(DateUtil.tomorrow().toTimestamp());
        return salesVo;
    }

    @Override
    public List<StoreAfterSalesVo> getStoreInfoByOrderCodeAndUid(String key, Long uid) {
        List<StoreAfterSales> storeAfterSales = baseMapper.selectList(Wrappers.<StoreAfterSales>lambdaQuery().eq(StoreAfterSales::getUserId, uid).eq(StoreAfterSales::getOrderCode, key));
        return generator.convert(storeAfterSales, StoreAfterSalesVo.class);
    }

    /**
     * 处理售后订单返回的状态
     *
     * @param storeAfterSalesVo /
     * @return StoreAfterSalesVo /
     */
    @Override
    public StoreAfterSalesVo handleSales(StoreAfterSalesVo storeAfterSalesVo) {
        List<StoreAfterSalesItem> storeAfterSalesItems = storeAfterSalesItemMapper.selectList(Wrappers.<StoreAfterSalesItem>lambdaQuery().eq(StoreAfterSalesItem::getStoreAfterSalesId, storeAfterSalesVo.getId()));
        List<YqmStoreCartQueryVo> cartInfo = storeAfterSalesItems.stream()
                .map(cart -> JSON.parseObject(cart.getCartInfo(), YqmStoreCartQueryVo.class))
                .collect(Collectors.toList());
        storeAfterSalesVo.setCartInfo(cartInfo);
        List<StoreAfterSalesStatus> storeAfterSalesStatuses = storeAfterSalesStatusMapper.selectList(Wrappers.<StoreAfterSalesStatus>lambdaQuery().eq(StoreAfterSalesStatus::getStoreAfterSalesId, storeAfterSalesVo.getId()));

        storeAfterSalesVo.setCompleteTime(storeAfterSalesStatuses.stream().filter(item -> item.getChangeType() == 3).findFirst().orElse(new StoreAfterSalesStatus()).getChangeTime());
        storeAfterSalesVo.setDeliveryTime(storeAfterSalesStatuses.stream().filter(item -> item.getChangeType() == 2).findFirst().orElse(new StoreAfterSalesStatus()).getChangeTime());
        storeAfterSalesVo.setAuditFailedTime(storeAfterSalesStatuses.stream().filter(item -> item.getChangeType() == 4).findFirst().orElse(new StoreAfterSalesStatus()).getChangeTime());
        storeAfterSalesVo.setReviewTime(storeAfterSalesStatuses.stream().filter(item -> item.getChangeType() == 1).findFirst().orElse(new StoreAfterSalesStatus()).getChangeTime());
        storeAfterSalesVo.setRevocationTime(storeAfterSalesStatuses.stream().filter(item -> item.getChangeType() == 5).findFirst().orElse(new StoreAfterSalesStatus()).getChangeTime());
        return storeAfterSalesVo;
    }

    @Override
    public Boolean revoke(String key, Long uid, Long id) {
        StoreAfterSales storeAfterSales = baseMapper.selectOne(Wrappers.<StoreAfterSales>lambdaQuery().eq(StoreAfterSales::getUserId, uid).eq(StoreAfterSales::getId, id).eq(StoreAfterSales::getOrderCode, key));
        if (storeAfterSales == null) {
            throw new YqmShopException("未查询到售后订单信息");
        }
        if (storeAfterSales.getState().equals(AfterSalesStatusEnum.STATUS_2.getValue()) || storeAfterSales.getState().equals(AfterSalesStatusEnum.STATUS_3.getValue())) {
            throw new YqmShopException("订单不能撤销");
        }
        storeAfterSales.setSalesState(1);

        YqmStoreOrder yqmStoreOrder = storeOrderMapper.selectOne(Wrappers.<YqmStoreOrder>lambdaQuery().eq(YqmStoreOrder::getOrderId, key));
        yqmStoreOrder.setStatus(OrderInfoEnum.STATUS_0.getValue());
        yqmStoreOrder.setRefundStatus(OrderInfoEnum.STATUS_0.getValue());
        storeOrderMapper.updateById(yqmStoreOrder);

        List<YqmStoreOrderCartInfo> yqmStoreOrderCartInfos = storeOrderCartInfoMapper.selectList(Wrappers.<YqmStoreOrderCartInfo>lambdaQuery().eq(YqmStoreOrderCartInfo::getOid, yqmStoreOrder.getId()));
        for (YqmStoreOrderCartInfo yqmStoreOrderCartInfo : yqmStoreOrderCartInfos) {
            yqmStoreOrderCartInfo.setIsAfterSales(1);
            storeOrderCartInfoMapper.updateById(yqmStoreOrderCartInfo);
        }

        //操作记录
        StoreAfterSalesStatus storeAfterSalesStatus = new StoreAfterSalesStatus();
        storeAfterSalesStatus.setStoreAfterSalesId(storeAfterSales.getId());
        storeAfterSalesStatus.setChangeType(5);
        storeAfterSalesStatus.setChangeMessage("售后订单生成");
        storeAfterSalesStatus.setChangeTime(Timestamp.valueOf(LocalDateTime.now()));
        storeAfterSalesStatus.setOperator("用户操作");
        storeAfterSalesStatusMapper.insert(storeAfterSalesStatus);

        return baseMapper.updateById(storeAfterSales) > 0;
    }

    @Override
    public Boolean addLogisticsInformation(String code, String name, String postalCode, String orderCode) {
        StoreAfterSales storeAfterSales = baseMapper.selectOne(Wrappers.<StoreAfterSales>lambdaQuery().eq(StoreAfterSales::getOrderCode, orderCode));
        if (!storeAfterSales.getState().equals(AfterSalesStatusEnum.STATUS_1.getValue())) {
            throw new YqmShopException("当前状态不能添加物流信息!");
        }
        storeAfterSales.setShipperCode(code);
        storeAfterSales.setDeliverySn(postalCode);
        storeAfterSales.setDeliveryName(name);
        storeAfterSales.setState(AfterSalesStatusEnum.STATUS_2.getValue());

        //操作记录
        StoreAfterSalesStatus storeAfterSalesStatus = new StoreAfterSalesStatus();
        storeAfterSalesStatus.setStoreAfterSalesId(storeAfterSales.getId());
        storeAfterSalesStatus.setChangeType(2);
        storeAfterSalesStatus.setChangeMessage("售后订单生成");
        storeAfterSalesStatus.setChangeTime(Timestamp.valueOf(LocalDateTime.now()));
        storeAfterSalesStatus.setOperator("用户操作");
        storeAfterSalesStatusMapper.insert(storeAfterSalesStatus);

        return baseMapper.updateById(storeAfterSales) > 0;
    }

    @Override
    public Boolean deleteAfterSalesOrder(String orderCode, Long id) {
        StoreAfterSales storeAfterSales = baseMapper.selectOne(Wrappers.<StoreAfterSales>lambdaQuery().eq(StoreAfterSales::getId, id).eq(StoreAfterSales::getOrderCode, orderCode));
        return baseMapper.deleteById(storeAfterSales.getId()) > 0;
    }

    @Override
    public Object salesCheck(Long salesId, String orderCode, Integer approvalStatus, String consignee, String phoneNumber, String address) {
        StoreAfterSales storeAfterSales = baseMapper.selectOne(Wrappers.<StoreAfterSales>lambdaQuery().eq(StoreAfterSales::getOrderCode, orderCode).eq(StoreAfterSales::getId, salesId));
        if (approvalStatus == 0) {
            storeAfterSales.setState(AfterSalesStatusEnum.STATUS_1.getValue());
            if (storeAfterSales.getServiceType() == 1) {
                if (StringUtils.isEmpty(consignee) || StringUtils.isEmpty(phoneNumber) || StringUtils.isEmpty(address)) {
                    throw new ErrorRequestException("请输入收货人信息");
                }
                storeAfterSales.setConsignee(consignee);
                storeAfterSales.setPhoneNumber(phoneNumber);
                storeAfterSales.setAddress(address);
            } else {
                this.makeMoney(storeAfterSales.getId(),storeAfterSales.getOrderCode());
            }
            //操作记录
            StoreAfterSalesStatus storeAfterSalesStatus = new StoreAfterSalesStatus();
            storeAfterSalesStatus.setStoreAfterSalesId(storeAfterSales.getId());
            storeAfterSalesStatus.setChangeType(1);
            storeAfterSalesStatus.setChangeMessage("平台审核成功");
            storeAfterSalesStatus.setChangeTime(Timestamp.valueOf(LocalDateTime.now()));
            storeAfterSalesStatus.setOperator(SecurityUtils.getUsername());
            storeAfterSalesStatusMapper.insert(storeAfterSalesStatus);

        } else {
            storeAfterSales.setState(AfterSalesStatusEnum.STATUS_1.getValue());
            storeAfterSales.setSalesState(2);
            //操作记录
            StoreAfterSalesStatus storeAfterSalesStatus = new StoreAfterSalesStatus();
            storeAfterSalesStatus.setStoreAfterSalesId(storeAfterSales.getId());
            storeAfterSalesStatus.setChangeType(4);
            storeAfterSalesStatus.setChangeMessage("平台审核失败");
            storeAfterSalesStatus.setChangeTime(Timestamp.valueOf(LocalDateTime.now()));
            storeAfterSalesStatus.setOperator(SecurityUtils.getUsername());
            storeAfterSalesStatusMapper.insert(storeAfterSalesStatus);
        }
        return baseMapper.updateById(storeAfterSales) > 0;
    }

    @Override
    public StoreAfterSales makeMoney(Long salesId, String orderCode) {
        StoreAfterSales storeAfterSales = baseMapper.selectOne(Wrappers.<StoreAfterSales>lambdaQuery().eq(StoreAfterSales::getOrderCode, orderCode).eq(StoreAfterSales::getId, salesId));
        storeAfterSales.setState(AfterSalesStatusEnum.STATUS_3.getValue());
        baseMapper.updateById(storeAfterSales);
        //操作记录
        StoreAfterSalesStatus storeAfterSalesStatus = new StoreAfterSalesStatus();
        storeAfterSalesStatus.setStoreAfterSalesId(storeAfterSales.getId());
        storeAfterSalesStatus.setChangeType(3);
        storeAfterSalesStatus.setChangeMessage("平台打款成功");
        storeAfterSalesStatus.setChangeTime(Timestamp.valueOf(LocalDateTime.now()));
        storeAfterSalesStatus.setOperator(SecurityUtils.getUsername());
        storeAfterSalesStatusMapper.insert(storeAfterSalesStatus);

        YqmStoreOrder yqmStoreOrder = storeOrderMapper.selectOne(Wrappers.<YqmStoreOrder>lambdaQuery().eq(YqmStoreOrder::getOrderId, storeAfterSales.getOrderCode()));
        yqmStoreOrder.setStatus(-2);
        storeOrderMapper.updateById(yqmStoreOrder);
        return storeAfterSales;
    }

    /**
     * 检查订单是否符合售后订单
     *
     * @param yqmStoreOrder 订单
     */
    private void checkOrder(YqmStoreOrder yqmStoreOrder) {
        if (yqmStoreOrder == null) {
            throw new YqmShopException("未查询到订单信息");
        }

        if (!yqmStoreOrder.getPaid().equals(OrderInfoEnum.PAY_STATUS_1.getValue())
                || !yqmStoreOrder.getRefundStatus().equals(OrderInfoEnum.REFUND_STATUS_0.getValue())
                || yqmStoreOrder.getStatus() < OrderInfoEnum.STATUS_0.getValue()) {
            throw new YqmShopException("订单状态不能售后");
        }
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreAfterSalesQueryCriteria criteria, Pageable pageable) {
        Page<StoreAfterSales> storeAfterSalesPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());

        LambdaQueryWrapper<StoreAfterSales> eq = Wrappers.<StoreAfterSales>lambdaQuery()
                .in(ObjectUtils.isNotEmpty(criteria.getState()), StoreAfterSales::getState, criteria.getState())
                .eq(StringUtils.isNotBlank(criteria.getOrderCode()), StoreAfterSales::getOrderCode, criteria.getOrderCode())
                .eq(ObjectUtils.isNotEmpty(criteria.getSalesState()), StoreAfterSales::getSalesState, criteria.getSalesState())
                .eq(ObjectUtils.isNotEmpty(criteria.getServiceType()), StoreAfterSales::getServiceType, criteria.getServiceType())
                .orderByDesc(StoreAfterSales::getCreateTime)
                .eq(StoreAfterSales::getIsDel, ShopCommonEnum.DELETE_0.getValue());
        if (CollectionUtils.isNotEmpty(criteria.getTime())) {
            eq.ge(StoreAfterSales::getCreateTime, criteria.getTime().get(0))
                    .le(StoreAfterSales::getCreateTime, criteria.getTime().get(1));
        }

        baseMapper.selectPage(storeAfterSalesPage, eq);
        List<StoreAfterSalesVo> storeAfterSalesVos = generator.convert(storeAfterSalesPage.getRecords(), StoreAfterSalesVo.class);
        Map<String, Object> map = new HashMap<>();
        map.put("content", storeAfterSalesVos.stream().map(this::handleSales).collect(Collectors.toList()));
        map.put("totalElements", storeAfterSalesPage.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<StoreAfterSales> queryAll(YqmStoreAfterSalesQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(StoreAfterSales.class, criteria));
    }


    @Override
    public void download(List<YqmStoreAfterSalesDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreAfterSalesDto yqmStoreAfterSales : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("订单号", yqmStoreAfterSales.getOrderCode());
            map.put("退款金额", yqmStoreAfterSales.getRefundAmount());
            map.put("服务类型0仅退款1退货退款", yqmStoreAfterSales.getServiceType());
            map.put("申请原因", yqmStoreAfterSales.getReasons());
            map.put("说明", yqmStoreAfterSales.getExplains());
            map.put("说明图片->多个用逗号分割", yqmStoreAfterSales.getExplainImg());
            map.put("物流公司编码", yqmStoreAfterSales.getShipperCode());
            map.put("物流单号", yqmStoreAfterSales.getDeliverySn());
            map.put("物流名称", yqmStoreAfterSales.getDeliveryName());
            map.put("状态 0已提交等待平台审核 1平台已审核 等待用户发货/退款 2 用户已发货 3退款成功", yqmStoreAfterSales.getState());
            map.put("售后状态-0正常1用户取消2商家拒绝", yqmStoreAfterSales.getSalesState());
            map.put("添加时间", yqmStoreAfterSales.getCreateTime());
            map.put("逻辑删除", yqmStoreAfterSales.getIsDel());
            map.put("用户id", yqmStoreAfterSales.getUserId());
            map.put("商家收货人", yqmStoreAfterSales.getConsignee());
            map.put("商家手机号", yqmStoreAfterSales.getPhoneNumber());
            map.put("商家地址", yqmStoreAfterSales.getAddress());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
