/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;


import cn.hutool.core.util.StrUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.user.domain.YqmUserAddress;
import com.yqm.modules.user.param.AddressParam;
import com.yqm.modules.user.service.YqmUserAddressService;
import com.yqm.modules.user.service.mapper.YqmUserAddressMapper;
import com.yqm.modules.user.vo.YqmUserAddressQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * 用户地址表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-10-28
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YqmUserAddressServiceImpl extends BaseServiceImpl<YqmUserAddressMapper, YqmUserAddress> implements YqmUserAddressService {

    private final YqmUserAddressMapper yqmUserAddressMapper;
    private final IGenerator generator;

    /**
     * 设置默认地址
     * @param uid uid
     * @param addressId 地址id
     */
    @Override
    public void setDefault(Long uid,Long addressId){
        YqmUserAddress address = new YqmUserAddress();
        address.setIsDefault(ShopCommonEnum.DEFAULT_0.getValue());
        yqmUserAddressMapper.update(address,
                new LambdaQueryWrapper<YqmUserAddress>().eq(YqmUserAddress::getUid,uid));

        YqmUserAddress userAddress = new YqmUserAddress();
        userAddress.setIsDefault(ShopCommonEnum.DEFAULT_1.getValue());
        userAddress.setId(addressId);
        yqmUserAddressMapper.updateById(userAddress);
    }


    /**
     * 添加或者修改地址
     * @param uid uid
     * @param param AddressParam
     */
    @Override
    public Long addAndEdit(Long uid, AddressParam param){
        YqmUserAddress userAddress = YqmUserAddress.builder()
                .city(param.getAddress().getCity())
                .cityId(param.getAddress().getCityId())
                .district(param.getAddress().getDistrict())
                .province(param.getAddress().getProvince())
                .detail(param.getDetail())
                .uid(uid)
                .phone(param.getPhone())
                .postCode(param.getPost_code())
                .realName(param.getReal_name())
                .build();
        if("true".equals(param.getIs_default())){
            userAddress.setIsDefault(ShopCommonEnum.DEFAULT_1.getValue());
            //新增地址如果是默认，把之前的状态改掉
            YqmUserAddress address = new YqmUserAddress();
            address.setIsDefault(ShopCommonEnum.DEFAULT_0.getValue());
            baseMapper.update(address,new LambdaQueryWrapper<YqmUserAddress>().eq(YqmUserAddress::getUid,uid));
        }else{
            userAddress.setIsDefault(ShopCommonEnum.DEFAULT_0.getValue());
        }
        if(StrUtil.isBlank(param.getId())){
            this.save(userAddress);
        }else{
            userAddress.setId(Long.valueOf(param.getId()));
            this.updateById(userAddress);
        }

        return userAddress.getId();
    }

    /**
     * 地址详情
     * @param id 地址id
     * @return YqmUserAddressQueryVo
     */
    @Override
    public YqmUserAddressQueryVo getDetail(Long id){
        return generator.convert(this.getById(id),YqmUserAddressQueryVo.class);
    }


    /**
     * 获取用户地址
     * @param uid uid
     * @param page page
     * @param limit limit
     * @return List
     */
    @Override
    public List<YqmUserAddressQueryVo> getList(Long uid,int page,int limit){
        Page<YqmUserAddress> pageModel = new Page<>(page, limit);
        IPage<YqmUserAddress> pageList = this.lambdaQuery().eq(YqmUserAddress::getUid,uid).page(pageModel);
        return generator.convert(pageList.getRecords(),YqmUserAddressQueryVo.class);
    }

    /**
     * 获取默认地址
     * @param uid uid
     * @return YqmUserAddress
     */
    @Override
    public YqmUserAddress getUserDefaultAddress(Long uid) {
        LambdaQueryWrapper<YqmUserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmUserAddress::getIsDefault,1).
                eq(YqmUserAddress::getUid,uid)
                .last("limit 1");
        return getOne(wrapper);
    }



}
