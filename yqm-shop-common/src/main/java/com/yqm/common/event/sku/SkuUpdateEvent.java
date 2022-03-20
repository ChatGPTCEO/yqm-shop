package com.yqm.common.event.sku;

import com.yqm.common.dto.YqmStoreSkuDTO;
import org.springframework.context.ApplicationEvent;

/**
 * Sku修改
 *
 * @Author: weiximei
 * @Date: 2021/10/16 23:38
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class SkuUpdateEvent extends ApplicationEvent {

    private YqmStoreSkuDTO storeAttributeDTO;

    public SkuUpdateEvent(YqmStoreSkuDTO source) {
        super(source);
        this.storeAttributeDTO = source;
    }

    public YqmStoreSkuDTO getStoreAttributeDTO() {
        return storeAttributeDTO;
    }
}
