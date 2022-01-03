package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpSite;
import com.yqm.common.request.TpSiteRequest;

/**
 * <p>
 * 站点 服务类
 * </p>
 *
 * @author weiximei
 * @since 2021-12-26
 */
public interface ITpSiteService extends IService<TpSite> {


    QueryWrapper<TpSite> queryWrapper(TpSiteRequest request);

    /**
     * 所有排序加一
     *
     * @return
     */
    int updateAllSortGal(Integer currentSort, String userId);

    /**
     * 置顶
     *
     * @return
     */
    int top(String id, String userId);

    /**
     * 获取最大 Sort
     *
     * @return
     */
    int getMaxSort(String userId);

}
