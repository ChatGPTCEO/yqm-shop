package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpRecruitment;
import com.yqm.common.request.TpRecruitmentRequest;

/**
* <p>
    * 招聘 服务类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
public interface ITpRecruitmentService extends IService<TpRecruitment> {

        QueryWrapper<TpRecruitment> queryWrapper(TpRecruitmentRequest request);

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
