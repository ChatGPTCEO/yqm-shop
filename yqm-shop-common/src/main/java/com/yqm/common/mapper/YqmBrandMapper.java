package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.YqmBrand;
import org.apache.ibatis.annotations.Param;

/**
* <p>
    * 品牌 Mapper 接口
    * </p>
*
* @author weiximei
* @since 2022-01-29
*/
public interface YqmBrandMapper extends BaseMapper<YqmBrand> {

    /**
     * 获取最大 sort 值
     * @return
     */
    int  getMaxSort();

    /**
     * 置顶
     * @param userId
     * @param id
     */
    void topSort(@Param("userId") String userId, @Param("id") String id);

    /**
     * sort 加一
     * @param userId
     */
    void minusOneSort(@Param("userId") String userId);
}
