package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.YqmStoreProduct;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
public interface YqmStoreProductMapper extends BaseMapper<YqmStoreProduct> {

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

    /**
     * 获取总数
     * @param userId
     * @return
     */
    int getCount(@Param("userId") String userId, @Param("status") String status);

    /**
     * 获取总数 (上下架)
     * @param userId
     * @return
     */
    int getShelvesCount(@Param("userId") String userId, @Param("shelves") String shelves);

}
