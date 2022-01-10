package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpModule;
import com.yqm.common.request.TpModuleRequest;

/**
 * <p>
 * 模块 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
public interface ITpModuleService extends IService<TpModule> {

    QueryWrapper<TpModule> queryWrapper(TpModuleRequest request);

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
