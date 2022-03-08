package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmStoreProduct;
import com.yqm.common.request.YqmStoreProductRequest;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
public interface IYqmStoreProductService extends IService<YqmStoreProduct> {

    QueryWrapper<YqmStoreProduct> getQuery(YqmStoreProductRequest request);


    /**
     * 获取最大 sort 值
     *
     * @return
     */
    int getMaxSort();

    /**
     * 置顶
     *
     * @param userId
     * @param id
     */
    void topSort(@Param("userId") String userId, @Param("id") String id);

    /**
     * sort 减一
     *
     * @param userId
     */
    void minusOneSort(@Param("userId") String userId);

}
