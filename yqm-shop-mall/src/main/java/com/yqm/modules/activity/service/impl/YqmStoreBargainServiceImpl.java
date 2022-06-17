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
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.OrderInfoEnum;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.activity.domain.YqmStoreBargain;
import com.yqm.modules.activity.domain.YqmStoreBargainUser;
import com.yqm.modules.activity.domain.YqmStoreBargainUserHelp;
import com.yqm.modules.activity.service.YqmStoreBargainService;
import com.yqm.modules.activity.service.YqmStoreBargainUserHelpService;
import com.yqm.modules.activity.service.YqmStoreBargainUserService;
import com.yqm.modules.activity.service.dto.YqmStoreBargainDto;
import com.yqm.modules.activity.service.dto.YqmStoreBargainQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreBargainMapper;
import com.yqm.modules.activity.vo.BargainCountVo;
import com.yqm.modules.activity.vo.BargainVo;
import com.yqm.modules.activity.vo.TopCountVo;
import com.yqm.modules.activity.vo.YqmStoreBargainQueryVo;
import com.yqm.modules.order.domain.YqmStoreOrder;
import com.yqm.modules.order.service.YqmStoreOrderService;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.vo.YqmUserQueryVo;
import com.yqm.utils.FileUtil;
import com.yqm.utils.OrderUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
 * @author weiximei
 * @date 2020-05-13
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreBargainServiceImpl extends BaseServiceImpl<YqmStoreBargainMapper, YqmStoreBargain> implements YqmStoreBargainService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private YqmStoreBargainMapper yqmStoreBargainMapper;


    @Autowired
    private YqmStoreBargainUserService storeBargainUserService;
    @Autowired
    private YqmStoreOrderService storeOrderService;
    @Autowired
    private YqmStoreBargainUserHelpService storeBargainUserHelpService;



    /**
     * 退回库存销量
     * @param num 数量
     * @param bargainId 砍价产品id
     */
    @Override
    public void incStockDecSales(int num, Long bargainId) {
        yqmStoreBargainMapper.incStockDecSales(num,bargainId);
    }

    /**
     * 增加销量 减少库存
     * @param num 数量
     * @param bargainId 砍价id
     */
    @Override
    public void decStockIncSales(int num, Long bargainId) {
        int res = yqmStoreBargainMapper.decStockIncSales(num,bargainId);
        if(res == 0) {
            throw new YqmShopException("砍价产品库存不足");
        }
    }

//    @Override
//    public YqmStoreBargain getBargain(int bargainId) {
//        QueryWrapper<YqmStoreBargain> wrapper = new QueryWrapper<>();
//        int nowTime = OrderUtil.getSecondTimestampTwo();
//        wrapper.eq("id",bargainId).eq("is_del",0).eq("status",1)
//                .le("start_time",nowTime).ge("stop_time",nowTime);
//        return yqmStoreBargainMapper.selectOne(wrapper);
//    }



    /**
     * 开始帮助好友砍价
     * @param bargainId 砍价产品id
     * @param bargainUserUid 开启砍价用户id
     * @param uid 当前用户id
     */
    @Override
    public void doHelp(Long bargainId, Long bargainUserUid, Long uid) {
        //开始真正的砍价
        YqmStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,bargainUserUid);


        YqmStoreBargain storeBargain = this.getById(bargainId);
        //用户可以砍掉的金额 好友砍价之前获取可以砍价金额
        double coverPrice = NumberUtil.sub(storeBargainUser.getBargainPrice()
                ,storeBargainUser.getBargainPriceMin()).doubleValue();

        double random = 0d;
        if(coverPrice > 0 ){
            //用户剩余要砍掉的价格
            double surplusPrice = NumberUtil.sub(coverPrice,
                    storeBargainUser.getPrice()).doubleValue();
            if(surplusPrice == 0) {
                return;
            }


            //生成一个区间随机数
            random = OrderUtil.randomNumber(
                    storeBargain.getBargainMinPrice().doubleValue(),
                    storeBargain.getBargainMaxPrice().doubleValue());
            if(random > surplusPrice) {
                random = surplusPrice;
            }
        }


        //添加砍价帮助表
        YqmStoreBargainUserHelp storeBargainUserHelp = YqmStoreBargainUserHelp
                .builder()
                .uid(uid)
                .bargainId(bargainId)
                .bargainUserId(storeBargainUser.getId())
                .price(BigDecimal.valueOf(random))
                .build();
        storeBargainUserHelpService.save(storeBargainUserHelp);

        //累计砍掉的金额
        double totalPrice = NumberUtil.add(storeBargainUser.getPrice().doubleValue(),random);

        //更新砍价参与表
        YqmStoreBargainUser bargainUser = YqmStoreBargainUser
                .builder()
                .id(storeBargainUser.getId())
                .price(BigDecimal.valueOf(totalPrice))
                .build();

        storeBargainUserService.updateById(bargainUser);
    }

    /**
     * 顶部统计
     * @param bargainId 砍价商品id
     * @return TopCountVo
     */
    @Override
    public TopCountVo topCount(Long bargainId) {
        if(bargainId != null) {
            this.addBargainShare(bargainId);
        }
        return TopCountVo.builder()
                .lookCount(yqmStoreBargainMapper.lookCount())
                .shareCount(yqmStoreBargainMapper.shareCount())
                .userCount(storeBargainUserService.count())
                .build();
    }

    /**
     * 砍价 砍价帮总人数、剩余金额、进度条、已经砍掉的价格
     * @param bargainId 砍价商品id
     * @param uid 砍价用户id
     * @param myUid 当前用户id
     * @return BargainCountVo
     */
    @Override
    public BargainCountVo helpCount(Long bargainId, Long uid, Long myUid) {
        YqmStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,uid);
        // 是否帮别人砍,没砍是true，砍了false
        boolean userBargainStatus = true;
        if(storeBargainUser == null) {
            return BargainCountVo
                    .builder()
                    .count(0)
                    .alreadyPrice(0d)
                    .status(0)
                    .pricePercent(0)
                    .price(0d)
                    .userBargainStatus(userBargainStatus)
                    .build();
        }


        int helpCount = storeBargainUserHelpService.lambdaQuery()
                .eq(YqmStoreBargainUserHelp::getBargainUserId,storeBargainUser.getId())
                .eq(YqmStoreBargainUserHelp::getBargainId,bargainId)
                .eq(YqmStoreBargainUserHelp::getUid,myUid)
                .count();

        if(helpCount > 0) {
            userBargainStatus = false;
        }


        int count = storeBargainUserHelpService
                .getBargainUserHelpPeopleCount(bargainId,storeBargainUser.getId());
        //用户可以砍掉的价格
        double diffPrice = NumberUtil.sub(storeBargainUser.getBargainPrice()
                ,storeBargainUser.getBargainPriceMin()).doubleValue();
        //砍价进度条百分比
        int pricePercent = 0;
        if(diffPrice <= 0) {
            pricePercent = 100;
        }else{
            pricePercent = NumberUtil.round(NumberUtil.mul(NumberUtil.div(
                    storeBargainUser.getPrice(),diffPrice),100)
                    ,0).intValue();
        }



        //剩余的砍价金额
        double surplusPrice = NumberUtil.sub(diffPrice,storeBargainUser.getPrice()).doubleValue();

        return BargainCountVo
                .builder()
                .count(count)
                .alreadyPrice(storeBargainUser.getPrice().doubleValue())
                .status(storeBargainUser.getStatus())
                .pricePercent(pricePercent)
                .price(surplusPrice)
                .userBargainStatus(userBargainStatus)
                .build();
    }





    /**
     * 砍价详情
     * @param id 砍价id
     * @param yqmUser 用户
     * @return BargainVo
     */
    @Override
    public BargainVo getDetail(Long id, YqmUser yqmUser) {

        Date now = new Date();
        YqmStoreBargain storeBargain = this.lambdaQuery().eq(YqmStoreBargain::getId,id)
                .eq(YqmStoreBargain::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .le(YqmStoreBargain::getStartTime,now)
                .ge(YqmStoreBargain::getStopTime,now)
                .one();

        if(storeBargain == null) {
            throw new YqmShopException("砍价已结束");
        }

        this.addBargainLook(id);

        YqmStoreBargainQueryVo storeBargainQueryVo = generator.convert(storeBargain,
                YqmStoreBargainQueryVo.class);

        return  BargainVo
                .builder()
                .bargain(storeBargainQueryVo)
                .userInfo(generator.convert(yqmUser, YqmUserQueryVo.class))
                .bargainSumCount(this.getBargainPayCount(id))
                .build();
    }

    /**
     * 获取砍价商品列表
     * @param page page
     * @param limit limit
     * @return List
     */
    @Override
    public List<YqmStoreBargainQueryVo> getList(int page, int limit) {
        Page<YqmStoreBargain> pageModel = new Page<>(page, limit);
        LambdaQueryWrapper<YqmStoreBargain> wrapper = new LambdaQueryWrapper<>();
        Date nowTime = new Date();
        wrapper.eq(YqmStoreBargain::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .lt(YqmStoreBargain::getStartTime,nowTime)
                .gt(YqmStoreBargain::getStopTime,nowTime);

        List<YqmStoreBargainQueryVo> yqmStoreBargainQueryVos = generator.convert(
                yqmStoreBargainMapper.selectPage(pageModel,wrapper).getRecords(),
                YqmStoreBargainQueryVo.class);

        yqmStoreBargainQueryVos.forEach(item->{
            item.setPeople(storeBargainUserService.getBargainUserCount(item.getId(),
                    OrderInfoEnum.BARGAIN_STATUS_1.getValue()));
        });

        return yqmStoreBargainQueryVos;
    }


    /**
     * 增加分享次数
     * @param id 砍价商品id
     */
    private void addBargainShare(Long id) {
        yqmStoreBargainMapper.addBargainShare(id);
    }

    /**
     * 增加浏览次数
     * @param id 砍价商品id
     */
    private void addBargainLook(Long id) {
        yqmStoreBargainMapper.addBargainLook(id);
    }


    /**
     * 砍价支付成功订单数量
     * @param bargainId 砍价id
     * @return int
     */
    private int getBargainPayCount(Long bargainId) {
        return storeOrderService.lambdaQuery().eq(YqmStoreOrder::getBargainId,bargainId)
                .eq(YqmStoreOrder::getPaid,OrderInfoEnum.PAY_STATUS_1.getValue())
                .eq(YqmStoreOrder::getRefundStatus,OrderInfoEnum.REFUND_STATUS_0.getValue())
                .count();
    }


    //===================================================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreBargainQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreBargain> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        List<YqmStoreBargainDto> storeBargainDtoList = generator.convert(page.getList(), YqmStoreBargainDto.class);
        for (YqmStoreBargainDto storeBargainDto : storeBargainDtoList) {

            String statusStr = OrderUtil.checkActivityStatus(storeBargainDto.getStartTime(),
                    storeBargainDto.getStopTime(), storeBargainDto.getStatus());
            storeBargainDto.setStatusStr(statusStr);
        }
        map.put("content", storeBargainDtoList);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreBargain> queryAll(YqmStoreBargainQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreBargain.class, criteria));
    }


    @Override
    public void download(List<YqmStoreBargainDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreBargainDto yqmStoreBargain : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("关联产品ID", yqmStoreBargain.getProductId());
            map.put("砍价活动名称", yqmStoreBargain.getTitle());
            map.put("砍价活动图片", yqmStoreBargain.getImage());
            map.put("单位名称", yqmStoreBargain.getUnitName());
            map.put("库存", yqmStoreBargain.getStock());
            map.put("销量", yqmStoreBargain.getSales());
            map.put("砍价产品轮播图", yqmStoreBargain.getImages());
            map.put("砍价开启时间", yqmStoreBargain.getStartTime());
            map.put("砍价结束时间", yqmStoreBargain.getStopTime());
            map.put("砍价产品名称", yqmStoreBargain.getStoreName());
            map.put("砍价金额", yqmStoreBargain.getPrice());
            map.put("砍价商品最低价", yqmStoreBargain.getMinPrice());
            map.put("每次购买的砍价产品数量", yqmStoreBargain.getNum());
            map.put("用户每次砍价的最大金额", yqmStoreBargain.getBargainMaxPrice());
            map.put("用户每次砍价的最小金额", yqmStoreBargain.getBargainMinPrice());
            map.put("用户每次砍价的次数", yqmStoreBargain.getBargainNum());
            map.put("砍价状态 0(到砍价时间不自动开启)  1(到砍价时间自动开启时间)", yqmStoreBargain.getStatus());
            map.put("砍价详情", yqmStoreBargain.getDescription());
            map.put("反多少积分", yqmStoreBargain.getGiveIntegral());
            map.put("砍价活动简介", yqmStoreBargain.getInfo());
            map.put("成本价", yqmStoreBargain.getCost());
            map.put("排序", yqmStoreBargain.getSort());
            map.put("是否推荐0不推荐1推荐", yqmStoreBargain.getIsHot());
            map.put("是否包邮 0不包邮 1包邮", yqmStoreBargain.getIsPostage());
            map.put("邮费", yqmStoreBargain.getPostage());
            map.put("砍价规则", yqmStoreBargain.getRule());
            map.put("砍价产品浏览量", yqmStoreBargain.getLook());
            map.put("砍价产品分享量", yqmStoreBargain.getShare());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 删除砍价海报
     *
     * @param name
     */
    @Override
    public void deleteBargainImg(String name) {
        baseMapper.deleteBargainImg(name);
    }
}
