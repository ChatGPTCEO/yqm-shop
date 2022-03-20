package com.yqm.common.event.sku;

import com.yqm.common.dto.YqmStoreSkuDTO;
import org.springframework.context.ApplicationEvent;

/**
 * Sku删除
 *
 * @Author: weiximei
 * @Date: 2021/10/16 23:38
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class SkuDeleteEvent extends ApplicationEvent {

    private YqmStoreSkuDTO storeSkuDTO;

    public SkuDeleteEvent(YqmStoreSkuDTO source) {
        super(source);
        this.storeSkuDTO = source;
    }

    public YqmStoreSkuDTO getStoreSkuDTO() {
        return storeSkuDTO;
    }
}
