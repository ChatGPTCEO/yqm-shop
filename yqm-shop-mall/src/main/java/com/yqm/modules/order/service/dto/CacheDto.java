package com.yqm.modules.order.service.dto;

import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CacheDto
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
public class CacheDto implements Serializable {
    private List<YqmStoreCartQueryVo> cartInfo;
    private PriceGroupDto priceGroup;
    private OtherDto other;
}
