/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service;


import com.yqm.common.service.BaseService;
import com.yqm.modules.user.domain.YqmUserAddress;
import com.yqm.modules.user.param.AddressParam;
import com.yqm.modules.user.vo.YqmUserAddressQueryVo;

import java.util.List;

/**
 * <p>
 * 用户地址表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2019-10-28
 */
public interface YqmUserAddressService extends BaseService<YqmUserAddress> {

    /**
     * 设置默认地址
     * @param uid uid
     * @param addressId 地址id
     */
    void setDefault(Long uid,Long addressId);

    /**
     * 添加或者修改地址
     * @param uid uid
     * @param param AddressParam
     */
    Long addAndEdit(Long uid, AddressParam param);

    /**
     * 地址详情
     * @param id 地址id
     * @return YqmUserAddressQueryVo
     */
    YqmUserAddressQueryVo getDetail(Long id);

    /**
     * 获取用户地址
     * @param uid uid
     * @param page page
     * @param limit limit
     * @return List
     */
    List<YqmUserAddressQueryVo> getList(Long uid,int page,int limit);

    /**
     * 获取默认地址
     * @param uid uid
     * @return YqmUserAddress
     */
    YqmUserAddress getUserDefaultAddress(Long uid);

}
