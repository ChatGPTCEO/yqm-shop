package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.TpTemplate;

/**
 * <p>
 * 模板 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
public interface TpTemplateMapper extends BaseMapper<TpTemplate> {

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
