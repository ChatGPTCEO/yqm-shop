/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.OrderInfoEnum;
import com.yqm.modules.activity.domain.YqmStoreBargain;
import com.yqm.modules.activity.domain.YqmStoreBargainUser;
import com.yqm.modules.activity.domain.YqmStoreBargainUserHelp;
import com.yqm.modules.activity.service.YqmStoreBargainService;
import com.yqm.modules.activity.service.YqmStoreBargainUserHelpService;
import com.yqm.modules.activity.service.YqmStoreBargainUserService;
import com.yqm.modules.activity.service.mapper.YqmStoreBargainUserMapper;
import com.yqm.modules.activity.vo.YqmStoreBargainUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 用户参与砍价表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-21
 */
@Slf4j
@Service
public class YqmStoreBargainUserServiceImpl extends BaseServiceImpl<YqmStoreBargainUserMapper, YqmStoreBargainUser> implements YqmStoreBargainUserService {


    @Autowired
    private IGenerator generator;

    @Autowired
    private YqmStoreBargainUserMapper yqmStoreBargainUserMapper;

    @Autowired
    private YqmStoreBargainService storeBargainService;
    @Autowired
    private YqmStoreBargainUserHelpService storeBargainUserHelpService;


    /**
     * 修改用户砍价状态
     * @param bargainId 砍价产品id
     * @param uid 用户id
     */
    @Override
    public void setBargainUserStatus(Long bargainId, Long uid) {
        YqmStoreBargainUser storeBargainUser = getBargainUserInfo(bargainId.longValue(),uid);
        if(ObjectUtil.isNull(storeBargainUser)) {
            return;
        }

        if(storeBargainUser.getStatus() != 1) {
            return;
        }
        double price = NumberUtil.sub(NumberUtil.sub(storeBargainUser.getBargainPrice(),
                storeBargainUser.getBargainPriceMin()),storeBargainUser.getPrice()).doubleValue();
        if(price > 0) {
            return;
        }

        storeBargainUser.setStatus(3);

        yqmStoreBargainUserMapper.updateById(storeBargainUser);
    }

    /**
     * 砍价取消
     * @param bargainId 砍价商品id
     * @param uid uid
     */
    @Override
    public void bargainCancel(Long bargainId, Long uid) {
        YqmStoreBargainUser storeBargainUser = this.getBargainUserInfo(bargainId,uid);
        if(ObjectUtil.isNull(storeBargainUser)) {
            throw new YqmShopException("数据不存在");
        }
        if(!OrderInfoEnum.BARGAIN_STATUS_1.getValue().equals(storeBargainUser.getStatus())){
            throw new YqmShopException("状态错误");
        }
        yqmStoreBargainUserMapper.deleteById(storeBargainUser.getId());
    }

    /**
     * 获取用户的砍价产品
     * @param bargainUserUid 用户id
     * @param page page
     * @param limit limit
     * @return List
     */
    @Override
    public List<YqmStoreBargainUserQueryVo> bargainUserList(Long bargainUserUid, int page, int limit) {
        Page<YqmStoreBargainUser> pageModel = new Page<>(page, limit);
        return yqmStoreBargainUserMapper.getBargainUserList(bargainUserUid,pageModel);
    }

    /**
     * 判断用户是否还可以砍价
     * @param bargainId 砍价产品id
     * @param bargainUserUid 开启砍价用户id
     * @param uid  当前用户id
     * @return false=NO true=YES
     */
    @Override
    public  boolean isBargainUserHelp(Long bargainId, Long bargainUserUid, Long uid) {
        YqmStoreBargainUser storeBargainUser = this.getBargainUserInfo(bargainId, bargainUserUid);
        YqmStoreBargain storeBargain = storeBargainService
                .getById(bargainId);
        if(ObjectUtil.isNull(storeBargainUser) || ObjectUtil.isNull(storeBargain)){
            return false;
        }
        int count = storeBargainUserHelpService.lambdaQuery()
                .eq(YqmStoreBargainUserHelp::getBargainId,bargainId)
                .eq(YqmStoreBargainUserHelp::getBargainUserId,storeBargainUser.getId())
                .eq(YqmStoreBargainUserHelp::getUid,uid)
                .count();
        if(count == 0) {
            return true;
        }
        return false;
    }

    /**
     * 添加砍价记录
     * @param bargainId 砍价商品id
     * @param uid 用户id
     */
    @Override
    public void setBargain(Long bargainId, Long uid) {
        YqmStoreBargainUser storeBargainUser = this.getBargainUserInfo(bargainId,uid);
        if(storeBargainUser != null) {
            throw new YqmShopException("你已经参与了");
        }
        YqmStoreBargain storeBargain = storeBargainService.getById(bargainId);
        if(storeBargain == null) {
            throw new YqmShopException("砍价商品不存在");
        }
        YqmStoreBargainUser yqmStoreBargainUser = YqmStoreBargainUser
                .builder()
                .bargainId(bargainId)
                .uid(uid)
                .bargainPrice(storeBargain.getPrice())
                .bargainPriceMin(storeBargain.getMinPrice())
                .price(BigDecimal.ZERO)
                .status(OrderInfoEnum.BARGAIN_STATUS_1.getValue())
                .build();
        yqmStoreBargainUserMapper.insert(yqmStoreBargainUser);
    }

//    /**
//     * 获取用户可以砍掉的价格
//     * @param id
//     * @return
//     */
//    @Override
//    public double getBargainUserDiffPrice(int id) {
//        YqmStoreBargainUser storeBargainUserQueryVo = this.getById(id);
//        return NumberUtil.sub(storeBargainUserQueryVo.getBargainPrice()
//                ,storeBargainUserQueryVo.getBargainPriceMin()).doubleValue();
//    }



    /**
     * 获取某个用户参与砍价信息
     * @param bargainId 砍价id
     * @param uid 用户id
     * @return  YqmStoreBargainUser
     */
    @Override
    public YqmStoreBargainUser getBargainUserInfo(Long bargainId, Long uid) {
       LambdaQueryWrapper<YqmStoreBargainUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreBargainUser::getBargainId,bargainId)
                .eq(YqmStoreBargainUser::getUid,uid)
                .last("limit 1");
        return yqmStoreBargainUserMapper.selectOne(wrapper);
    }

    /**
     * 获取参与砍价的用户数量
     * @param bargainId 砍价id
     * @param status  状态  OrderInfoEnum 1 进行中  2 结束失败  3结束成功
     * @return int
     */
    @Override
    public int getBargainUserCount(Long bargainId, Integer status) {
        return this.lambdaQuery().eq(YqmStoreBargainUser::getBargainId,bargainId)
                .eq(YqmStoreBargainUser::getStatus,status).count();
    }


//
//    /**
//     * 获取参与砍价的用户列表
//     * @param bargainId 砍价id
//     * @param status  状态  1 进行中  2 结束失败  3结束成功
//     * @return
//     */
//    @Override
//    public List<YqmStoreBargainUserQueryVo> getBargainUserList(int bargainId, int status) {
//       LambdaQueryWrapper<YqmStoreBargainUser> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq("bargain_id",bargainId).eq("status",status);
//        return generator.convert(yqmStoreBargainUserMapper.selectList(wrapper),
//                YqmStoreBargainUserQueryVo.class);
//    }




}
