package com.yqm.common.event.order;

import com.yqm.common.dto.YqmOrderDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 订单新增
 *
 * @Author: weiximei
 * @Date: 2021/10/16 23:38
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class OrderAddEvent  extends ApplicationEvent {

    private YqmOrderDTO orderDTO;


    public OrderAddEvent(YqmOrderDTO orderDTO) {
        super(orderDTO);
        this.orderDTO = orderDTO;
    }

    public YqmOrderDTO getOrderDTO() {
        return orderDTO;
    }
}
