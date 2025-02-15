/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.cn
* 注意：
* 本软件为www.yqmshop.cn开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.mp.service;

import com.yqm.common.service.BaseService;
import com.yqm.modules.mp.service.dto.YqmWechatLiveGoodsDto;
import com.yqm.modules.mp.service.dto.YqmWechatLiveGoodsQueryCriteria;
import com.yqm.modules.mp.domain.YqmWechatLiveGoods;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author weiximei
* @date 2020-08-11
*/
public interface YqmWechatLiveGoodsService  extends BaseService<YqmWechatLiveGoods>{

/**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(YqmWechatLiveGoodsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmWechatLiveGoodsDto>
    */
    List<YqmWechatLiveGoods> queryAll(YqmWechatLiveGoodsQueryCriteria criteria);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmWechatLiveGoodsDto> all, HttpServletResponse response) throws IOException;

    /**
     * 保存直播商品信息
     * @param resources
     * @return
     */
    boolean saveGoods(YqmWechatLiveGoods resources);

    /**
     * 同步商品更新审核状态
     * @param goodsIds
     * @return
     */
    boolean synchroWxOlLive(List<Integer> goodsIds);

    /**
     * 根据id删除直播商品信息
     * @param id
     */
    void removeGoods(Long id);

    /**
     * 更新直播商品信息
     * @param resources
     */
    void updateGoods(YqmWechatLiveGoods resources);
}
