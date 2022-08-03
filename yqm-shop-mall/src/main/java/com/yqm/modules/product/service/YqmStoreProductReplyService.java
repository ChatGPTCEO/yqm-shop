/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.product.domain.YqmStoreProductReply;
import com.yqm.modules.product.service.dto.YqmStoreProductReplyDto;
import com.yqm.modules.product.service.dto.YqmStoreProductReplyQueryCriteria;
import com.yqm.modules.product.vo.ReplyCountVo;
import com.yqm.modules.product.vo.YqmStoreProductReplyQueryVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-12
*/
public interface YqmStoreProductReplyService  extends BaseService<YqmStoreProductReply>{

    /**
     * 评价数据
     * @param productId 商品id
     * @return ReplyCountVO
     */
    ReplyCountVo getReplyCount(long productId);

    /**
     * 处理评价
     * @param replyQueryVo replyQueryVo
     * @return YqmStoreProductReplyQueryVo
     */
    YqmStoreProductReplyQueryVo handleReply(YqmStoreProductReplyQueryVo replyQueryVo);

    /**
     * 获取单条评价
     * @param productId 商品di
     * @return YqmStoreProductReplyQueryVo
     */
    YqmStoreProductReplyQueryVo getReply(long productId);

    /**
     * 获取评价列表
     * @param productId 商品id
     * @param type 0-全部 1-好评 2-中评 3-差评
     * @param page page
     * @param limit limit
     * @return list
     */
    List<YqmStoreProductReplyQueryVo> getReplyList(long productId,int type,int page, int limit);

    int getInfoCount(Integer oid, String unique);

    int productReplyCount(long productId);

    int replyCount(String unique);

    String replyPer(long productId);


    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmStoreProductReplyQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmStoreProductReplyDto>
    */
    List<YqmStoreProductReply> queryAll(YqmStoreProductReplyQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmStoreProductReplyDto> all, HttpServletResponse response) throws IOException;
}
