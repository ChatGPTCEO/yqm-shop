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
import com.yqm.enums.ProductTypeEnum;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.enums.SpecTypeEnum;
import com.yqm.exception.BadRequestException;
import com.yqm.modules.activity.domain.YqmStoreCombination;
import com.yqm.modules.activity.domain.YqmStorePink;
import com.yqm.modules.activity.domain.YqmStoreVisit;
import com.yqm.modules.activity.service.YqmStoreCombinationService;
import com.yqm.modules.activity.service.YqmStorePinkService;
import com.yqm.modules.activity.service.dto.PinkAllDto;
import com.yqm.modules.activity.service.dto.YqmStoreCombinationDto;
import com.yqm.modules.activity.service.dto.YqmStoreCombinationQueryCriteria;
import com.yqm.modules.activity.service.mapper.YqmStoreCombinationMapper;
import com.yqm.modules.activity.service.mapper.YqmStorePinkMapper;
import com.yqm.modules.activity.service.mapper.YqmStoreVisitMapper;
import com.yqm.modules.activity.vo.CombinationQueryVo;
import com.yqm.modules.activity.vo.StoreCombinationVo;
import com.yqm.modules.activity.vo.YqmStoreCombinationQueryVo;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.service.YqmStoreProductAttrService;
import com.yqm.modules.product.service.YqmStoreProductAttrValueService;
import com.yqm.modules.product.service.YqmStoreProductReplyService;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.service.dto.FromatDetailDto;
import com.yqm.modules.product.service.dto.ProductFormatDto;
import com.yqm.modules.product.service.dto.ProductResultDto;
import com.yqm.modules.product.vo.YqmStoreProductAttrQueryVo;
import com.yqm.modules.template.domain.YqmShippingTemplates;
import com.yqm.modules.template.service.YqmShippingTemplatesService;
import com.yqm.utils.FileUtil;
import com.yqm.utils.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
* @author weiximei
* @date 2020-05-13
*/
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class YqmStoreCombinationServiceImpl extends BaseServiceImpl<YqmStoreCombinationMapper, YqmStoreCombination> implements YqmStoreCombinationService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private YqmStorePinkMapper yqmStorePinkMapper;
    @Autowired
    private YqmStoreVisitMapper yqmStoreVisitMapper;

    @Autowired
    private YqmStoreCombinationMapper yqmStoreCombinationMapper;
    @Autowired
    private YqmStoreProductReplyService replyService;
    @Autowired
    private YqmStorePinkService storePinkService;
    @Autowired
    private YqmStoreProductAttrService yqmStoreProductAttrService;
    @Autowired
    private YqmStoreProductAttrValueService yqmStoreProductAttrValueService;
    @Autowired
    private YqmShippingTemplatesService shippingTemplatesService;

    @Autowired
    private YqmStoreProductService storeProductService;



    /**
     * 获取拼团详情
     * @param id 拼团产品id
     * @param uid uid
     * @return StoreCombinationVo
     */
    @Override
    public StoreCombinationVo getDetail(Long id, Long uid) {
        Date now = new Date();
        YqmStoreCombination storeCombination = this
                .lambdaQuery().eq(YqmStoreCombination::getId,id)
                .eq(YqmStoreCombination::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .le(YqmStoreCombination::getStartTime,now)
                .ge(YqmStoreCombination::getStopTime,now)
                .one();
        if(storeCombination == null){
            throw new YqmShopException("拼团不存在或已下架");
        }
        //获取商品sku
        Map<String, Object> returnMap = yqmStoreProductAttrService.getProductAttrDetail(storeCombination.getProductId());

        YqmStoreCombinationQueryVo storeCombinationQueryVo = generator.convert(storeCombination,
                YqmStoreCombinationQueryVo.class);

        StoreCombinationVo storeCombinationVo = new StoreCombinationVo();

        storeCombinationVo.setStoreInfo(storeCombinationQueryVo);

        //评价
        storeCombinationVo.setReply(replyService
                .getReply(storeCombinationQueryVo.getProductId()));
        int replyCount = replyService.productReplyCount(storeCombinationQueryVo.getProductId());
        //总条数
        storeCombinationVo.setReplyCount(replyCount);
        //好评比例
        storeCombinationVo.setReplyChance(replyService.replyPer(storeCombinationQueryVo.getProductId()));

        //获取运费模板名称
        String storeFreePostage = RedisUtil.get("store_free_postage");
        String tempName = "";
        if(StrUtil.isBlank(storeFreePostage)
                || !NumberUtil.isNumber(storeFreePostage)
                || Integer.valueOf(storeFreePostage) == 0){
            tempName = "全国包邮";
        }else{
            YqmShippingTemplates shippingTemplates = shippingTemplatesService.getById(storeCombination.getTempId());
            if(ObjectUtil.isNotNull(shippingTemplates)){
                tempName = shippingTemplates.getName();
            }else {
                throw new BadRequestException("请配置运费模板");
            }

        }
        storeCombinationVo.setTempName(tempName);

        PinkAllDto pinkAllDto = storePinkService.getPinkAll(id);
        storeCombinationVo.setPindAll(pinkAllDto.getPindAll());
        storeCombinationVo.setPink(pinkAllDto.getList());
        storeCombinationVo.setPinkOkList(storePinkService.getPinkOkList(uid));
        storeCombinationVo.setPinkOkSum(storePinkService.getPinkOkSumTotalNum());
        storeCombinationVo.setProductAttr((List<YqmStoreProductAttrQueryVo>)returnMap.get("productAttr"));
        storeCombinationVo.setProductValue((Map<String, YqmStoreProductAttrValue>)returnMap.get("productValue"));
        return storeCombinationVo;
    }

    /**
     * 拼团列表
     * @param page page
     * @param limit limit
     * @return list
     */
    @Override
    public CombinationQueryVo getList(int page, int limit) {
        CombinationQueryVo combinationQueryVo = new CombinationQueryVo();
        Date nowTime = new Date();
        Page<YqmStoreCombination> pageModel = new Page<>(page, limit);
       LambdaQueryWrapper<YqmStoreCombination> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(YqmStoreCombination::getIsShow,1)
                .le(YqmStoreCombination::getStartTime,nowTime)
                .ge(YqmStoreCombination::getStopTime,nowTime)
                .orderByDesc(YqmStoreCombination::getSort);
        IPage<YqmStoreCombination> yqmStoreCombinationIPage = yqmStoreCombinationMapper.selectPage(pageModel, wrapper);

        List<YqmStoreCombinationQueryVo> collect = yqmStoreCombinationIPage.getRecords().stream().map(i -> {
            YqmStoreCombinationQueryVo yqmStoreCombinationQueryVo = new YqmStoreCombinationQueryVo();
            BeanUtils.copyProperties(i, yqmStoreCombinationQueryVo);
            return yqmStoreCombinationQueryVo;
        }).collect(Collectors.toList());
        combinationQueryVo.setStoreCombinationQueryVos(collect);
        combinationQueryVo.setLastPage(yqmStoreCombinationIPage.getPages());
        return combinationQueryVo;
    }


    //=======================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmStoreCombinationQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCombination> page = new PageInfo<>(queryAll(criteria));

        List<YqmStoreCombinationDto> combinationDTOS = generator.convert(page.getList(),YqmStoreCombinationDto.class);
        for (YqmStoreCombinationDto combinationDTO : combinationDTOS) {
            //参与人数
            combinationDTO.setCountPeopleAll(yqmStorePinkMapper.selectCount(new LambdaQueryWrapper<YqmStorePink>()
                    .eq(YqmStorePink::getCid,combinationDTO.getId())));

            //成团人数
            combinationDTO.setCountPeoplePink(yqmStorePinkMapper.selectCount(new LambdaQueryWrapper<YqmStorePink>()
                    .eq(YqmStorePink::getCid,combinationDTO.getId())
                    .eq(YqmStorePink::getKId,0)));//团长
            //获取查看拼团产品人数
            combinationDTO.setCountPeopleBrowse(yqmStoreVisitMapper.selectCount(new LambdaQueryWrapper<YqmStoreVisit>()
                    .eq(YqmStoreVisit::getProductId,combinationDTO.getId())
                    .eq(YqmStoreVisit::getProductType, ProductTypeEnum.COMBINATION.getValue())));
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content",combinationDTOS);
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmStoreCombination> queryAll(YqmStoreCombinationQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreCombination.class, criteria));
    }


    @Override
    public void download(List<YqmStoreCombinationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCombinationDto yqmStoreCombination : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("商品id", yqmStoreCombination.getProductId());
            map.put("推荐图", yqmStoreCombination.getImage());
            map.put("轮播图", yqmStoreCombination.getImages());
            map.put("活动标题", yqmStoreCombination.getTitle());
            map.put("参团人数", yqmStoreCombination.getPeople());
            map.put("简介", yqmStoreCombination.getInfo());
            map.put("价格", yqmStoreCombination.getPrice());
            map.put("排序", yqmStoreCombination.getSort());
            map.put("销量", yqmStoreCombination.getSales());
            map.put("库存", yqmStoreCombination.getStock());
            map.put("推荐", yqmStoreCombination.getIsHost());
            map.put("产品状态", yqmStoreCombination.getIsShow());
            map.put(" combination",  yqmStoreCombination.getCombination());
            map.put("商户是否可用1可用0不可用", yqmStoreCombination.getMerUse());
            map.put("是否包邮1是0否", yqmStoreCombination.getIsPostage());
            map.put("邮费", yqmStoreCombination.getPostage());
            map.put("拼团内容", yqmStoreCombination.getDescription());
            map.put("拼团开始时间", yqmStoreCombination.getStartTime());
            map.put("拼团结束时间", yqmStoreCombination.getStopTime());
            map.put("拼团订单有效时间", yqmStoreCombination.getEffectiveTime());
            map.put("拼团产品成本", yqmStoreCombination.getCost());
            map.put("浏览量", yqmStoreCombination.getBrowse());
            map.put("单位名", yqmStoreCombination.getUnitName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 修改状态
     * @param id 拼团产品id
     * @param status ShopCommonEnum
     */
    @Override
    public void onSale(Long id, Integer status) {
        if(ShopCommonEnum.SHOW_1.getValue().equals(status)){
            status = ShopCommonEnum.SHOW_0.getValue();
        }else{
            status = ShopCommonEnum.SHOW_1.getValue();
        }
        YqmStoreCombination yqmStoreCombination = new YqmStoreCombination();
        yqmStoreCombination.setIsShow(status);
        yqmStoreCombination.setId(id);
        this.saveOrUpdate(yqmStoreCombination);
    }

    @Override
    public boolean saveCombination(YqmStoreCombinationDto resources) {
        ProductResultDto resultDTO = this.computedProduct(resources.getAttrs());

        //添加商品
        YqmStoreCombination yqmStoreCombination = new YqmStoreCombination();
        BeanUtil.copyProperties(resources,yqmStoreCombination,"images");
        if(resources.getImages().isEmpty()) {
            throw new YqmShopException("请上传轮播图");
        }

        yqmStoreCombination.setPrice(BigDecimal.valueOf(resultDTO.getMinPrice()));
        yqmStoreCombination.setProductPrice(BigDecimal.valueOf(resultDTO.getMinOtPrice()));
        yqmStoreCombination.setCost(resultDTO.getMinCost().intValue());
        yqmStoreCombination.setStock(resultDTO.getStock());
        yqmStoreCombination.setImages(String.join(",", resources.getImages()));
        this.saveOrUpdate(yqmStoreCombination);

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
                .map(ProductFormatDto::getPinkPrice)
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
                .map(ProductFormatDto::getPinkStock)
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

    /**
     * mapTobean
     * @param listMap listMap
     * @return list
     */
    private List<ProductFormatDto> ListMapToListBean(List<Map<String, Object>> listMap){
        List<ProductFormatDto> list = new ArrayList<>();
        // 循环遍历出map对象
        for (Map<String, Object> m : listMap) {
            list.add(BeanUtil.mapToBean(m,ProductFormatDto.class,true));
        }
        return list;
    }
}
