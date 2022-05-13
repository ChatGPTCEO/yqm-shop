package com.yqm.common.event.refundGoods;

import com.yqm.common.dto.YqmRefundGoodsDTO;
import org.springframework.context.ApplicationEvent;

public class ConfirmRefundGoodsEvent  extends ApplicationEvent {

    private YqmRefundGoodsDTO refundGoodsDTO;

    public ConfirmRefundGoodsEvent(YqmRefundGoodsDTO refundGoodsDTO) {
        super(refundGoodsDTO);
        this.refundGoodsDTO = refundGoodsDTO;
    }

    public YqmRefundGoodsDTO getRefundGoodsDTO() {
        return refundGoodsDTO;
    }
}
