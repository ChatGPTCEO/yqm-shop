/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.cart.vo.YqmStoreCartQueryVo;
import com.yqm.modules.product.domain.YqmStoreProductReply;
import com.yqm.modules.product.service.YqmStoreProductReplyService;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.service.dto.YqmStoreProductReplyDto;
import com.yqm.modules.product.service.dto.YqmStoreProductReplyQueryCriteria;
import com.yqm.modules.product.service.mapper.StoreProductReplyMapper;
import com.yqm.modules.product.vo.ReplyCountVo;
import com.yqm.modules.product.vo.YqmStoreProductReplyQueryVo;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.utils.FileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class YqmStoreProductReplyServiceImpl extends BaseServiceImpl<StoreProductReplyMapper, YqmStoreProductReply> implements YqmStoreProductReplyService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private YqmUserService yqmUserService;

    @Autowired
    private YqmStoreProductService yqmStoreProductService;


    /**
     * 评价数据
     * @param productId 商品id
     * @return ReplyCountVO
     */
    @Override
    public ReplyCountVo getReplyCount(long productId) {
        int sumCount = productReplyCount(productId);

        if(sumCount == 0) {
            return new ReplyCountVo();
        }

        //好评
        int goodCount = this.baseMapper.selectCount(Wrappers.<YqmStoreProductReply>lambdaQuery()
                .eq(YqmStoreProductReply::getProductId,productId)
                .eq(YqmStoreProductReply::getProductScore,5));

        //中评
        int inCount = this.baseMapper.selectCount(Wrappers.<YqmStoreProductReply>lambdaQuery()
                .eq(YqmStoreProductReply::getProductId,productId)
                .lt(YqmStoreProductReply::getProductScore,5)
                .gt(YqmStoreProductReply::getProductScore,2));

        //差评
        int poorCount = this.baseMapper.selectCount(Wrappers.<YqmStoreProductReply>lambdaQuery()
                .eq(YqmStoreProductReply::getProductId,productId)
                .lt(YqmStoreProductReply::getProductScore,2));

        //好评率
        String replyChance = ""+NumberUtil.round(NumberUtil.mul(NumberUtil.div(goodCount,sumCount),100),2);
        String replyStar = ""+NumberUtil.round(NumberUtil.mul(NumberUtil.div(goodCount,sumCount),5),2);

        return ReplyCountVo.builder()
                .sumCount(sumCount)
                .goodCount(goodCount)
                .inCount(inCount)
                .poorCount(poorCount)
                .replyChance(replyChance)
                .replySstar(replyStar)
                .build();

    }

    /**
     * 处理评价
     * @param replyQueryVo replyQueryVo
     * @return YqmStoreProductReplyQueryVo
     */
    @Override
    public YqmStoreProductReplyQueryVo handleReply(YqmStoreProductReplyQueryVo replyQueryVo) {
        YqmStoreCartQueryVo cartInfo = JSONObject.parseObject(replyQueryVo.getCartInfo()
                ,YqmStoreCartQueryVo.class);
        if(ObjectUtil.isNotNull(cartInfo)){
            if(ObjectUtil.isNotNull(cartInfo.getProductInfo())){
                if(ObjectUtil.isNotNull(cartInfo.getProductInfo().getAttrInfo())){
                    replyQueryVo.setSku(cartInfo.getProductInfo().getAttrInfo().getSku());
                }
            }
        }

        BigDecimal star = NumberUtil.add(replyQueryVo.getProductScore(),
                replyQueryVo.getServiceScore());

        star = NumberUtil.div(star,2);

        replyQueryVo.setStar(String.valueOf(star.intValue()));

        if(StrUtil.isEmpty(replyQueryVo.getComment())){
            replyQueryVo.setComment("此用户没有填写评价");
        }

        return replyQueryVo;
    }

    /**
     * 获取单条评价
     * @param productId 商品di
     * @return YqmStoreProductReplyQueryVo
     */
    @Override
    public YqmStoreProductReplyQueryVo getReply(long productId) {
        YqmStoreProductReplyQueryVo vo = this.baseMapper.getReply(productId);
        if(ObjectUtil.isNotNull(vo)){
            return handleReply(this.baseMapper.getReply(productId));
        }
        return null;
    }


    /**
     * 获取评价列表
     * @param productId 商品id
     * @param type 0-全部 1-好评 2-中评 3-差评
     * @param page page
     * @param limit limit
     * @return list
     */
    @Override
    public List<YqmStoreProductReplyQueryVo> getReplyList(long productId,int type,int page, int limit) {
        List<YqmStoreProductReplyQueryVo> newList = new ArrayList<>();
        Page<YqmStoreProductReply> pageModel = new Page<>(page, limit);
        List<YqmStoreProductReplyQueryVo> list = this.baseMapper
                .selectReplyList(pageModel,productId,type);
        List<YqmStoreProductReplyQueryVo> list1 = list.stream().map(i ->{
            YqmStoreProductReplyQueryVo vo = new YqmStoreProductReplyQueryVo();
            BeanUtils.copyProperties(i,vo);
            if(i.getPictures().contains(",")){
                vo.setPics(i.getPictures().split(","));
            }
            return vo;
        }).collect(Collectors.toList());
        for (YqmStoreProductReplyQueryVo queryVo : list1) {
            newList.add(handleReply(queryVo));
        }
        return newList;
    }

    @Override
    public int getInfoCount(Integer oid, String unique) {
       LambdaQueryWrapper<YqmStoreProductReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreProductReply::getUnique,unique).eq(YqmStoreProductReply::getOid,oid);
        return this.baseMapper.selectCount(wrapper);
    }

    @Override
    public int productReplyCount(long productId) {

        return this.baseMapper.selectCount(Wrappers.<YqmStoreProductReply>lambdaQuery()
                .eq(YqmStoreProductReply::getProductId,productId));

    }

    @Override
    public int replyCount(String unique) {
       LambdaQueryWrapper<YqmStoreProductReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreProductReply::getUnique,unique);
        return this.baseMapper.selectCount(wrapper);
    }

    /**
     * 好评比例
     * @param productId 商品id
     * @return %
     */
    @Override
    public String replyPer(long productId) {
       LambdaQueryWrapper<YqmStoreProductReply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreProductReply::getProductId,productId)
                .eq(YqmStoreProductReply::getIsDel,ShopCommonEnum.DELETE_0.getValue())
                .eq(YqmStoreProductReply::getProductScore,5);
        int productScoreCount = this.baseMapper.selectCount(wrapper);
        int count = productReplyCount(productId);
        if(count > 0){
            return ""+NumberUtil.round(NumberUtil.mul(NumberUtil.div(productScoreCount,count),100),2);
        }

        return "0";
    }





    //===================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreProductReplyQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreProductReply> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmStoreProductReplyDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreProductReply> queryAll(YqmStoreProductReplyQueryCriteria criteria){
        List<YqmStoreProductReply> storeProductReplyList =  baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreProductReply.class, criteria));
        storeProductReplyList.forEach(yqmStoreProductReply->{
            yqmStoreProductReply.setUser(yqmUserService.getById(yqmStoreProductReply.getUid()));
            yqmStoreProductReply.setStoreProduct(yqmStoreProductService.getById(yqmStoreProductReply.getProductId()));
        });
        return storeProductReplyList;
    }


    @Override
    public void download(List<YqmStoreProductReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreProductReplyDto yqmStoreProductReply : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", yqmStoreProductReply.getUid());
            map.put("订单ID", yqmStoreProductReply.getOid());
            map.put("唯一id", yqmStoreProductReply.getUnique());
            map.put("产品id", yqmStoreProductReply.getProductId());
            map.put("某种商品类型(普通商品、秒杀商品）", yqmStoreProductReply.getReplyType());
            map.put("商品分数", yqmStoreProductReply.getProductScore());
            map.put("服务分数", yqmStoreProductReply.getServiceScore());
            map.put("评论内容", yqmStoreProductReply.getComment());
            map.put("评论图片", yqmStoreProductReply.getPics());
            map.put("管理员回复内容", yqmStoreProductReply.getMerchantReplyContent());
            map.put("管理员回复时间", yqmStoreProductReply.getMerchantReplyTime());
            map.put("0未回复1已回复", yqmStoreProductReply.getIsReply());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
