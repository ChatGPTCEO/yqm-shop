/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.mp.service;

import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import com.yqm.common.service.BaseService;
import com.yqm.modules.mp.service.dto.UpdateGoodsDto;
import com.yqm.modules.mp.service.dto.WxMaLiveInfo;
import com.yqm.modules.mp.service.dto.YqmWechatLiveDto;
import com.yqm.modules.mp.service.dto.YqmWechatLiveQueryCriteria;
import com.yqm.modules.mp.vo.WechatLiveVo;
import com.yqm.modules.mp.domain.YqmWechatLive;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
* @author weiximei
* @date 2020-08-10
*/
public interface YqmWechatLiveService  extends BaseService<YqmWechatLive>{




    /**
     * 同步直播间
     * @return
     */
    boolean synchroWxOlLive();

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    WechatLiveVo queryAll(YqmWechatLiveQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<YqmWechatLiveDto>
    */
    List<YqmWechatLive> queryAll(YqmWechatLiveQueryCriteria criteria);


    boolean saveLive(YqmWechatLive resources);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<YqmWechatLiveDto> all, HttpServletResponse response) throws IOException;


    /**
     * 创建直播间
     * <pre>
     * 调用此接口创建直播间，创建成功后将在直播间列表展示，调用额度：10000次/一天
     * 文档地址：https://developers.weixin.qq.com/miniprogram/dev/framework/liveplayer/studio-api.html#1
     * http请求方式：POST https://api.weixin.qq.com/wxaapi/broadcast/room/create?access_token=ACCESS_TOKEN
     * </pre>
     *
     * @param roomInfo 直播间信息
     * @return .
     * @throws WxErrorException .
     */
    Integer createRoom(WxMaLiveInfo.RoomInfo roomInfo) throws WxErrorException;

    /**
     * 获取直播回放
     * @param roomId
     * @return
     */
     List<WxMaLiveResult.LiveReplay> getLiveReplay(Integer roomId);


    /**
     * 商品列表
     * @param page 页码
     * @param limit 条数
     * @param order ProductEnum
     * @return List
     */
    List<YqmWechatLiveDto> getList(int page, int limit, int order);

    boolean addGoods(UpdateGoodsDto resources);
}
