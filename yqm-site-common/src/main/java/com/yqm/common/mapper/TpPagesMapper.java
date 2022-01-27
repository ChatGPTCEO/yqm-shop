package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.TpPages;

/**
 * <p>
 * 页面 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-01-02
 */
public interface TpPagesMapper extends BaseMapper<TpPages> {

    /**
     * 所有 Sort 加 1
     *
     * @param currentSort
     * @return
     */
    int updateAllSortGal(Integer currentSort, String userId);

    /**
     * 置顶
     *
     * @param id
     * @return
     */
    int top(String id, String userId);

    /**
     * 获取最大的 Sort
     *
     * @return
     */
    int getMaxSort(String userId);

}
