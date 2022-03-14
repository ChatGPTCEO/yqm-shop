package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.YqmStoreAttribute;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品属性 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-03-13
 */
public interface YqmStoreAttributeMapper extends BaseMapper<YqmStoreAttribute> {

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
