package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpPages;
import com.yqm.common.request.TpPagesRequest;

/**
 * <p>
 * 页面 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-02
 */
public interface ITpPagesService extends IService<TpPages> {


    QueryWrapper<TpPages> queryWrapper(TpPagesRequest request);

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
