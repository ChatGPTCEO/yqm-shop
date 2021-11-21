package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpLink;
import com.yqm.common.entity.TpNewsClassify;
import com.yqm.common.request.TpLinkRequest;
import com.yqm.common.request.TpNewsClassifyRequest;

/**
* <p>
    * 新闻分类 服务类
    * </p>
*
* @author weiximei
* @since 2021-11-21
*/
public interface ITpNewsClassifyService extends IService<TpNewsClassify> {


    QueryWrapper<TpNewsClassify> queryWrapper(TpNewsClassifyRequest request);

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


