package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmStoreType;
import com.yqm.common.request.YqmStoreTypeRequest;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品类型 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-13
 */
public interface IYqmStoreTypeService extends IService<YqmStoreType> {

    QueryWrapper<YqmStoreType> getQuery(YqmStoreTypeRequest request);

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
     * sort 加一
     *
     * @param userId
     */
    void minusOneSort(@Param("userId") String userId);

    /**
     * 属性加一
     * @param id
     */
    void minusOneAttributeNum(@Param("id") String id);

    /**
     * 属性减一
     * @param id
     */
    void subtractionOneAttributeNum(@Param("id") String id);

}
