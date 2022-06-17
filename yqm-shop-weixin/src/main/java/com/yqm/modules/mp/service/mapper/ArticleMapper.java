/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.mp.service.mapper;

import com.yqm.common.mapper.CoreMapper;
import com.yqm.modules.mp.domain.YqmArticle;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
* @author weiximei
* @date 2020-05-12
*/
@Repository
public interface ArticleMapper extends CoreMapper<YqmArticle> {
    @Update("update yqm_article set visit=visit+1 " +
            "where id=#{id}")
    int incVisitNum(@Param("id") int id);

}
