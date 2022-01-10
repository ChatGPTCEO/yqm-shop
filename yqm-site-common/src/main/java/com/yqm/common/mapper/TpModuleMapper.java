package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.TpModule;

/**
 * <p>
 * 模块 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
public interface TpModuleMapper extends BaseMapper<TpModule> {

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
