package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpComponent;
import com.yqm.common.request.TpComponentRequest;

/**
 * <p>
 * 组件 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-11
 */
public interface ITpComponentService extends IService<TpComponent> {


    QueryWrapper<TpComponent> queryWrapper(TpComponentRequest request);

    /**
     * 所有排序加一
     *
     * @return
     */
    int updateAllSortGal(Integer currentSort);

    /**
     * 置顶
     *
     * @return
     */
    int top(String id);

    /**
     * 获取最大 Sort
     *
     * @return
     */
    int getMaxSort();

}
