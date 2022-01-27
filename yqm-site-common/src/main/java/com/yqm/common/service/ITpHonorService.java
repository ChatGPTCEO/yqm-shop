package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpHonor;
import com.yqm.common.entity.TpLink;
import com.yqm.common.request.TpHonorRequest;
import com.yqm.common.request.TpLinkRequest;

/**
* <p>
    * 荣誉证书 服务类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
public interface ITpHonorService extends IService<TpHonor> {

    QueryWrapper<TpHonor> queryWrapper(TpHonorRequest request);

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
