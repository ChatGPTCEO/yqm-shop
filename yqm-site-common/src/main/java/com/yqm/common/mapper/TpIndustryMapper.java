package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.TpIndustry;

/**
 * <p>
 * 行业类目 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
public interface TpIndustryMapper extends BaseMapper<TpIndustry> {


    /**
     * 所有 Sort 加 1
     *
     * @param currentSort
     * @return
     */
    int updateAllSortGal(Integer currentSort);

    /**
     * 置顶
     *
     * @param id
     * @return
     */
    int top(String id);

    /**
     * 获取最大的 Sort
     *
     * @return
     */
    int getMaxSort();

}
