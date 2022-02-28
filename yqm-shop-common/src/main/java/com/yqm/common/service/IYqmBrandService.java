package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmBrand;
import com.yqm.common.request.YqmBrandRequest;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 品牌 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-29
 */
public interface IYqmBrandService extends IService<YqmBrand> {

    QueryWrapper<YqmBrand> getQuery(YqmBrandRequest request);

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
