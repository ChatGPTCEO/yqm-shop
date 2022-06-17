/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.modules.mp.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.mp.service.dto.YqmWechatReplyDto;
import com.yqm.modules.mp.service.dto.YqmWechatReplyQueryCriteria;
import com.yqm.modules.mp.domain.YqmWechatReply;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmWechatReplyService  extends BaseService<YqmWechatReply>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmWechatReplyQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmWechatReplyDto>
    */
    List<YqmWechatReply> queryAll(YqmWechatReplyQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmWechatReplyDto> all, HttpServletResponse response) throws IOException;

    YqmWechatReply isExist(String subscribe);

    void create(YqmWechatReply yqmWechatReply);

    void upDate(YqmWechatReply yqmWechatReply);
}
