package com.yqm.common.event.refundGoods;

import com.yqm.common.dto.YqmRefundGoodsDTO;
import org.springframework.context.ApplicationEvent;

public class RefusedRefundGoodsEvent  extends ApplicationEvent {

    private YqmRefundGoodsDTO refundGoodsDTO;

    public RefusedRefundGoodsEvent(YqmRefundGoodsDTO source) {
        super(source);
        this.refundGoodsDTO = source;
    }

    public YqmRefundGoodsDTO getRefundGoodsDTO() {
        return refundGoodsDTO;
    }
}
