package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.YqmClassification;
import com.yqm.common.request.YqmClassificationRequest;

/**
* <p>
    * 商品分类 服务类
    * </p>
*
* @author weiximei
* @since 2022-01-30
*/
public interface IYqmClassificationService extends IService<YqmClassification> {

	 QueryWrapper<YqmClassification> getQuery(YqmClassificationRequest request);
}
