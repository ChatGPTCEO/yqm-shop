package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmStoreAttribute;
import com.yqm.common.request.YqmStoreAttributeRequest;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-13
 */
public interface IYqmStoreAttributeService extends IService<YqmStoreAttribute> {


    QueryWrapper<YqmStoreAttribute> getQuery(YqmStoreAttributeRequest request);

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
