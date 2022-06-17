/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yqm.api.BusinessException;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ProductTypeEnum;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.product.domain.YqmStoreProductAttr;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.service.YqmStoreProductAttrResultService;
import com.yqm.modules.product.service.YqmStoreProductAttrService;
import com.yqm.modules.product.service.YqmStoreProductAttrValueService;
import com.yqm.modules.product.service.dto.AttrValueDto;
import com.yqm.modules.product.service.dto.FromatDetailDto;
import com.yqm.modules.product.service.dto.ProductFormatDto;
import com.yqm.modules.product.service.mapper.StoreProductAttrMapper;
import com.yqm.modules.product.service.mapper.StoreProductAttrValueMapper;
import com.yqm.modules.product.vo.YqmStoreProductAttrQueryVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class YqmStoreProductAttrServiceImpl extends BaseServiceImpl<StoreProductAttrMapper, YqmStoreProductAttr> implements YqmStoreProductAttrService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private StoreProductAttrMapper yqmStoreProductAttrMapper;
    @Autowired
    private StoreProductAttrValueMapper yqmStoreProductAttrValueMapper;

    @Autowired
    private YqmStoreProductAttrValueService storeProductAttrValueService;
    @Autowired
    private YqmStoreProductAttrResultService storeProductAttrResultService;

    /**
     * 新增商品属性
     * @param items attr
     * @param attrs value
     * @param productId 商品id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertYqmStoreProductAttr(List<FromatDetailDto> items, List<ProductFormatDto> attrs,
                                         Long productId)
    {
        List<YqmStoreProductAttr> attrGroup = new ArrayList<>();
        for (FromatDetailDto fromatDetailDto : items) {
            YqmStoreProductAttr  yqmStoreProductAttr = YqmStoreProductAttr.builder()
                    .productId(productId)
                    .attrName(fromatDetailDto.getValue())
                    .attrValues(StrUtil.join(",",fromatDetailDto.getDetail()))
                    .build();

            attrGroup.add(yqmStoreProductAttr);
        }

        /*int count = storeProductAttrValueService.count(Wrappers.<YqmStoreProductAttrValue>lambdaQuery().eq(YqmStoreProductAttrValue::getProductId, productId));
        if (count > 0 ) {
            throw new BadRequestException("该产品已被添加到其他活动,禁止操作!");
        }*/

        List<YqmStoreProductAttrValue> valueGroup = new ArrayList<>();
        for (ProductFormatDto productFormatDto : attrs) {

            if(productFormatDto.getPinkStock()>productFormatDto.getStock() || productFormatDto.getSeckillStock()>productFormatDto.getStock()){
                throw new BadRequestException("活动商品库存不能大于原有商品库存");
            }
            List<String> stringList = new ArrayList<>(productFormatDto.getDetail().values());
            Collections.sort(stringList);

            YqmStoreProductAttrValue yqmStoreProductAttrValue = YqmStoreProductAttrValue.builder()
                    .productId(productId)
                    .sku(StrUtil.join(",",stringList))
                    .price(BigDecimal.valueOf(productFormatDto.getPrice()))
                    .cost(BigDecimal.valueOf(productFormatDto.getCost()))
                    .otPrice(BigDecimal.valueOf(productFormatDto.getOtPrice()))
                    .unique(IdUtil.simpleUUID())
                    .image(productFormatDto.getPic())
                    .barCode(productFormatDto.getBarCode())
                    .weight(BigDecimal.valueOf(productFormatDto.getWeight()))
                    .volume(BigDecimal.valueOf(productFormatDto.getVolume()))
                    .brokerage(BigDecimal.valueOf(productFormatDto.getBrokerage()))
                    .brokerageTwo(BigDecimal.valueOf(productFormatDto.getBrokerageTwo()))
                    .stock(productFormatDto.getStock())
                    .integral(productFormatDto.getIntegral())
                    .pinkPrice(BigDecimal.valueOf(productFormatDto.getPinkPrice()==null?0:productFormatDto.getPinkPrice()))
                    .seckillPrice(BigDecimal.valueOf(productFormatDto.getSeckillPrice()==null?0:productFormatDto.getSeckillPrice()))
                    .pinkStock(productFormatDto.getPinkStock()==null?0:productFormatDto.getPinkStock())
                    .seckillStock(productFormatDto.getSeckillStock()==null?0:productFormatDto.getSeckillStock())
                    .build();


            valueGroup.add(yqmStoreProductAttrValue);
        }

        if(attrGroup.isEmpty() || valueGroup.isEmpty()){
            throw new BusinessException("请设置至少一个属性!");
        }

        //清理属性
        this.clearProductAttr(productId);

        //批量添加
        this.saveBatch(attrGroup);
        storeProductAttrValueService.saveBatch(valueGroup);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("attr",items);
        map.put("value",attrs);

        storeProductAttrResultService.insertYqmStoreProductAttrResult(map,productId);
    }

    /**
     * 删除YqmStoreProductAttrValue表的属性
     * @param productId 商品id
     */
    private void clearProductAttr(Long productId) {
        if(ObjectUtil.isNull(productId)) {
            throw new YqmShopException("产品不存在");
        }

        yqmStoreProductAttrMapper.delete(Wrappers.<YqmStoreProductAttr>lambdaQuery()
                .eq(YqmStoreProductAttr::getProductId,productId));
        yqmStoreProductAttrValueMapper.delete(Wrappers.<YqmStoreProductAttrValue>lambdaQuery()
                .eq(YqmStoreProductAttrValue::getProductId,productId));

    }


    /**
     * 增加库存减去销量
     * @param num 数量
     * @param productId 商品id
     * @param unique sku唯一值
     */
    @Override
    public void incProductAttrStock(Integer num, Long productId, String unique, String type ) {

        if(ProductTypeEnum.COMBINATION.getValue().equals(type)){
           yqmStoreProductAttrValueMapper.incCombinationStockDecSales(num,productId,unique);
        }else if(ProductTypeEnum.SECKILL.getValue().equals(type)){
           yqmStoreProductAttrValueMapper.incSeckillStockDecSales(num,productId,unique);
        }else {
            yqmStoreProductAttrValueMapper.incStockDecSales(num,productId,unique);
        }
    }

    /**
     * 减少库存增加销量（针对sku操作）
     * @param num 数量
     * @param productId 商品id
     * @param unique sku唯一值
     */
    @Override
    public void decProductAttrStock(int num, Long productId, String unique,String type) {
        int res = 0;
        if(ProductTypeEnum.COMBINATION.getValue().equals(type)){
            res =  yqmStoreProductAttrValueMapper.decCombinationStockIncSales(num,productId,unique);
        }else if(ProductTypeEnum.SECKILL.getValue().equals(type)){
            res =  yqmStoreProductAttrValueMapper.decSeckillStockIncSales(num,productId,unique);
        }else {
            res =  yqmStoreProductAttrValueMapper.decStockIncSales(num,productId,unique);
        }
        if(res == 0) {
            throw new YqmShopException("商品库存不足");
        }
    }



    /**
     * 更加sku 唯一值获取sku对象
     * @param unique 唯一值
     * @return YqmStoreProductAttrValue
     */
    @Override
    public YqmStoreProductAttrValue uniqueByAttrInfo(String unique) {
        return yqmStoreProductAttrValueMapper.selectOne(Wrappers.<YqmStoreProductAttrValue>lambdaQuery()
                .eq(YqmStoreProductAttrValue::getUnique,unique));
    }


    /**
     * 获取商品sku属性
     * @param productId 商品id
     * @return map
     */
    @Override
    public Map<String, Object> getProductAttrDetail(long productId) {

        List<YqmStoreProductAttr>  storeProductAttrs = yqmStoreProductAttrMapper
                .selectList(Wrappers.<YqmStoreProductAttr>lambdaQuery()
                        .eq(YqmStoreProductAttr::getProductId,productId)
                        .orderByAsc(YqmStoreProductAttr::getAttrValues));

        List<YqmStoreProductAttrValue>  productAttrValues = storeProductAttrValueService
                .list(Wrappers.<YqmStoreProductAttrValue>lambdaQuery()
                        .eq(YqmStoreProductAttrValue::getProductId,productId));


        Map<String, YqmStoreProductAttrValue> map = productAttrValues.stream()
                .collect(Collectors.toMap(YqmStoreProductAttrValue::getSku, p -> p));

        List<YqmStoreProductAttrQueryVo> yqmStoreProductAttrQueryVoList = new ArrayList<>();

        for (YqmStoreProductAttr attr : storeProductAttrs) {
            List<String> stringList = Arrays.asList(attr.getAttrValues().split(","));
            List<AttrValueDto> attrValueDTOS = new ArrayList<>();
            for (String str : stringList) {
                AttrValueDto attrValueDTO = new AttrValueDto();
                attrValueDTO.setAttr(str);
                attrValueDTOS.add(attrValueDTO);
            }
            YqmStoreProductAttrQueryVo attrQueryVo = generator.convert(attr,YqmStoreProductAttrQueryVo.class);
            attrQueryVo.setAttrValue(attrValueDTOS);
            attrQueryVo.setAttrValueArr(stringList);

            yqmStoreProductAttrQueryVoList.add(attrQueryVo);
        }

        Map<String, Object> returnMap = new LinkedHashMap<>(2);
        returnMap.put("productAttr",yqmStoreProductAttrQueryVoList);
        returnMap.put("productValue",map);

        return returnMap;
    }

}
