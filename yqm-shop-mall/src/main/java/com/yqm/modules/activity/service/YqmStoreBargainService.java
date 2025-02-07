/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.activity.domain.YqmStoreBargain;
import com.yqm.modules.activity.service.dto.YqmStoreBargainDto;
import com.yqm.modules.activity.service.dto.YqmStoreBargainQueryCriteria;
import com.yqm.modules.activity.vo.BargainCountVo;
import com.yqm.modules.activity.vo.BargainVo;
import com.yqm.modules.activity.vo.TopCountVo;
import com.yqm.modules.activity.vo.YqmStoreBargainQueryVo;
import com.yqm.modules.user.domain.YqmUser;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-05-13
*/
public interface YqmStoreBargainService  extends BaseService<YqmStoreBargain>{

    /**
     * 退回库存销量
     * @param num 数量
     * @param bargainId 砍价产品id
     */
    void incStockDecSales(int num,Long bargainId);

    /**
     * 增加销量 减少库存
     * @param num 数量
     * @param bargainId 砍价id
     */
    void decStockIncSales(int num,Long bargainId);

    //YqmStoreBargain getBargain(int bargainId);

    /**
     * 开始帮助好友砍价
     * @param bargainId 砍价产品id
     * @param bargainUserUid 开启砍价用户id
     * @param uid 当前用户id
     */
    void doHelp(Long bargainId,Long bargainUserUid,Long uid);

    /**
     * 顶部统计
     * @param bargainId 砍价商品id
     * @return TopCountVo
     */
    TopCountVo topCount(Long bargainId);

    /**
     * 砍价 砍价帮总人数、剩余金额、进度条、已经砍掉的价格
     * @param bargainId 砍价商品id
     * @param uid 砍价用户id
     * @param myUid 当前用户id
     * @return BargainCountVo
     */
    BargainCountVo helpCount(Long bargainId, Long uid, Long myUid);

    //int getBargainPayCount(int bargainId);

    //void addBargainShare(int id);

    //void addBargainLook(int id);

    /**
     * 砍价详情
     * @param id 砍价id
     * @param yqmUser 用户
     * @return BargainVo
     */
    BargainVo getDetail(Long id, YqmUser yqmUser);


    /**
     * 获取砍价商品列表
     * @param page page
     * @param limit limit
     * @return List
     */
    List<YqmStoreBargainQueryVo> getList(int page, int limit);


    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmStoreBargainQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmStoreBargainDto>
    */
    List<YqmStoreBargain> queryAll(YqmStoreBargainQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmStoreBargainDto> all, HttpServletResponse response) throws IOException;

    /**
     * 删除砍价海报
     * @param id
     */
    void deleteBargainImg(String id);
}
