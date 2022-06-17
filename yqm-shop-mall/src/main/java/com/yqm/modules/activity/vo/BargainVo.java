package com.yqm.modules.activity.vo;


import com.yqm.modules.user.vo.YqmUserQueryVo;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BargainVo
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/12/21
 **/
@Data
@Builder
public class BargainVo implements Serializable {
    private YqmStoreBargainQueryVo bargain;
    private YqmUserQueryVo userInfo;
    private Integer bargainSumCount;//砍价支付成功订单数量
}
