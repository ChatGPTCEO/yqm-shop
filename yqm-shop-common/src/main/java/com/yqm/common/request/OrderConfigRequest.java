package com.yqm.common.request;

import lombok.Data;

@Data
public class OrderConfigRequest extends BaseRequest{

    /**
     * 正常订单超时，未付款，订单自动关闭
     */
    private String orderOvertimeClose;

    /**
     * 发货超时，未收货，订单自动完成
     */
    private String orderDeliveryOvertimeCompleted;

    /**
     * 订单完成超时，自动结束交易，不能申请售后
     */
    private String orderCompletedOvertimeRefund;

    /**
     * 订单完成超时，自动五星好评
     */
    private String orderCompletedOvertimeEvaluation;

}
