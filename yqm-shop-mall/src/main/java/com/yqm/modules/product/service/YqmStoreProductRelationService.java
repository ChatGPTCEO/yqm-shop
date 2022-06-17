/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service;


import com.yqm.common.service.BaseService;
import com.yqm.domain.PageResult;
import com.yqm.modules.product.domain.YqmStoreProductRelation;
import com.yqm.modules.product.service.dto.YqmStoreProductRelationDto;
import com.yqm.modules.product.service.dto.YqmStoreProductRelationQueryCriteria;
import com.yqm.modules.product.vo.YqmStoreProductRelationQueryVo;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 商品点赞和收藏表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2019-10-23
 */
public interface YqmStoreProductRelationService extends BaseService<YqmStoreProductRelation> {

    /**
     * 是否收藏
     * @param productId 商品ID
     * @param uid 用户ID
     * @return Boolean
     */
    Boolean isProductRelation(long productId, long uid);

    /**
     *添加收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    void addRroductRelation(long productId,long uid,String category);

    /**
     * 取消收藏
     * @param productId 商品id
     * @param uid 用户id
     */
    void delRroductRelation(long productId,long uid,String category);

    /**
     * 获取用户收藏列表
     * @param page page
     * @param limit limit
     * @param uid 用户id
     * @return list
     */
    List<YqmStoreProductRelationQueryVo> userCollectProduct(int page, int limit, Long uid,String type);

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    PageResult<YqmStoreProductRelationDto> queryAll(YqmStoreProductRelationQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<YqmStoreProductRelationDto>
     */
    List<YqmStoreProductRelation> queryAll(YqmStoreProductRelationQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<YqmStoreProductRelationDto> all, HttpServletResponse response) throws IOException;


    /**
     * 批量删除
     * @param ids /
     */
    void collectDelFoot(List<Long> ids);
}
