package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmProjectGoods;
import com.yqm.common.mapper.YqmProjectGoodsMapper;
import com.yqm.common.request.YqmProjectGoodsRequest;
import com.yqm.common.service.IYqmProjectGoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专题关联商品 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-05-26
 */
@Service
public class YqmProjectGoodsServiceImpl extends ServiceImpl<YqmProjectGoodsMapper, YqmProjectGoods> implements IYqmProjectGoodsService {

    @Override
    public QueryWrapper<YqmProjectGoods> getQuery(YqmProjectGoodsRequest request) {
        QueryWrapper<YqmProjectGoods> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getProjectId())) {
            queryWrapper.eq("project_id", request.getProjectId());
        }
        if (StringUtils.isNotBlank(request.getProjectGoodsId())) {
            queryWrapper.eq("project_goods_id", request.getProjectGoodsId());
        }
        return queryWrapper;
    }
}
