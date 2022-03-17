package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmStoreProduct;
import com.yqm.common.request.YqmStoreProductRequest;

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
    void topSort(String userId,String id);

    /**
     * sort 加一
     *
     * @param userId
     */
    void minusOneSort(String userId);

    /**
     * 获取总数
     * @param userId
     * @return
     */
    int getCount(String userId);

    /**
     * 获取总数 (上下架)
     * @param userId
     * @return
     */
    int getShelvesCount(String userId, String shelves);

}
