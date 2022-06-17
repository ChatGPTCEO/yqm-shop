/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.cart.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.CartTypeEnum;
import com.yqm.enums.OrderInfoEnum;
import com.yqm.enums.ProductTypeEnum;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.activity.domain.YqmStoreBargain;
import com.yqm.modules.activity.service.YqmStoreBargainService;
import com.yqm.modules.activity.service.mapper.YqmStoreBargainMapper;
import com.yqm.modules.activity.service.mapper.YqmStoreCombinationMapper;
import com.yqm.modules.activity.service.mapper.YqmStoreSeckillMapper;
import com.yqm.modules.cart.domain.YqmStoreCart;
import com.yqm.modules.cart.service.YqmStoreCartService;
import com.yqm.modules.cart.service.dto.YqmStoreCartDto;
import com.yqm.modules.cart.service.dto.YqmStoreCartQueryCriteria;
import com.yqm.modules.cart.service.mapper.StoreCartMapper;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.order.service.dto.CountDto;
import com.yqm.modules.product.domain.YqmStoreProduct;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.service.YqmStoreProductAttrService;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.vo.YqmStoreProductQueryVo;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author weiximei
 * @date 2020-05-12
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreCartServiceImpl extends BaseServiceImpl<StoreCartMapper, YqmStoreCart> implements YqmStoreCartService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private StoreCartMapper yqmStoreCartMapper;
    @Autowired
    private YqmStoreSeckillMapper storeSeckillMapper;
    @Autowired
    private YqmStoreBargainMapper yqmStoreBargainMapper;
    @Autowired
    private YqmStoreCombinationMapper storeCombinationMapper;
    @Autowired
    private YqmStoreProductService productService;
    @Autowired
    private YqmStoreProductAttrService productAttrService;
    @Autowired
    private YqmStoreBargainService storeBargainService;
    @Autowired
    private YqmUserService userService;


    /**
     * 删除购物车
     *
     * @param uid uid
     * @param ids 购物车id集合
     */
    @Override
    public void removeUserCart(Long uid, List<String> ids) {
        List<Long> newids = ids.stream().map(Long::new).collect(Collectors.toList());
        yqmStoreCartMapper.delete(Wrappers.<YqmStoreCart>lambdaQuery()
                .eq(YqmStoreCart::getUid, uid)
                .in(YqmStoreCart::getId, newids));
    }

    /**
     * 改购物车数量
     *
     * @param cartId  购物车id
     * @param cartNum 数量
     * @param uid     uid
     */
    @Override
    public void changeUserCartNum(Long cartId, int cartNum, Long uid) {
        YqmStoreCart cart = this.lambdaQuery()
                .eq(YqmStoreCart::getUid, uid)
                .eq(YqmStoreCart::getId, cartId)
                .one();
        if (cart == null) {
            throw new YqmShopException("购物车不存在");
        }

        if (cartNum <= 0) {
            throw new YqmShopException("库存错误");
        }

        //普通商品库存
        int stock = productService.getProductStock(cart.getProductId()
                , cart.getProductAttrUnique(), "");
        if (stock < cartNum) {
            throw new YqmShopException("该产品库存不足" + cartNum);
        }

        if (cartNum == cart.getCartNum()) {
            return;
        }

        YqmStoreCart storeCart = new YqmStoreCart();
        storeCart.setCartNum(cartNum);
        storeCart.setId(cartId);

        yqmStoreCartMapper.updateById(storeCart);


    }

    /**
     * 购物车列表
     *
     * @param uid     用户id
     * @param cartIds 购物车id，多个逗号隔开
     * @param status  0-购购物车列表
     * @return map valid-有效购物车 invalid-失效购物车
     */
    @Override
    public Map<String, Object> getUserProductCartList(Long uid, String cartIds, Integer status) {
       LambdaQueryWrapper<YqmStoreCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreCart::getUid, uid)
                .eq(YqmStoreCart::getIsPay, OrderInfoEnum.PAY_STATUS_0.getValue())
                .orderByDesc(YqmStoreCart::getId);
        if (status == null) {
            wrapper.eq(YqmStoreCart::getIsNew, CartTypeEnum.NEW_0.getValue());
        }
        if (StrUtil.isNotEmpty(cartIds)) {
            wrapper.in(YqmStoreCart::getId, Arrays.asList(cartIds.split(",")));
        }
        List<YqmStoreCart> carts = yqmStoreCartMapper.selectList(wrapper);

        List<YqmStoreCartQueryVo> valid = new ArrayList<>();
        List<YqmStoreCartQueryVo> invalid = new ArrayList<>();

        for (YqmStoreCart storeCart : carts) {
            YqmStoreProductQueryVo storeProduct = null;
            if (storeCart.getCombinationId() != null && storeCart.getCombinationId() > 0) {
                storeProduct = ObjectUtil.clone(storeCombinationMapper.combinatiionInfo(storeCart.getCombinationId()));
            } else if (storeCart.getSeckillId() != null && storeCart.getSeckillId() > 0) {
                storeProduct = ObjectUtil.clone(storeSeckillMapper.seckillInfo(storeCart.getSeckillId()));
            } else if (storeCart.getBargainId() != null && storeCart.getBargainId() > 0) {
                storeProduct = ObjectUtil.clone(yqmStoreBargainMapper.bargainInfo(storeCart.getBargainId()));
            } else {
                //必须得重新克隆创建一个新对象
                storeProduct = ObjectUtil.clone(productService
                        .getStoreProductById(storeCart.getProductId()));
            }

            YqmStoreCartQueryVo storeCartQueryVo = generator.convert(storeCart, YqmStoreCartQueryVo.class);

            if (ObjectUtil.isNull(storeProduct)) {
                this.removeById(storeCart.getId());
            } else if (ShopCommonEnum.SHOW_0.getValue().equals(storeProduct.getIsShow()) || (storeProduct.getStock() == 0 && StrUtil.isEmpty(storeCart.getProductAttrUnique()))) {
                storeCartQueryVo.setProductInfo(storeProduct);
                invalid.add(storeCartQueryVo);
            } else {
                if (StrUtil.isNotEmpty(storeCart.getProductAttrUnique())) {
                    YqmStoreProductAttrValue productAttrValue = productAttrService
                            .uniqueByAttrInfo(storeCart.getProductAttrUnique());
                    if (ObjectUtil.isNull(productAttrValue) || productAttrValue.getStock() == 0) {
                        storeCartQueryVo.setProductInfo(storeProduct);
                        invalid.add(storeCartQueryVo);
                    } else {
                        storeProduct.setAttrInfo(productAttrValue);
                        storeCartQueryVo.setProductInfo(storeProduct);

                        //设置真实价格
                        //设置VIP价格
                        double vipPrice = userService.setLevelPrice(
                                productAttrValue.getPrice().doubleValue(), uid);
                        //砍价金额
                        if ( storeCart.getBargainId() > 0
                               ) {
                            vipPrice = storeProduct.getPrice().doubleValue();
                        }
                        //设置拼团价格
                       if(storeCart.getCombinationId() > 0 ){
                            vipPrice = productAttrValue.getPinkPrice().doubleValue();
                        }
                        //设置秒杀价格
                        if( storeCart.getSeckillId() > 0){
                            vipPrice = productAttrValue.getSeckillPrice().doubleValue();
                        }
                        storeCartQueryVo.setTruePrice(vipPrice);
                        //设置会员价
                        storeCartQueryVo.setVipTruePrice(productAttrValue.getPrice()
                                .doubleValue());
                        storeCartQueryVo.setCostPrice(productAttrValue.getCost()
                                .doubleValue());
                        storeCartQueryVo.setTrueStock(productAttrValue.getStock());

                        valid.add(storeCartQueryVo);

                    }
                } else {
                    //设置VIP价格,营销商品不参与
                    double vipPrice = userService.setLevelPrice(
                            storeProduct.getPrice().doubleValue(), uid);
                    if (storeCart.getCombinationId() > 0 || storeCart.getSeckillId() > 0
                            || storeCart.getBargainId() > 0) {
                        vipPrice = storeProduct.getPrice().doubleValue();
                    }

                    storeCartQueryVo.setTruePrice(vipPrice);
                    // 设置会员价
                    storeCartQueryVo.setVipTruePrice(0d);
                    storeCartQueryVo.setCostPrice(storeProduct.getCost()
                            .doubleValue());
                    storeCartQueryVo.setTrueStock(storeProduct.getStock());
                    storeCartQueryVo.setProductInfo(storeProduct);

                    valid.add(storeCartQueryVo);
                }
            }

        }

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("valid", valid);
        map.put("invalid", invalid);
        return map;
    }

    /**
     * 添加购物车
     * @param uid               用户id
     * @param productId         普通产品编号
     * @param cartNum           购物车数量
     * @param productAttrUnique 属性唯一值
     * @param isNew             1 加入购物车直接购买  0 加入购物车
     * @param combinationId     拼团id
     * @param seckillId         秒杀id
     * @param bargainId         砍价id
     * @return 购物车id
     */
    @Override
    public long addCart(Long uid, Long productId, Integer cartNum, String productAttrUnique,
                        Integer isNew, Long combinationId, Long seckillId, Long bargainId) {

        this.checkProductStock(uid, productId, cartNum, productAttrUnique, combinationId, seckillId, bargainId);
       LambdaQueryWrapper<YqmStoreCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreCart::getUid, uid)
                .eq(YqmStoreCart::getIsPay, OrderInfoEnum.PAY_STATUS_0.getValue())
                .eq(YqmStoreCart::getProductId, productId)
                .eq(YqmStoreCart::getIsNew, isNew)
                .eq(YqmStoreCart::getProductAttrUnique, productAttrUnique)
                .eq(YqmStoreCart::getBargainId, bargainId)
                .eq(YqmStoreCart::getCombinationId, combinationId)
                .eq(YqmStoreCart::getSeckillId, seckillId)
                .orderByDesc(YqmStoreCart::getId)
                .last("limit 1");

        YqmStoreCart cart = yqmStoreCartMapper.selectOne(wrapper);

        YqmStoreCart storeCart = YqmStoreCart.builder()
                .cartNum(cartNum)
                .productAttrUnique(productAttrUnique)
                .productId(productId)
                .bargainId(bargainId)
                .combinationId(combinationId)
                .seckillId(seckillId)
                .isNew(isNew)
                .uid(uid)
                .build();
        if (cart != null) {
            if (CartTypeEnum.NEW_0.getValue().equals(isNew)) {
                storeCart.setCartNum(cartNum + cart.getCartNum());
            }
            storeCart.setId(cart.getId());
            yqmStoreCartMapper.updateById(storeCart);
        } else {
            yqmStoreCartMapper.insert(storeCart);
        }

        return storeCart.getId();
    }

    /**
     * 返回当前用户购物车总数量
     *
     * @param uid 用户id
     * @return int
     */
    @Override
    public int getUserCartNum(Long uid) {
        return yqmStoreCartMapper.cartSum(uid);
    }

//    @Override
//    public YqmStoreCartQueryVo getYqmStoreCartById(Serializable id){
//        return yqmStoreCartMapper.getYqmStoreCartById(id);
//    }

    /**
     * 检测商品/秒杀/砍价/拼团库存
     *
     * @param uid               用户ID
     * @param productId         产品ID
     * @param cartNum           购买数量
     * @param productAttrUnique 商品属性Unique
     * @param combinationId     拼团产品ID
     * @param seckillId         秒杀产品ID
     * @param bargainId         砍价产品ID
     */
    @Override
    public void checkProductStock(Long uid, Long productId, Integer cartNum, String productAttrUnique,
                                  Long combinationId, Long seckillId, Long bargainId) {
        Date now = new Date();
        //拼团
        if (combinationId != null && combinationId > 0) {
            YqmStoreProduct product = productService
                    .lambdaQuery().eq(YqmStoreProduct::getId, productId)
                    .eq(YqmStoreProduct::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                    .one();
            if (product == null) {
                throw new YqmShopException("该产品已下架或删除");
            }

            int stock = productService.getProductStock(productId, productAttrUnique, ProductTypeEnum.PINK.getValue());
            if (stock < cartNum) {
                throw new YqmShopException(product.getStoreName() + "库存不足" + cartNum);
            }
            //秒杀
        } else if (seckillId != null && seckillId > 0) {
            YqmStoreProduct product = productService
                    .lambdaQuery().eq(YqmStoreProduct::getId, productId)
                    .eq(YqmStoreProduct::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                    .one();
            if (product == null) {
                throw new YqmShopException("该产品已下架或删除");
            }

            int stock = productService.getProductStock(productId, productAttrUnique, ProductTypeEnum.SECKILL.getValue());
            if (stock < cartNum) {
                throw new YqmShopException(product.getStoreName() + "库存不足" + cartNum);
            }
            //砍价
        } else if (bargainId != null && bargainId > 0) {
            YqmStoreBargain yqmStoreBargain = storeBargainService
                    .lambdaQuery().eq(YqmStoreBargain::getId, bargainId)
                    .eq(YqmStoreBargain::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                    .le(YqmStoreBargain::getStartTime, now)
                    .ge(YqmStoreBargain::getStopTime, now)
                    .one();
            if (yqmStoreBargain == null) {
                throw new YqmShopException("该产品已下架或删除");
            }
            if (yqmStoreBargain.getStock() < cartNum) {
                throw new YqmShopException("该产品库存不足");
            }

        } else {
            YqmStoreProduct product = productService
                    .lambdaQuery().eq(YqmStoreProduct::getId, productId)
                    .eq(YqmStoreProduct::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                    .one();
            if (product == null) {
                throw new YqmShopException("该产品已下架或删除");
            }

            int stock = productService.getProductStock(productId, productAttrUnique, "");
            if (stock < cartNum) {
                throw new YqmShopException(product.getStoreName() + "库存不足" + cartNum);
            }
        }

    }


    //====================================================================//


    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreCartQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCart> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmStoreCartDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreCart> queryAll(YqmStoreCartQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreCart.class, criteria));
    }


    @Override
    public void download(List<YqmStoreCartDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCartDto yqmStoreCart : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户ID", yqmStoreCart.getUid());
            map.put("类型", yqmStoreCart.getType());
            map.put("商品ID", yqmStoreCart.getProductId());
            map.put("商品属性", yqmStoreCart.getProductAttrUnique());
            map.put("商品数量", yqmStoreCart.getCartNum());
            map.put("添加时间", yqmStoreCart.getAddTime());
            map.put("0 = 未购买 1 = 已购买", yqmStoreCart.getIsPay());
            map.put("是否删除", yqmStoreCart.getIsDel());
            map.put("是否为立即购买", yqmStoreCart.getIsNew());
            map.put("拼团id", yqmStoreCart.getCombinationId());
            map.put("秒杀产品ID", yqmStoreCart.getSeckillId());
            map.put("砍价id", yqmStoreCart.getBargainId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<CountDto> findCateName() {
        return yqmStoreCartMapper.findCateName();
    }
}
