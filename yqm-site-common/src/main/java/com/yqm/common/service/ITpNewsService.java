package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpNews;
import com.yqm.common.request.TpNewsRequest;

/**
* <p>
    * 新闻 服务类
    * </p>
*
* @author weiximei
* @since 2021-11-28
*/
public interface ITpNewsService extends IService<TpNews> {

    QueryWrapper<TpNews> queryWrapper(TpNewsRequest request);

    /**
     * 所有排序加一
     * @return
     */
    int updateAllSortGal(Integer currentSort,String userId);

    /**
     * 置顶
     * @return
     */
    int top(String id,String userId);

    /**
     * 获取最大 Sort
     * @return
     */
    int getMaxSort(String userId);

}
