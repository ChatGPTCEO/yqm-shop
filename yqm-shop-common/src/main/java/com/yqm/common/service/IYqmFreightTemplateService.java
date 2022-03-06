package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmFreightTemplate;
import com.yqm.common.request.YqmFreightTemplateRequest;

/**
 * <p>
 * 运费模板 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-03-06
 */
public interface IYqmFreightTemplateService extends IService<YqmFreightTemplate> {
    
    QueryWrapper<YqmFreightTemplate> getQuery(YqmFreightTemplateRequest request);
}
