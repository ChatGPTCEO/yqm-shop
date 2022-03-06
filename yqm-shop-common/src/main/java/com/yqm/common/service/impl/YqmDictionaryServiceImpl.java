package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmDictionary;
import com.yqm.common.mapper.YqmDictionaryMapper;
import com.yqm.common.request.YqmDictionaryRequest;
import com.yqm.common.service.IYqmDictionaryService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-06
 */
@Service
public class YqmDictionaryServiceImpl extends ServiceImpl<YqmDictionaryMapper, YqmDictionary> implements IYqmDictionaryService {

    @Override
    public QueryWrapper<YqmDictionary> getQuery(YqmDictionaryRequest request) {
        QueryWrapper<YqmDictionary> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getPcode())) {
            queryWrapper.eq("pcode", request.getPcode());
        }
        if (CollectionUtils.isNotEmpty(request.getStatusList())) {
            queryWrapper.in("status", request.getStatusList());
        }
        return queryWrapper;
    }
}
