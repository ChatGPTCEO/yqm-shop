package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author weiximei
 * @since 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态;delete 删除 success 有效 failure 失效
     */
    private String status;

    /**
     * 用户编号
     */
    private String userId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 应付金额
     */
    private String amountPayable;

    /**
     * 订单总额
     */
    private String amount;

    /**
     * 支付方式;alipay 支付宝 wxpay 微信支付
     */
    private String payType;

    /**
     * 订单来源;app APP miniapp 小程序
     */
    private String orderSource;

    /**
     * 订单状态;0 待付款 1 已付款 2 待发货 3 已完成 4 已关闭
     */
    private Integer orderStatus;

    /**
     * 收货地址
     */
    private String shippingAddress;

    /**
     * 收货电话
     */
    private String shippingPhone;

    /**
     * 收货人姓名
     */
    private String shippingName;

    /**
     * 备注
     */
    private String note;

    /**
     * 快递单号
     */
    private String expressSingle;

    /**
     * 邮编
     */
    private String zipCode;

    /**
     * 物流轨迹
     */
    private String expressLogistics;

    /**
     * 自动收货天数
     */
    private Integer automaticDay;


    /**
     * 自动收货天数
     */
    private LocalDateTime automaticDate;

    /**
     * 订单类型;ordinary 普通
     */
    private String orderType;

    /**
     * 配送方式;express 快递
     */
    private String distributionMode;

    /**
     * 运费
     */
    private String freight;

    /**
     * 省id
     */
    private String provinceId;

    /**
     * 省
     */
    private String provinceName;


    /**
     * 市id
     */
    private String cityId;

    /**
     * 市
     */
    private String cityName;

    /**
     * 区id
     */
    private String areaId;

    /**
     * 区
     */
    private String areaName;

    /**
     * 折扣金额
     */
    private String discountAmount;

}