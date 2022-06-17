/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.enums.SpecTypeEnum;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.activity.domain.YqmStoreSeckill;
import com.yqm.modules.activity.service.YqmStoreSeckillService;
import com.yqm.modules.activity.service.dto.YqmStoreSeckillDto;
import com.yqm.modules.activity.service.dto.YqmStoreSeckillQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreSeckillMapper;
import com.yqm.modules.activity.vo.StoreSeckillVo;
import com.yqm.modules.activity.vo.YqmStoreSeckillQueryVo;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.service.YqmStoreProductAttrService;
import com.yqm.modules.product.service.YqmStoreProductReplyService;
import com.yqm.modules.product.service.dto.FromatDetailDto;
import com.yqm.modules.product.service.dto.ProductFormatDto;
import com.yqm.modules.product.service.dto.ProductResultDto;
import com.yqm.modules.product.vo.YqmStoreProductAttrQueryVo;
import com.yqm.modules.template.domain.YqmShippingTemplates;
import com.yqm.modules.template.service.YqmShippingTemplatesService;
import com.yqm.utils.FileUtil;
import com.yqm.utils.OrderUtil;
import com.yqm.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
* @author weiximei
* @date 2020-05-13
*/
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreSeckillServiceImpl extends BaseServiceImpl<YqmStoreSeckillMapper, YqmStoreSeckill> implements YqmStoreSeckillService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private YqmStoreSeckillMapper yqmStoreSeckillMapper;

    @Autowired
    private YqmStoreProductReplyService replyService;

    @Autowired
    private YqmStoreProductAttrService yqmStoreProductAttrService;

    @Autowired
    private YqmShippingTemplatesService shippingTemplatesService;

    /**
     * 产品详情
     * @param id 砍价商品id
     * @return StoreSeckillVo
     */
    @Override
    public StoreSeckillVo getDetail(Long id){
        Date now = new Date();
        YqmStoreSeckill storeSeckill = this.lambdaQuery().eq(YqmStoreSeckill::getId,id)
                .eq(YqmStoreSeckill::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(YqmStoreSeckill::getIsShow,ShopCommonEnum.SHOW_1.getValue())
                .le(YqmStoreSeckill::getStartTime,now)
                .ge(YqmStoreSeckill::getStopTime,now)
                .one();

        if(storeSeckill == null){
            throw new YqmShopException("秒杀产品不存在或已下架");
        }
        //获取商品sku
        Map<String, Object> returnMap = yqmStoreProductAttrService.getProductAttrDetail(storeSeckill.getProductId());
        //获取运费模板名称
        String storeFreePostage = RedisUtil.get("store_free_postage");
        String tempName = "";
        if(StrUtil.isBlank(storeFreePostage)
                || !NumberUtil.isNumber(storeFreePostage)
                || Integer.parseInt(storeFreePostage) == 0){
            tempName = "全国包邮";
        }else{
            YqmShippingTemplates shippingTemplates = shippingTemplatesService.getById(storeSeckill.getTempId());
            if(ObjectUtil.isNotNull(shippingTemplates)){
                tempName = shippingTemplates.getName();
            }else {
                throw new BadRequestException("请配置运费模板");
            }

        }
        return StoreSeckillVo.builder()
                .productAttr((List<YqmStoreProductAttrQueryVo>)returnMap.get("productAttr"))
                .productValue((Map<String, YqmStoreProductAttrValue>)returnMap.get("productValue"))
                .storeInfo(generator.convert(storeSeckill, YqmStoreSeckillQueryVo.class))
                .reply(replyService.getReply(storeSeckill.getProductId()))
                .replyCount(replyService.productReplyCount(storeSeckill.getProductId()))
                .tempName(tempName)
                .build();
    }

    /**
     * 秒杀产品列表
     * @param page page
     * @param limit limit
     * @return list
     */
    @Override
    public List<YqmStoreSeckillQueryVo> getList(int page, int limit, int time) {
        Date nowTime = new Date();
        Page<YqmStoreSeckill> pageModel = new Page<>(page, limit);
       LambdaQueryWrapper<YqmStoreSeckill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreSeckill::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(YqmStoreSeckill::getTimeId,time)
                .le(YqmStoreSeckill::getStartTime,nowTime)
                .ge(YqmStoreSeckill::getStopTime,nowTime)
                .orderByDesc(YqmStoreSeckill::getSort);
        List<YqmStoreSeckillQueryVo> yqmStoreSeckillQueryVos = generator.convert
               (yqmStoreSeckillMapper.selectPage(pageModel,wrapper).getRecords(),
                       YqmStoreSeckillQueryVo.class);
        yqmStoreSeckillQueryVos.forEach(item->{
            Integer sum = item.getSales() + item.getStock();
            item.setPercent(NumberUtil.round(NumberUtil.mul(NumberUtil.div(item.getSales(),sum),
                    100),0).intValue());
        });
        return yqmStoreSeckillQueryVos;
    }
    /**
     * 秒杀产品列表（首页用）
     * @param page page
     * @param limit limit
     * @return list
     */
    @Override
    public List<YqmStoreSeckillQueryVo> getList(int page, int limit) {
        Date nowTime = new Date();
        Page<YqmStoreSeckill> pageModel = new Page<>(page, limit);
       LambdaQueryWrapper<YqmStoreSeckill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreSeckill::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(YqmStoreSeckill::getIsHot,1)
                .le(YqmStoreSeckill::getStartTime,nowTime)
                .ge(YqmStoreSeckill::getStopTime,nowTime)
                .orderByDesc(YqmStoreSeckill::getSort);
        List<YqmStoreSeckillQueryVo> yqmStoreSeckillQueryVos = generator.convert
                (yqmStoreSeckillMapper.selectPage(pageModel,wrapper).getRecords(),
                        YqmStoreSeckillQueryVo.class);
        yqmStoreSeckillQueryVos.forEach(item->{
            Integer sum = item.getSales() + item.getStock();
            item.setPercent(NumberUtil.round(NumberUtil.mul(NumberUtil.div(item.getSales(),sum),
                    100),0).intValue());
        });
        return yqmStoreSeckillQueryVos;
    }
    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreSeckillQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreSeckill> page = new PageInfo<>(queryAll(criteria));
        List<YqmStoreSeckillDto> storeSeckillDTOS = generator.convert(page.getList(),YqmStoreSeckillDto.class);
        for (YqmStoreSeckillDto storeSeckillDTO : storeSeckillDTOS){
            String statusStr = OrderUtil.checkActivityStatus(storeSeckillDTO.getStartTime(),
                    storeSeckillDTO.getStopTime(), storeSeckillDTO.getStatus());
            storeSeckillDTO.setStatusStr(statusStr);
        }
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",storeSeckillDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreSeckill> queryAll(YqmStoreSeckillQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreSeckill.class, criteria));
    }


    @Override
    public void download(List<YqmStoreSeckillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreSeckillDto yqmStoreSeckill : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("推荐图", yqmStoreSeckill.getImage());
            map.put("轮播图", yqmStoreSeckill.getImages());
            map.put("活动标题", yqmStoreSeckill.getTitle());
            map.put("简介", yqmStoreSeckill.getInfo());
            map.put("返多少积分", yqmStoreSeckill.getGiveIntegral());
            map.put("排序", yqmStoreSeckill.getSort());
            map.put("库存", yqmStoreSeckill.getStock());
            map.put("销量", yqmStoreSeckill.getSales());
            map.put("单位名", yqmStoreSeckill.getUnitName());
            map.put("邮费", yqmStoreSeckill.getPostage());
            map.put("内容", yqmStoreSeckill.getDescription());
            map.put("开始时间", yqmStoreSeckill.getStartTime());
            map.put("结束时间", yqmStoreSeckill.getStopTime());
            map.put("产品状态", yqmStoreSeckill.getStatus());
            map.put("是否包邮", yqmStoreSeckill.getIsPostage());
            map.put("热门推荐", yqmStoreSeckill.getIsHot());
            map.put("最多秒杀几个", yqmStoreSeckill.getNum());
            map.put("显示", yqmStoreSeckill.getIsShow());
            map.put("时间段id", yqmStoreSeckill.getTimeId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public boolean saveSeckill(YqmStoreSeckillDto resources) {
        ProductResultDto resultDTO = this.computedProduct(resources.getAttrs());

        //添加商品
        YqmStoreSeckill yqmStoreSeckill = new YqmStoreSeckill();
        BeanUtil.copyProperties(resources,yqmStoreSeckill,"images");
        if(resources.getImages().isEmpty()) {
            throw new YqmShopException("请上传轮播图");
        }

        yqmStoreSeckill.setStock(resultDTO.getStock());
        yqmStoreSeckill.setOtPrice(BigDecimal.valueOf(resultDTO.getMinOtPrice()));
        yqmStoreSeckill.setPrice(BigDecimal.valueOf(resultDTO.getMinPrice()));
        yqmStoreSeckill.setCost(BigDecimal.valueOf(resultDTO.getMinCost()));
        yqmStoreSeckill.setStock(resultDTO.getStock());
        yqmStoreSeckill.setImages(String.join(",", resources.getImages()));
        this.saveOrUpdate(yqmStoreSeckill);

        //属性处理
        //处理单sKu
        if(SpecTypeEnum.TYPE_0.getValue().equals(resources.getSpecType())){
            FromatDetailDto fromatDetailDto = FromatDetailDto.builder()
                    .value("规格")
                    .detailValue("")
                    .attrHidden("")
                    .detail(ListUtil.toList("默认"))
                    .build();
            List<ProductFormatDto> attrs = resources.getAttrs();
            ProductFormatDto productFormatDto = attrs.get(0);
            productFormatDto.setValue1("规格");
            Map<String,String> map = new HashMap<>();
            map.put("规格","默认");
            productFormatDto.setDetail(map);
            yqmStoreProductAttrService.insertYqmStoreProductAttr(ListUtil.toList(fromatDetailDto),
                    ListUtil.toList(productFormatDto),resources.getProductId());
        }else{
            yqmStoreProductAttrService.insertYqmStoreProductAttr(resources.getItems(),
                    resources.getAttrs(),resources.getProductId());
        }
        return true;
    }

    /**
     * 计算产品数据
     * @param attrs attrs
     * @return ProductResultDto
     */
    private ProductResultDto computedProduct(List<ProductFormatDto> attrs){
        //取最小价格
        Double minPrice = attrs
                .stream()
                .map(ProductFormatDto::getSeckillPrice)
                .min(Comparator.naturalOrder())
                .orElse(0d);

        Double minOtPrice = attrs
                .stream()
                .map(ProductFormatDto::getOtPrice)
                .min(Comparator.naturalOrder())
                .orElse(0d);

        Double minCost = attrs
                .stream()
                .map(ProductFormatDto::getCost)
                .min(Comparator.naturalOrder())
                .orElse(0d);
        //计算库存
        Integer stock = attrs
                .stream()
                .map(ProductFormatDto::getSeckillStock)
                .reduce(Integer::sum)
                .orElse(0);

        if(stock <= 0) {
            throw new YqmShopException("库存不能低于0");
        }

        return ProductResultDto.builder()
                .minPrice(minPrice)
                .minOtPrice(minOtPrice)
                .minCost(minCost)
                .stock(stock)
                .build();
    }

}
