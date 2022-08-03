/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.tools.service.mapper;

import com.yqm.common.mapper.CoreMapper;
import com.yqm.tools.domain.QiniuContent;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author weiximei
* @date 2020-05-13
*/
@Repository
@Mapper
public interface QiniuContentMapper extends CoreMapper<QiniuContent> {

}
