package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpIndustry;
import com.yqm.common.request.TpIndustryRequest;

/**
 * <p>
 * 行业类目 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-01-10
 */
public interface ITpIndustryService extends IService<TpIndustry> {

    QueryWrapper<TpIndustry> queryWrapper(TpIndustryRequest request);

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
