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
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveGoodInfo;
import cn.binarywang.wx.miniapp.bean.live.WxMaLiveResult;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.LiveGoodsEnum;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.mp.service.mapper.YqmWechatLiveGoodsMapper;
import com.yqm.modules.mp.domain.YqmWechatLiveGoods;
import com.yqm.modules.mp.service.YqmWechatLiveGoodsService;
import com.yqm.modules.mp.service.dto.YqmWechatLiveGoodsDto;
import com.yqm.modules.mp.service.dto.YqmWechatLiveGoodsQueryCriteria;
import com.yqm.modules.mp.config.WxMaConfiguration;
import com.yqm.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
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

/**
* @author weiximei
* @date 2020-08-11
*/
@Slf4j
@Service
//@CacheConfig(cacheNames = "yqmWechatLiveGoods")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class YqmWechatLiveGoodsServiceImpl extends BaseServiceImpl<YqmWechatLiveGoodsMapper, YqmWechatLiveGoods> implements YqmWechatLiveGoodsService {

    private final IGenerator generator;
    @Value("${file.path}")
    private String uploadDirStr;
    public YqmWechatLiveGoodsServiceImpl(IGenerator generator) {
        this.generator = generator;
    }


    /**
     * 同步商品更新审核状态
     * @return
     */
    //@Cacheable
    @Override
    public boolean synchroWxOlLive(List<Integer> goodsIds) {
        try {
            WxMaService wxMaService = WxMaConfiguration.getWxMaService();
            WxMaLiveResult liveInfos = wxMaService.getLiveGoodsService().getGoodsWareHouse(goodsIds);
            List<YqmWechatLiveGoods> convert = generator.convert(liveInfos.getGoods(), YqmWechatLiveGoods.class);
            this.saveOrUpdateBatch(convert);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void removeGoods(Long id) {
        this.removeById(id);
        try {
            WxMaService wxMaService = WxMaConfiguration.getWxMaService();
            wxMaService.getLiveGoodsService().deleteGoods(id.intValue());
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新直播商品信息
     * @param resources
     */
    @Override
    public void updateGoods(YqmWechatLiveGoods resources) {
        YqmWechatLiveGoods wechatLiveGoods = this.getById(resources.getGoodsId());
        try {
        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
            WxMaLiveGoodInfo goods = generator.convert(resources, WxMaLiveGoodInfo.class);
            if(ObjectUtil.isNotEmpty(wechatLiveGoods)){
                /** 审核状态 0：未审核，1：审核中，2:审核通过，3审核失败 */
                if(LiveGoodsEnum.IS_Audit_2.getValue().equals(wechatLiveGoods.getAuditStatus())){
                }else if(LiveGoodsEnum.IS_Audit_0.getValue().equals(wechatLiveGoods.getAuditStatus())){
                    resources.setCoverImgUrl(uploadPhotoToWx(wxMaService,resources.getCoverImgeUrl()).getMediaId());
                }else if(LiveGoodsEnum.IS_Audit_1.getValue().equals(wechatLiveGoods.getAuditStatus())){
                    throw new BadRequestException("商品审核中不允许修改");
                }else if(LiveGoodsEnum.IS_Audit_3.getValue().equals(wechatLiveGoods.getAuditStatus())){
                    resources.setCoverImgUrl(uploadPhotoToWx(wxMaService,resources.getCoverImgeUrl()).getMediaId());
                    wxMaService.getLiveGoodsService().updateGoods(goods);
                    wxMaService.getLiveGoodsService().auditGoods(goods.getGoodsId());
                    return;
                }
            }

            boolean wxMaLiveResult = wxMaService.getLiveGoodsService().updateGoods(goods);
            this.saveOrUpdate(resources);
        } catch (WxErrorException e) {
            throw new BadRequestException(e.toString());
        }
    }

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String,Object>
     */
    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmWechatLiveGoodsQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmWechatLiveGoods> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        List<YqmWechatLiveGoodsDto> goodsDtos = generator.convert(page.getList(), YqmWechatLiveGoodsDto.class);
        goodsDtos.forEach(i ->{
            i.setId(i.getGoodsId());
        });
        map.put("content",goodsDtos);
        map.put("totalElements", page.getTotal());
        return map;
    }

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<YqmWechatLiveGoodsDto>
     */
    @Override
    //@Cacheable
    public List<YqmWechatLiveGoods> queryAll(YqmWechatLiveGoodsQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmWechatLiveGoods.class, criteria));
    }

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    @Override
    public void download(List<YqmWechatLiveGoodsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmWechatLiveGoodsDto yqmWechatLiveGoods : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("关联商品id", yqmWechatLiveGoods.getProductId());
            map.put("商品图片", yqmWechatLiveGoods.getCoverImgeUrl());
            map.put("商品小程序路径", yqmWechatLiveGoods.getUrl());
            map.put("价格类型 1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传） 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）", yqmWechatLiveGoods.getPriceType());
            map.put(" price",  yqmWechatLiveGoods.getPrice());
            map.put(" price2",  yqmWechatLiveGoods.getPrice2());
            map.put("商品名称", yqmWechatLiveGoods.getName());
            map.put("1, 2：表示是为api添加商品，否则是直播控制台添加的商品", yqmWechatLiveGoods.getThirdPartyTag());
            map.put("审核单id", yqmWechatLiveGoods.getAuditId());
            map.put("审核状态 0：未审核，1：审核中，2:审核通过，3审核失败", yqmWechatLiveGoods.getAuditStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 保存直播商品信息
     * @param resources
     * @return
     */
    @Override
    public boolean saveGoods(YqmWechatLiveGoods resources) {
        WxMaService wxMaService = WxMaConfiguration.getWxMaService();
        try {
            resources.setCoverImgUrl(uploadPhotoToWx(wxMaService,resources.getCoverImgeUrl()).getMediaId());
            WxMaLiveGoodInfo goods = generator.convert(resources, WxMaLiveGoodInfo.class);
            WxMaLiveResult wxMaLiveResult = wxMaService.getLiveGoodsService().addGoods(goods);
            resources.setGoodsId(Long.valueOf(wxMaLiveResult.getGoodsId()));
            resources.setAuditId(Long.valueOf(wxMaLiveResult.getAuditId()));
            this.save(resources);
        } catch (WxErrorException e) {
            throw new BadRequestException(e.toString());
        }
        return true;
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
}
