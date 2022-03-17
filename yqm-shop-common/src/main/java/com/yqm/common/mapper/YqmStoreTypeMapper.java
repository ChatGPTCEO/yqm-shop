package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.YqmStoreType;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品类型 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-03-13
 */
public interface YqmStoreTypeMapper extends BaseMapper<YqmStoreType> {

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
