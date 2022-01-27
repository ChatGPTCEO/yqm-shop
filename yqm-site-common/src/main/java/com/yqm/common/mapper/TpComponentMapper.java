package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.TpComponent;

/**
 * <p>
 * 组件 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-01-11
 */
public interface TpComponentMapper extends BaseMapper<TpComponent> {

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
