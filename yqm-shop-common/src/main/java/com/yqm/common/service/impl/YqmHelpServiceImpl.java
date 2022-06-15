package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.YqmHelp;
import com.yqm.common.mapper.YqmHelpMapper;
import com.yqm.common.request.YqmHelpRequest;
import com.yqm.common.service.IYqmHelpService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 话题 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
@Service
public class YqmHelpServiceImpl extends ServiceImpl<YqmHelpMapper, YqmHelp> implements IYqmHelpService {

    @Override
    public QueryWrapper<YqmHelp> getQuery(YqmHelpRequest request) {
        QueryWrapper<YqmHelp> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getKeyword())) {
            queryWrapper.like("title", request.getKeyword());
        }
        if (CollectionUtils.isNotEmpty(request.getInStatusList())) {
            queryWrapper.in("status", request.getInStatusList());
        }
        if (StringUtils.isNotBlank(request.getClassificationId())) {
            queryWrapper.eq("classification_id", request.getClassificationId());
        }
        return queryWrapper;
    }

}
