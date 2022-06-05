package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmProjectGoods;
import com.yqm.common.request.YqmProjectGoodsRequest;

/**
 * <p>
 * 专题关联商品 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-26
 */
public interface IYqmProjectGoodsService extends IService<YqmProjectGoods> {

    QueryWrapper<YqmProjectGoods> getQuery(YqmProjectGoodsRequest request);

}
