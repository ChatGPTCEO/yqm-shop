/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service.impl;

import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.domain.PageResult;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.product.domain.YqmStoreProductRelation;
import com.yqm.modules.product.service.YqmStoreProductRelationService;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.service.dto.YqmStoreProductRelationDto;
import com.yqm.modules.product.service.dto.YqmStoreProductRelationQueryCriteria;
import com.yqm.modules.product.service.mapper.YqmStoreProductRelationMapper;
import com.yqm.modules.product.vo.YqmStoreProductRelationQueryVo;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 商品点赞和收藏表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-10-23
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YqmStoreProductRelationServiceImpl extends BaseServiceImpl<YqmStoreProductRelationMapper, YqmStoreProductRelation> implements YqmStoreProductRelationService {

    private final YqmStoreProductRelationMapper yqmStoreProductRelationMapper;
    private final YqmStoreProductService storeProductService;
    private final YqmUserService userService;
    private final IGenerator generator;

    /**
     * 获取用户收藏列表
     * @param page page
     * @param limit limit
     * @param uid 用户id
     * @return list
     */
    @Override
    public List<YqmStoreProductRelationQueryVo> userCollectProduct(int page, int limit, Long uid,String type) {
        Page<YqmStoreProductRelation> pageModel = new Page<>(page, limit);
        List<YqmStoreProductRelationQueryVo> list = yqmStoreProductRelationMapper.selectRelationList(pageModel,uid,type);
        return list;
    }

    /**
     * 添加收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    @Override
    public void addRroductRelation(long productId,long uid,String category) {
        if(isProductRelation(productId,uid)) {
            throw new YqmShopException("已收藏");
        }
        YqmStoreProductRelation storeProductRelation = YqmStoreProductRelation.builder()
                .productId(productId)
                .uid(uid)
                .type(category)
                .build();
        yqmStoreProductRelationMapper.insert(storeProductRelation);
    }

    /**
     * 取消收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    @Override
    public void delRroductRelation(long productId,long uid,String category) {
        YqmStoreProductRelation productRelation = this.lambdaQuery()
                .eq(YqmStoreProductRelation::getProductId,productId)
                .eq(YqmStoreProductRelation::getUid,uid)
                .eq(YqmStoreProductRelation::getType,category)
                .one();
        if(productRelation == null) {
            throw new YqmShopException("已取消");
        }
        this.removeById(productRelation.getId());
    }


    /**
     * 是否收藏
     * @param productId 商品ID
     * @param uid 用户ID
     * @return Boolean
     */
    @Override
    public Boolean isProductRelation(long productId, long uid) {
        int count = yqmStoreProductRelationMapper
                .selectCount(Wrappers.<YqmStoreProductRelation>lambdaQuery()
                        .eq(YqmStoreProductRelation::getUid,uid)
                        .eq(YqmStoreProductRelation::getType,"collect")
                        .eq(YqmStoreProductRelation::getProductId,productId));
        if(count > 0) {
            return true;
        }

        return false;
    }

    @Override
    //@Cacheable
    public PageResult<YqmStoreProductRelationDto> queryAll(YqmStoreProductRelationQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreProductRelation> page = new PageInfo<>(queryAll(criteria));
        PageResult<YqmStoreProductRelationDto> relationDtoPageResult = generator.convertPageInfo(page, YqmStoreProductRelationDto.class);
        relationDtoPageResult.getContent().forEach(i ->{
            i.setProduct(storeProductService.getById(i.getProductId()));
            i.setUserName(userService.getYqmUserById(i.getUid()).getNickname());
        });
        return relationDtoPageResult;
    }


    @Override
    //@Cacheable
    public List<YqmStoreProductRelation> queryAll(YqmStoreProductRelationQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreProductRelation.class, criteria));
    }


    @Override
    public void download(List<YqmStoreProductRelationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreProductRelationDto yqmStoreProductRelation : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", yqmStoreProductRelation.getUid());
            map.put("商品ID", yqmStoreProductRelation.getProductId());
            map.put("类型(收藏(collect）、点赞(like))", yqmStoreProductRelation.getType());
            map.put("某种类型的商品(普通商品、秒杀商品)", yqmStoreProductRelation.getCategory());
            map.put("添加时间", yqmStoreProductRelation.getCreateTime());
            map.put(" updateTime",  yqmStoreProductRelation.getUpdateTime());
            map.put(" isDel",  yqmStoreProductRelation.getIsDel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void collectDelFoot(List<Long> ids) {
        yqmStoreProductRelationMapper.deleteBatchIds(ids);
    }
}
