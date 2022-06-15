package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmHelp;
import com.yqm.common.request.YqmHelpRequest;

/**
 * <p>
 * 话题 服务类
 * </p>
 *
 * @author weiximei
 * @since 2022-06-14
 */
public interface IYqmHelpService extends IService<YqmHelp> {

    QueryWrapper<YqmHelp> getQuery(YqmHelpRequest request);

}
