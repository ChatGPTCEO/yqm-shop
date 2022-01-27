package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpLink;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.request.TpLinkClassifyRequest;
import com.yqm.common.request.TpLinkRequest;

/**
* <p>
    * 友情链接 服务类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
public interface ITpLinkService extends IService<TpLink> {

    QueryWrapper<TpLink> queryWrapper(TpLinkRequest request);

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
