package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmStoreSpec;
import com.yqm.common.request.YqmStoreSpecRequest;

/**
 * <p>
 * 规格属性 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-19
 */
public interface IYqmStoreSpecService extends IService<YqmStoreSpec> {

    QueryWrapper<YqmStoreSpec> getQuery(YqmStoreSpecRequest request);
}
