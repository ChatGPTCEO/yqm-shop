/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.mp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import cn.binarywang.wx.miniapp.json.WxMaGsonBuilder;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.mp.domain.YqmWechatLiveGoods;
import com.yqm.modules.mp.service.YqmWechatLiveGoodsService;
import com.yqm.modules.mp.service.YqmWechatLiveService;
import com.yqm.modules.mp.service.dto.*;
import com.yqm.modules.mp.service.mapper.YqmWechatLiveMapper;
import com.yqm.modules.mp.vo.WechatLiveVo;
import com.yqm.modules.mp.domain.YqmWechatLive;
import com.yqm.modules.mp.config.WxMaConfiguration;
import com.yqm.utils.FileUtil;
import com.yqm.utils.OrderUtil;
import com.yqm.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.json.GsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.binarywang.wx.miniapp.constant.WxMaApiUrlConstants.Broadcast.Room.CREATE_ROOM;


/**
* @author weiximei
* @date 2020-08-10
*/
@Service
//@CacheConfig(cacheNames = "yqmWechatLive")
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class YqmWechatLiveServiceImpl extends BaseServiceImpl<YqmWechatLiveMapper, YqmWechatLive> implements YqmWechatLiveService {

    private final IGenerator generator;
    @Value("${file.path}")
    private String uploadDirStr;
    private final YqmWechatLiveGoodsService wechatLiveGoodsService;

    private final  YqmWechatLiveMapper wechatLiveMapper;

    public YqmWechatLiveServiceImpl(IGenerator generator, YqmWechatLiveGoodsService wechatLiveGoodsService, YqmWechatLiveMapper wechatLiveMapper) {
        this.generator = generator;
        this.wechatLiveGoodsService = wechatLiveGoodsService;
        this.wechatLiveMapper = wechatLiveMapper;
    }

    /**
     * 同步直播间
     * @return
     */
    //@Cacheable
    @Override
    public boolean synchroWxOlLive() {
        try {
            WxMaService wxMaService = WxMaConfiguration.getWxMaService();
            List<WxMaLiveResult.RoomInfo> liveInfos = wxMaService.getLiveService().getLiveInfos();
            List<YqmWechatLive> convert = generator.convert(liveInfos, YqmWechatLive.class);
            this.saveOrUpdateBatch(convert);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return true;
    }
    @Override
    //@Cacheable
    public WechatLiveVo queryAll(YqmWechatLiveQueryCriteria criteria, Pageable pageable) {
        String order=null;
        if(pageable.getSort()!=null){
            order= pageable.getSort().toString();
            order=order.replace(":","");
            if("UNSORTED".equals(order)){
                order="start_time desc";
            }
        }
        PageHelper.startPage(pageable.getPageNumber()+1, pageable.getPageSize(),order);
        PageInfo<YqmWechatLive> page = new PageInfo<>(queryAll(criteria));
        WechatLiveVo wechatLiveVo = new WechatLiveVo();
//            List<WxMaLiveResult.RoomInfo> liveInfos = wxMaLiveService.getLiveInfos();
        List<YqmWechatLiveDto> liveDtos = generator.convert(page.getList(), YqmWechatLiveDto.class);
        //获取所有商品
        liveDtos.forEach(i ->{
            if(StringUtils.isNotBlank(i.getProductId())){
                List<YqmWechatLiveGoodsDto> wechatLiveGoodsDtos = generator.convert(
                        wechatLiveGoodsService.list(new LambdaQueryWrapper<YqmWechatLiveGoods>().in(YqmWechatLiveGoods::getGoodsId,i.getProductId().split(",")))
                        ,YqmWechatLiveGoodsDto.class);
                i.setProduct(wechatLiveGoodsDtos);
            }
            i.setId(i.getRoomId());
        });
        wechatLiveVo.setContent(liveDtos);
        wechatLiveVo.setTotalElements(page.getTotal());
        wechatLiveVo.setPageNumber(page.getPageNum());
        wechatLiveVo.setLastPage(page.getPages());
        return wechatLiveVo;
    }

    @Override
    public boolean addGoods(UpdateGoodsDto resources) {
        YqmWechatLive wechatLive = new YqmWechatLive();

        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        if(StringUtils.isNotBlank(resources.getProductId())){
            wechatLive.setRoomId(Long.valueOf(resources.getRoomId()));
            wechatLive.setProductId(resources.getProductId());
            String[] productIds = resources.getProductId().split(",");
            List<Integer> pids = new ArrayList<>();
            for (String productId : productIds) {
                pids.add(Integer.valueOf(productId));
            }
            //添加商品
            try {
                wxMaService.getLiveService().addGoodsToRoom(resources.getRoomId().intValue(), pids);
                this.saveOrUpdate(wechatLive);
            } catch (WxErrorException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    @Override
    public boolean saveLive(YqmWechatLive resources){
        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        try {
            resources.setFeedsImg(uploadPhotoToWx(wxMaService,resources.getFeedsImg()).getMediaId());
            resources.setStartTime(Long.valueOf(OrderUtil.dateToTimestamp(resources.getStartDate())));
            resources.setEndTime(Long.valueOf(OrderUtil.dateToTimestamp(resources.getEndDate())));
            resources.setAnchorImg(uploadPhotoToWx(wxMaService,resources.getAnchorImge()).getMediaId());
            resources.setCoverImg(uploadPhotoToWx(wxMaService,resources.getCoverImge()).getMediaId());
            resources.setShareImg(uploadPhotoToWx(wxMaService,resources.getShareImge()).getMediaId());
            WxMaLiveInfo.RoomInfo roomInfo = generator.convert(resources, WxMaLiveInfo.RoomInfo.class);
            Integer status = this.createRoom(roomInfo);
            resources.setRoomId(Long.valueOf(status));
            if(StringUtils.isNotBlank(resources.getProductId())){
                String[] productIds = resources.getProductId().split(",");
                List<Integer> pids = new ArrayList<>();
                for (String productId : productIds) {
                    pids.add(Integer.valueOf(productId));
                }
                //添加商品
                wxMaService.getLiveService().addGoodsToRoom(status, pids);
            }
            this.save(resources);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new BadRequestException(e.toString());
        }
        return false;
    }

    @Override
    //@Cacheable
    public List<YqmWechatLive> queryAll(YqmWechatLiveQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmWechatLive.class, criteria));
    }

    @Override
    //@Cacheable
    public List<WxMaLiveResult.LiveReplay> getLiveReplay(Integer roomId){
        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        WxMaLiveResult get_replay = new WxMaLiveResult();
        try {
             get_replay = wxMaService.getLiveService().getLiveReplay("get_replay", roomId, 0, 100);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return get_replay.getLiveReplay();
    }
    @Override
    public void download(List<YqmWechatLiveDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmWechatLiveDto yqmWechatLive : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("直播间标题", yqmWechatLive.getName());
            map.put("背景图", yqmWechatLive.getCoverImge());
            map.put("分享图片", yqmWechatLive.getShareImge());
            map.put("直播间状态", yqmWechatLive.getLiveStatus());
            map.put("开始时间", yqmWechatLive.getStartTime());
            map.put("预计结束时间", yqmWechatLive.getEndTime());
            map.put("主播昵称", yqmWechatLive.getAnchorName());
            map.put("主播微信号", yqmWechatLive.getAnchorWechat());
            map.put("主播头像", yqmWechatLive.getAnchorImge());
            map.put("直播间类型 1：推流 0：手机直播", yqmWechatLive.getType());
            map.put("横屏、竖屏 【1：横屏，0：竖屏】", yqmWechatLive.getScreenType());
            map.put("是否关闭货架 【0：开启，1：关闭】", yqmWechatLive.getCloseLike());
            map.put("是否关闭评论 【0：开启，1：关闭】", yqmWechatLive.getCloseComment());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public Integer createRoom(WxMaLiveInfo.RoomInfo roomInfo) throws WxErrorException {
        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        String responseContent = wxMaService.post(CREATE_ROOM, WxMaGsonBuilder.create().toJson(roomInfo));
        JsonObject jsonObject = GsonParser.parse(responseContent);
        if (jsonObject.get("errcode").getAsInt() != 0) {
            throw new WxErrorException(WxError.fromJson(responseContent, WxType.MiniApp));
        }
        return jsonObject.get("roomId").getAsInt();
    }
    /**
     * 上传临时素材
     * @param wxMaService WxMaService
     * @param picPath 图片路径
     * @return WxMpMaterialUploadResult
     * @throws WxErrorException
     */
    private WxMediaUploadResult uploadPhotoToWx(WxMaService wxMaService, String picPath) throws WxErrorException {
        String filename = (int) System.currentTimeMillis() + ".png";
        String downloadPath = uploadDirStr + filename;
        long size = HttpUtil.downloadFile(picPath, cn.hutool.core.io.FileUtil.file(downloadPath));
        picPath = downloadPath;
        File picFile = new File( picPath );
        log.info( "picFile name : {}", picFile.getName() );
        WxMediaUploadResult wxMediaUploadResult = wxMaService.getMediaService().uploadMedia( WxConsts.MediaFileType.IMAGE, picFile );
        log.info( "wxMpMaterialUploadResult : {}", JSONUtil.toJsonStr( wxMediaUploadResult ) );
        return wxMediaUploadResult;
    }


    /**
     * 直播间列表
     * @param page 页码
     * @param limit 条数
     * @param order ProductEnum
     * @return List
     */
    @Override
    public List<YqmWechatLiveDto> getList(int page, int limit, int order) {
        //todo 添加状态判断
       LambdaQueryWrapper<YqmWechatLive> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(YqmWechatLive::getStartTime);


        Page<YqmWechatLive> pageModel = new Page<>(page, limit);

        IPage<YqmWechatLive> pageList = wechatLiveMapper.selectPage(pageModel,wrapper);


        return generator.convert(pageList.getRecords(),YqmWechatLiveDto.class);
    }


}
