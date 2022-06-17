package com.yqm.modules.order.vo;

import com.yqm.modules.activity.vo.StoreCouponUserVo;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.order.service.dto.PriceGroupDto;
import com.yqm.modules.product.vo.YqmSystemStoreQueryVo;
import com.yqm.modules.user.domain.YqmUserAddress;
import com.yqm.modules.user.vo.YqmUserQueryVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ConfirmOrderVo
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/10/27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfirmOrderVo implements Serializable {
    //地址信息
    private YqmUserAddress addressInfo;

    //砍价id
    private Integer bargainId;

    private List<YqmStoreCartQueryVo> cartInfo;

    private Integer combinationId;

    //优惠券减
    private Boolean deduction;

    private Boolean enableIntegral;

    private Double enableIntegralNum;

    //积分抵扣
    private Integer integralRatio;

    private String orderKey;

    private PriceGroupDto priceGroup;

    private Integer seckillId;

    //店铺自提
    private Integer storeSelfMention;

    //店铺信息
    private YqmSystemStoreQueryVo systemStore;


    private StoreCouponUserVo usableCoupon;

    private YqmUserQueryVo userInfo;



}
