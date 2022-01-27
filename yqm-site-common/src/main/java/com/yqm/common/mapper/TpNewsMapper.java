package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.TpNews;

/**
* <p>
    * 新闻 Mapper 接口
    * </p>
*
* @author weiximei
* @since 2021-11-28
*/
public interface TpNewsMapper extends BaseMapper<TpNews> {

    /**
     * 所有 Sort 加 1
     * @param currentSort
     * @return
     */
    int updateAllSortGal(Integer currentSort, String userId);

    /**
     * 置顶
     * @param id
     * @return
     */
    int top(String id, String userId);

    /**
     * 获取最大的 Sort
     * @return
     */
    int getMaxSort(String userId);

}
