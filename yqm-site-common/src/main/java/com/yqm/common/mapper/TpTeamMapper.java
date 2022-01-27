package com.yqm.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqm.common.entity.TpTeam;
import org.springframework.stereotype.Repository;

/**
* <p>
    * 团队 Mapper 接口
    * </p>
*
* @author weiximei
* @since 2021-09-11
*/
public interface TpTeamMapper extends BaseMapper<TpTeam> {

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
