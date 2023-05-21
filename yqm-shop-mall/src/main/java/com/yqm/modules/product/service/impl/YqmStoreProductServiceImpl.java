/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.product.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.qiniu.util.StringUtils;
import com.yqm.api.YqmShopException;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.constant.ShopConstants;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.CommonEnum;
import com.yqm.enums.ProductEnum;
import com.yqm.enums.ProductTypeEnum;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.enums.SortEnum;
import com.yqm.enums.SpecTypeEnum;
import com.yqm.exception.BadRequestException;
import com.yqm.exception.ErrorRequestException;
import com.yqm.modules.category.service.YqmStoreCategoryService;
import com.yqm.modules.product.domain.YqmStoreProduct;
import com.yqm.modules.product.domain.YqmStoreProductAttrValue;
import com.yqm.modules.product.domain.YqmStoreProductRelation;
import com.yqm.modules.product.param.YqmStoreProductQueryParam;
import com.yqm.modules.product.service.YqmStoreProductAttrService;
import com.yqm.modules.product.service.YqmStoreProductAttrValueService;
import com.yqm.modules.product.service.YqmStoreProductRelationService;
import com.yqm.modules.product.service.YqmStoreProductReplyService;
import com.yqm.modules.product.service.YqmStoreProductService;
import com.yqm.modules.product.service.dto.DetailDto;
import com.yqm.modules.product.service.dto.FromatDetailDto;
import com.yqm.modules.product.service.dto.ProductFormatDto;
import com.yqm.modules.product.service.dto.ProductResultDto;
import com.yqm.modules.product.service.dto.StoreProductDto;
import com.yqm.modules.product.service.dto.YqmStoreProductDto;
import com.yqm.modules.product.service.dto.YqmStoreProductQueryCriteria;
import com.yqm.modules.product.service.mapper.StoreProductMapper;
import com.yqm.modules.product.vo.ProductVo;
import com.yqm.modules.product.vo.YqmStoreProductAttrQueryVo;
import com.yqm.modules.product.vo.YqmStoreProductQueryVo;
import com.yqm.modules.product.vo.YqmStoreProductReplyQueryVo;
import com.yqm.modules.shop.service.YqmSystemStoreService;
import com.yqm.modules.template.domain.YqmShippingTemplates;
import com.yqm.modules.template.service.YqmShippingTemplatesService;
import com.yqm.modules.user.service.YqmUserService;
import com.yqm.utils.FileUtil;
import com.yqm.utils.RedisUtil;
import com.yqm.utils.RegexUtil;
import com.yqm.utils.ShopKeyUtils;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;


/**
 * @author weiximei
 * @date 2020-05-12
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class YqmStoreProductServiceImpl extends BaseServiceImpl<StoreProductMapper, YqmStoreProduct> implements YqmStoreProductService {

	@Autowired
	private IGenerator generator;
	@Autowired
	private StoreProductMapper storeProductMapper;
	@Autowired
	private YqmStoreCategoryService yqmStoreCategoryService;
	@Autowired
	private YqmStoreProductAttrService yqmStoreProductAttrService;
	@Autowired
	private YqmStoreProductAttrValueService yqmStoreProductAttrValueService;
	@Autowired
	private YqmUserService userService;
	@Autowired
	private YqmStoreProductReplyService replyService;
	@Autowired
	private YqmStoreProductRelationService relationService;
	@Autowired
	private YqmSystemStoreService systemStoreService;
	@Autowired
	private YqmShippingTemplatesService shippingTemplatesService;


	/**
	 * 增加库存 减少销量
	 *
	 * @param num       数量
	 * @param productId 商品id
	 * @param unique    sku唯一值
	 */
	@Override
	public void incProductStock(Integer num, Long productId, String unique, Long activityId, String type) {
		//处理属性sku
		if (CharSequenceUtil.isNotEmpty(unique)) {
			yqmStoreProductAttrService.incProductAttrStock(num, productId, unique, type);
		}
		//先处理商品库存，活动商品也要处理，因为共享库存
		storeProductMapper.incStockDecSales(num, productId);
		//处理商品外层显示的库存
		if (ProductTypeEnum.COMBINATION.getValue().equals(type)) {
			storeProductMapper.incCombinationStockIncSales(num, productId, activityId);
		} else if (ProductTypeEnum.SECKILL.getValue().equals(type)) {
			storeProductMapper.incSeckillStockIncSales(num, productId, activityId);
		}
		//todo 处理砍价商品库存
	}

	/**
	 * 减少库存与增加销量
	 *
	 * @param num       数量
	 * @param productId 商品id
	 * @param unique    sku
	 */
	@Override
	public void decProductStock(int num, Long productId, String unique, Long activityId, String type) {
		//处理属性sku
		if (CharSequenceUtil.isNotEmpty(unique)) {
			yqmStoreProductAttrService.decProductAttrStock(num, productId, unique, type);
		}
		//先处理商品库存，活动商品也要处理，因为共享库存
		int product = storeProductMapper.decStockIncSales(num, productId);
		if (product == 0) {
			throw new YqmShopException("共享商品库存不足");
		}
		//处理商品外层显示的库存
		if (ProductTypeEnum.COMBINATION.getValue().equals(type)) {
			int combinationRes = storeProductMapper.decCombinationStockIncSales(num, productId, activityId);
			if (combinationRes == 0) {
				throw new YqmShopException("拼团商品库存不足");
			}
		} else if (ProductTypeEnum.SECKILL.getValue().equals(type)) {
			int seckillRes = storeProductMapper.decSeckillStockIncSales(num, productId, activityId);
			if (seckillRes == 0) {
				throw new YqmShopException("秒杀商品库存不足");
			}
		}
		//todo 处理砍价库存

	}


	@Override
	public YqmStoreProduct getProductInfo(Long id) {
		LambdaQueryWrapper<YqmStoreProduct> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(YqmStoreProduct::getIsShow, 1).eq(YqmStoreProduct::getId, id);
		YqmStoreProduct storeProduct = this.baseMapper.selectOne(wrapper);
		if (ObjectUtil.isNull(storeProduct)) {
			throw new ErrorRequestException("商品不存在或已下架");
		}

		return storeProduct;
	}


	/**
	 * 获取单个商品
	 *
	 * @param id 商品id
	 * @return YqmStoreProductQueryVo
	 */
	@Override
	public YqmStoreProductQueryVo getStoreProductById(Long id) {
		return generator.convert(this.baseMapper.selectById(id), YqmStoreProductQueryVo.class);
	}


	/**
	 * 返回普通商品库存
	 *
	 * @param productId 商品id
	 * @param unique    sku唯一值
	 * @return int
	 */
	@Override
	public int getProductStock(Long productId, String unique, String type) {
		YqmStoreProductAttrValue storeProductAttrValue = yqmStoreProductAttrValueService
				.getOne(Wrappers.<YqmStoreProductAttrValue>lambdaQuery()
						.eq(YqmStoreProductAttrValue::getUnique, unique)
						.eq(YqmStoreProductAttrValue::getProductId, productId));

		if (storeProductAttrValue == null) {
			return 0;
		}
		if (ProductTypeEnum.PINK.getValue().equals(type)) {
			return storeProductAttrValue.getPinkStock();
		} else if (ProductTypeEnum.SECKILL.getValue().equals(type)) {
			return storeProductAttrValue.getSeckillStock();
		}
		return storeProductAttrValue.getStock();

	}


	/**
	 * 商品列表
	 *
	 * @param productQueryParam YqmStoreProductQueryParam
	 * @return list
	 */
	@Override
	public List<YqmStoreProductQueryVo> getGoodsList(YqmStoreProductQueryParam productQueryParam) {

		LambdaQueryWrapper<YqmStoreProduct> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(YqmStoreProduct::getIsShow, CommonEnum.SHOW_STATUS_1.getValue());
		wrapper.eq(YqmStoreProduct::getIsDel, CommonEnum.DEL_STATUS_0.getValue());
		//        wrapper.eq(YqmStoreProduct::getIsIntegral, CommonEnum.SHOW_STATUS_1.getValue());

		if (Objects.nonNull(productQueryParam.getIsIntegral())) {
			wrapper.eq(YqmStoreProduct::getIsIntegral, productQueryParam.getIsIntegral());
		}
		//多字段模糊查询分类搜索
		if (CharSequenceUtil.isNotBlank(productQueryParam.getSid()) &&
				!ShopConstants.YQM_SHOP_ZERO.equals(productQueryParam.getSid())) {
			wrapper.eq(YqmStoreProduct::getCateId, productQueryParam.getSid());
		}
		//关键字搜索
		if (CharSequenceUtil.isNotEmpty(productQueryParam.getKeyword())) {
			wrapper.and(wrapper1 -> {
				wrapper1.or();
				wrapper1.like(YqmStoreProduct::getStoreName, productQueryParam.getKeyword());
				wrapper1.or();
				wrapper1.like(YqmStoreProduct::getStoreInfo, productQueryParam.getKeyword());
				wrapper1.or();
				wrapper1.like(YqmStoreProduct::getKeyword, productQueryParam.getKeyword());
			});
		}
		//新品搜索
		if (CharSequenceUtil.isNotBlank(productQueryParam.getNews()) &&
				!ShopConstants.YQM_SHOP_ZERO.equals(productQueryParam.getNews())) {
			wrapper.eq(YqmStoreProduct::getIsNew, ShopCommonEnum.IS_NEW_1.getValue());
		}

		//销量排序
		if (SortEnum.DESC.getValue().equals(productQueryParam.getSalesOrder())) {
			wrapper.orderByDesc(YqmStoreProduct::getSales);
		} else if (SortEnum.ASC.getValue().equals(productQueryParam.getSalesOrder())) {
			wrapper.orderByAsc(YqmStoreProduct::getSales);
		}

		//价格排序
		if (SortEnum.DESC.getValue().equals(productQueryParam.getPriceOrder())) {
			wrapper.orderByDesc(YqmStoreProduct::getPrice);
		} else if (SortEnum.ASC.getValue().equals(productQueryParam.getPriceOrder())) {
			wrapper.orderByAsc(YqmStoreProduct::getPrice);
		}

		//无其他排序条件时,防止因为商品排序导致商品重复
		if (StringUtils.isNullOrEmpty(productQueryParam.getPriceOrder()) && StringUtils.isNullOrEmpty(productQueryParam.getSalesOrder())) {
			wrapper.orderByDesc(YqmStoreProduct::getId);
			wrapper.orderByDesc(YqmStoreProduct::getSort);
		}
		Page<YqmStoreProduct> pageModel = new Page<>(productQueryParam.getPage(),
				productQueryParam.getLimit());

		IPage<YqmStoreProduct> pageList = storeProductMapper.selectPage(pageModel, wrapper);

		List<YqmStoreProductQueryVo> list = generator.convert(pageList.getRecords(), YqmStoreProductQueryVo.class);

		return list;
	}

	/**
	 * 商品详情
	 *
	 * @param id        商品id
	 * @param uid       用户id
	 * @param latitude  纬度
	 * @param longitude 经度
	 * @return ProductVo
	 */
	@Override
	public ProductVo goodsDetail(Long id, Long uid, String latitude, String longitude) {
		LambdaQueryWrapper<YqmStoreProduct> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(YqmStoreProduct::getIsShow, ShopCommonEnum.SHOW_1.getValue())
		.eq(YqmStoreProduct::getId, id);
		YqmStoreProduct storeProduct = storeProductMapper.selectOne(wrapper);
		if (ObjectUtil.isNull(storeProduct)) {
			throw new ErrorRequestException("商品不存在或已下架");
		}

		//获取商品sku
		Map<String, Object> returnMap = yqmStoreProductAttrService.getProductAttrDetail(id);
		ProductVo productVo = new ProductVo();
		YqmStoreProductQueryVo storeProductQueryVo = generator.convert(storeProduct, YqmStoreProductQueryVo.class);

		//设置销量
		storeProductQueryVo.setSales(storeProductQueryVo.getSales() + storeProductQueryVo.getFicti());

		if (uid.longValue() > 0) {
			//设置VIP价格
			double vipPrice = userService.setLevelPrice(
					storeProductQueryVo.getPrice().doubleValue(), uid);
			storeProductQueryVo.setVipPrice(BigDecimal.valueOf(vipPrice));

			//收藏
			boolean isCollect = relationService.isProductRelation(id, uid);
			storeProductQueryVo.setUserCollect(isCollect);
		}
		//总条数
		int totalCount = replyService.productReplyCount(id);
		productVo.setReplyCount(totalCount);

		//评价
		YqmStoreProductReplyQueryVo storeProductReplyQueryVo = replyService.getReply(id);
		productVo.setReply(storeProductReplyQueryVo);

		//好评比例
		String replyPer = replyService.replyPer(id);
		productVo.setReplyChance(replyPer);

		//获取运费模板名称
		String storeFreePostage = RedisUtil.get("store_free_postage");
		String tempName = "";
		if (CharSequenceUtil.isBlank(storeFreePostage)
				|| !NumberUtil.isNumber(storeFreePostage)
				|| Integer.valueOf(storeFreePostage) == 0) {
			tempName = "全国包邮";
		} else {
			YqmShippingTemplates shippingTemplates = shippingTemplatesService.getById(storeProduct.getTempId());
			if (ObjectUtil.isNotNull(shippingTemplates)) {
				tempName = shippingTemplates.getName();
			} else {
				throw new BadRequestException("请配置运费模板");
			}

		}
		productVo.setTempName(tempName);

		//设置商品相关信息
		productVo.setStoreInfo(storeProductQueryVo);
		productVo.setProductAttr((List<YqmStoreProductAttrQueryVo>) returnMap.get("productAttr"));
		productVo.setProductValue((Map<String, YqmStoreProductAttrValue>) returnMap.get("productValue"));


		//门店
		productVo.setSystemStore(systemStoreService.getStoreInfo(latitude, longitude));
		productVo.setMapKey(RedisUtil.get(ShopKeyUtils.getTengXunMapKey()));
		if (uid.longValue() > 0) {
			//添加足迹
			YqmStoreProductRelation foot = relationService.getOne(new LambdaQueryWrapper<YqmStoreProductRelation>()
					.eq(YqmStoreProductRelation::getUid, uid)
					.eq(YqmStoreProductRelation::getProductId, storeProductQueryVo.getId())
					.eq(YqmStoreProductRelation::getType, "foot"));

			if (ObjectUtil.isNotNull(foot)) {
				foot.setCreateTime(new Date());
				relationService.saveOrUpdate(foot);
			} else {
				YqmStoreProductRelation storeProductRelation = new YqmStoreProductRelation();
				storeProductRelation.setProductId(storeProductQueryVo.getId());
				storeProductRelation.setUid(uid);
				storeProductRelation.setCreateTime(new Date());
				storeProductRelation.setType("foot");
				relationService.save(storeProductRelation);
			}
		}

		return productVo;
	}

	/**
	 * 商品浏览量
	 *
	 * @param productId
	 */
	@Override
	public void incBrowseNum(Long productId) {
		storeProductMapper.incBrowseNum(productId);
	}


	/**
	 * 商品列表
	 *
	 * @param page  页码
	 * @param limit 条数
	 * @param order ProductEnum
	 * @return List
	 */
	@Override
	public List<YqmStoreProductQueryVo> getList(int page, int limit, int order) {

		LambdaQueryWrapper<YqmStoreProduct> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(YqmStoreProduct::getIsShow, ShopCommonEnum.SHOW_1.getValue())
		.eq(YqmStoreProduct::getIsDel,ShopCommonEnum.DELETE_0.getValue())
		.orderByDesc(YqmStoreProduct::getSort);
		wrapper.eq(YqmStoreProduct::getIsIntegral,0);
		// order
		switch (ProductEnum.toType(order)) {
			//精品推荐
			case TYPE_1:
				wrapper.eq(YqmStoreProduct::getIsBest,
						ShopCommonEnum.IS_STATUS_1.getValue());
				break;
				//首发新品
			case TYPE_3:
				wrapper.eq(YqmStoreProduct::getIsNew,
						ShopCommonEnum.IS_STATUS_1.getValue());
				break;
				// 猜你喜欢
			case TYPE_4:
				wrapper.eq(YqmStoreProduct::getIsBenefit,
						ShopCommonEnum.IS_STATUS_1.getValue());
				break;
				// 热门榜单
			case TYPE_2:
				wrapper.eq(YqmStoreProduct::getIsHot,
						ShopCommonEnum.IS_STATUS_1.getValue());
				break;
			default:
		}
		Page<YqmStoreProduct> pageModel = new Page<>(page, limit);

		IPage<YqmStoreProduct> pageList = storeProductMapper.selectPage(pageModel, wrapper);

		return generator.convert(pageList.getRecords(), YqmStoreProductQueryVo.class);
	}


	//============ 分割线================//


	@Override
	public Map<String, Object> queryAll(YqmStoreProductQueryCriteria criteria, Pageable pageable) {
		getPage(pageable);
		PageInfo<YqmStoreProduct> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);
		map.put("content", generator.convert(page.getList(), YqmStoreProductDto.class));
		map.put("totalElements", page.getTotal());
		return map;
	}


	@Override
	public List<YqmStoreProduct> queryAll(YqmStoreProductQueryCriteria criteria) {
		List<YqmStoreProduct> yqmStoreProductList = baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreProduct.class, criteria));
		yqmStoreProductList.forEach(yqmStoreProduct -> {
			yqmStoreProduct.setStoreCategory(yqmStoreCategoryService.getById(yqmStoreProduct.getCateId()));
		});
		return yqmStoreProductList;
	}


	@Override
	public void download(List<YqmStoreProductDto> all, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (YqmStoreProductDto yqmStoreProduct : all) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("商户Id(0为总后台管理员创建,不为0的时候是商户后台创建)", yqmStoreProduct.getMerId());
			map.put("商品图片", yqmStoreProduct.getImage());
			map.put("轮播图", yqmStoreProduct.getSliderImage());
			map.put("商品名称", yqmStoreProduct.getStoreName());
			map.put("商品简介", yqmStoreProduct.getStoreInfo());
			map.put("关键字", yqmStoreProduct.getKeyword());
			map.put("产品条码（一维码）", yqmStoreProduct.getBarCode());
			map.put("分类id", yqmStoreProduct.getCateId());
			map.put("商品价格", yqmStoreProduct.getPrice());
			map.put("会员价格", yqmStoreProduct.getVipPrice());
			map.put("市场价", yqmStoreProduct.getOtPrice());
			map.put("邮费", yqmStoreProduct.getPostage());
			map.put("单位名", yqmStoreProduct.getUnitName());
			map.put("排序", yqmStoreProduct.getSort());
			map.put("销量", yqmStoreProduct.getSales());
			map.put("库存", yqmStoreProduct.getStock());
			map.put("状态（0：未上架，1：上架）", yqmStoreProduct.getIsShow());
			map.put("是否热卖", yqmStoreProduct.getIsHot());
			map.put("是否优惠", yqmStoreProduct.getIsBenefit());
			map.put("是否精品", yqmStoreProduct.getIsBest());
			map.put("是否新品", yqmStoreProduct.getIsNew());
			map.put("产品描述", yqmStoreProduct.getDescription());
			map.put("添加时间", yqmStoreProduct.getAddTime());
			map.put("是否包邮", yqmStoreProduct.getIsPostage());
			map.put("是否删除", yqmStoreProduct.getIsDel());
			map.put("商户是否代理 0不可代理1可代理", yqmStoreProduct.getMerUse());
			map.put("获得积分", yqmStoreProduct.getGiveIntegral());
			map.put("成本价", yqmStoreProduct.getCost());
			map.put("秒杀状态 0 未开启 1已开启", yqmStoreProduct.getIsSeckill());
			map.put("砍价状态 0未开启 1开启", yqmStoreProduct.getIsBargain());
			map.put("是否优品推荐", yqmStoreProduct.getIsGood());
			map.put("虚拟销量", yqmStoreProduct.getFicti());
			map.put("浏览量", yqmStoreProduct.getBrowse());
			map.put("产品二维码地址(用户小程序海报)", yqmStoreProduct.getCodePath());
			map.put("淘宝京东1688类型", yqmStoreProduct.getSoureLink());
			list.add(map);
		}
		FileUtil.downloadExcel(list, response);
	}


	/**
	 * 商品上架下架
	 *
	 * @param id     商品id
	 * @param status ShopCommonEnum
	 */
	@Override
	public void onSale(Long id, Integer status) {
		if (ShopCommonEnum.SHOW_1.getValue().equals(status)) {
			status = ShopCommonEnum.SHOW_0.getValue();
		} else {
			status = ShopCommonEnum.SHOW_1.getValue();
		}
		storeProductMapper.updateOnsale(status, id);
	}


	/**
	 * 新增/保存商品
	 *
	 * @param storeProductDto 商品
	 */
	@Override
	public void insertAndEditYqmStoreProduct(StoreProductDto storeProductDto) {
		storeProductDto.setDescription(RegexUtil.converProductDescription(storeProductDto.getDescription()));
		ProductResultDto resultDTO = this.computedProduct(storeProductDto.getAttrs());

		//添加商品
		YqmStoreProduct yqmStoreProduct = new YqmStoreProduct();
		BeanUtil.copyProperties(storeProductDto, yqmStoreProduct, "sliderImage");
		if (storeProductDto.getSliderImage().isEmpty()) {
			throw new YqmShopException("请上传轮播图");
		}

		yqmStoreProduct.setPrice(BigDecimal.valueOf(resultDTO.getMinPrice()));
		yqmStoreProduct.setOtPrice(BigDecimal.valueOf(resultDTO.getMinOtPrice()));
		yqmStoreProduct.setCost(BigDecimal.valueOf(resultDTO.getMinCost()));
		yqmStoreProduct.setIntegral(resultDTO.getMinIntegral());
		yqmStoreProduct.setStock(resultDTO.getStock());
		yqmStoreProduct.setSliderImage(String.join(",", storeProductDto.getSliderImage()));

		if (storeProductDto.getId() != null) {
			//清空商品转发图
			deleteForwardImg(storeProductDto.getId());
		}

		this.saveOrUpdate(yqmStoreProduct);

		//属性处理
		//处理单sKu
		if (SpecTypeEnum.TYPE_0.getValue().equals(storeProductDto.getSpecType())) {
			FromatDetailDto fromatDetailDto = FromatDetailDto.builder()
					.value("规格")
					.detailValue("")
					.attrHidden("")
					.detail(ListUtil.toList("默认"))
					.build();
			List<ProductFormatDto> attrs = storeProductDto.getAttrs();
			ProductFormatDto productFormatDto = attrs.get(0);
			productFormatDto.setValue1("规格");
			Map<String, String> map = new HashMap<>();
			map.put("规格", "默认");
			productFormatDto.setDetail(map);
			yqmStoreProductAttrService.insertYqmStoreProductAttr(ListUtil.toList(fromatDetailDto),
					ListUtil.toList(productFormatDto), yqmStoreProduct.getId());
		} else {
			yqmStoreProductAttrService.insertYqmStoreProductAttr(storeProductDto.getItems(),
					storeProductDto.getAttrs(), yqmStoreProduct.getId());
		}


	}


	/**
	 * 获取生成的属性
	 *
	 * @param id      商品id
	 * @param jsonStr jsonStr
	 * @return map
	 */
	@Override
	public Map<String, Object> getFormatAttr(Long id, String jsonStr, boolean isActivity) {
		JSONObject jsonObject = JSON.parseObject(jsonStr);
		Map<String, Object> resultMap = new LinkedHashMap<>(3);

		if (jsonObject == null || jsonObject.get("attrs") == null || jsonObject.getJSONArray("attrs").isEmpty()) {
			resultMap.put("attr", new ArrayList<>());
			resultMap.put("value", new ArrayList<>());
			resultMap.put("header", new ArrayList<>());
			return resultMap;
		}


		List<FromatDetailDto> fromatDetailDTOList = JSON.parseArray(jsonObject.get("attrs").toString(),
				FromatDetailDto.class);

		//fromatDetailDTOList
		DetailDto detailDto = this.attrFormat(fromatDetailDTOList);

		List<Map<String, Object>> headerMapList = null;
		List<Map<String, Object>> valueMapList = new ArrayList<>();
		String align = "center";
		Map<String, Object> headerMap = new LinkedHashMap<>();
		for (Map<String, Map<String, String>> map : detailDto.getRes()) {
			Map<String, String> detail = map.get("detail");
			String[] detailArr = detail.values().toArray(new String[]{});
			Arrays.sort(detailArr);

			String sku = String.join(",", detailArr);

			Map<String, Object> valueMap = new LinkedHashMap<>();

			List<String> detailKeys =
					detail.entrySet()
					.stream()
					.map(Map.Entry::getKey)
					.collect(Collectors.toList());

			int i = 0;
			headerMapList = new ArrayList<>();
			for (String title : detailKeys) {
				headerMap.put("title", title);
				headerMap.put("minWidth", "130");
				headerMap.put("align", align);
				headerMap.put("key", "value" + (i + 1));
				headerMap.put("slot", "value" + (i + 1));
				headerMapList.add(ObjectUtil.clone(headerMap));
				i++;
			}

			String[] detailValues = detail.values().toArray(new String[]{});
			for (int j = 0; j < detailValues.length; j++) {
				String key = "value" + (j + 1);
				valueMap.put(key, detailValues[j]);
			}
			//            /** 拼团属性对应的金额 */
			//            private BigDecimal pinkPrice;
			//
			//            /** 秒杀属性对应的金额 */
			//            private BigDecimal seckillPrice;
			//            /** 拼团库存属性对应的库存 */
			//            private Integer pinkStock;
			//
			//            private Integer seckillStock;
			valueMap.put("detail", detail);
			valueMap.put("sku", "");
			valueMap.put("pic", "");
			valueMap.put("price", 0);
			valueMap.put("cost", 0);
			valueMap.put("ot_price", 0);
			valueMap.put("stock", 0);
			valueMap.put("bar_code", "");
			valueMap.put("weight", 0);
			valueMap.put("volume", 0);
			valueMap.put("brokerage", 0);
			valueMap.put("brokerage_two", 0);
			valueMap.put("pink_price", 0);
			valueMap.put("seckill_price", 0);
			valueMap.put("pink_stock", 0);
			valueMap.put("seckill_stock", 0);
			valueMap.put("integral", 0);
			if (id > 0) {
				YqmStoreProductAttrValue storeProductAttrValue = yqmStoreProductAttrValueService
						.getOne(Wrappers.<YqmStoreProductAttrValue>lambdaQuery()
								.eq(YqmStoreProductAttrValue::getProductId, id)
								.eq(YqmStoreProductAttrValue::getSku, sku));
				if (storeProductAttrValue != null) {
					valueMap.put("sku",storeProductAttrValue.getSku());
					valueMap.put("pic", storeProductAttrValue.getImage());
					valueMap.put("price", storeProductAttrValue.getPrice());
					valueMap.put("cost", storeProductAttrValue.getCost());
					valueMap.put("ot_price", storeProductAttrValue.getOtPrice());
					valueMap.put("stock", storeProductAttrValue.getStock());
					valueMap.put("bar_code", storeProductAttrValue.getBarCode());
					valueMap.put("weight", storeProductAttrValue.getWeight());
					valueMap.put("volume", storeProductAttrValue.getVolume());
					valueMap.put("brokerage", storeProductAttrValue.getBrokerage());
					valueMap.put("brokerage_two", storeProductAttrValue.getBrokerageTwo());
					valueMap.put("pink_price", storeProductAttrValue.getPinkPrice());
					valueMap.put("seckill_price", storeProductAttrValue.getSeckillPrice());
					valueMap.put("pink_stock", storeProductAttrValue.getPinkStock());
					valueMap.put("seckill_stock", storeProductAttrValue.getSeckillStock());
					valueMap.put("integral", storeProductAttrValue.getIntegral());
				}
			}

			valueMapList.add(ObjectUtil.clone(valueMap));

		}

		this.addMap(headerMap, headerMapList, align, isActivity);


		resultMap.put("attr", fromatDetailDTOList);
		resultMap.put("value", valueMapList);
		resultMap.put("header", headerMapList);

		return resultMap;
	}


	/**
	 * 计算产品数据
	 *
	 * @param attrs attrs
	 * @return ProductResultDto
	 */
	private ProductResultDto computedProduct(List<ProductFormatDto> attrs) {
		//取最小价格
		Double minPrice = attrs
				.stream()
				.map(ProductFormatDto::getPrice)
				.min(Comparator.naturalOrder())
				.orElse(0d);

		//取最小积分
		Integer minIntegral = attrs
				.stream()
				.map(ProductFormatDto::getIntegral)
				.min(Comparator.naturalOrder())
				.orElse(0);

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
				.map(ProductFormatDto::getStock)
				.reduce(Integer::sum)
				.orElse(0);

		if (stock <= 0) {
			throw new YqmShopException("库存不能低于0");
		}

		return ProductResultDto.builder()
				.minPrice(minPrice)
				.minOtPrice(minOtPrice)
				.minCost(minCost)
				.stock(stock)
				.minIntegral(minIntegral)
				.build();
	}

	/**
	 * mapTobean
	 *
	 * @param listMap listMap
	 * @return list
	 */
	private List<ProductFormatDto> ListMapToListBean(List<Map<String, Object>> listMap) {
		List<ProductFormatDto> list = new ArrayList<>();
		// 循环遍历出map对象
		for (Map<String, Object> m : listMap) {
			list.add(BeanUtil.mapToBean(m, ProductFormatDto.class, true));
		}
		return list;
	}

	/**
	 * 增加表头
	 *
	 * @param headerMap     headerMap
	 * @param headerMapList headerMapList
	 * @param align         align
	 */
	private void addMap(Map<String, Object> headerMap, List<Map<String, Object>> headerMapList, String align, boolean isActivity) {
		headerMap.put("title", "图片");
		headerMap.put("slot", "pic");
		headerMap.put("align", align);
		headerMap.put("minWidth", 80);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "售价");
		headerMap.put("slot", "price");
		headerMap.put("align", align);
		headerMap.put("minWidth", 120);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "成本价");
		headerMap.put("slot", "cost");
		headerMap.put("align", align);
		headerMap.put("minWidth", 140);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "原价");
		headerMap.put("slot", "ot_price");
		headerMap.put("align", align);
		headerMap.put("minWidth", 140);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "库存");
		headerMap.put("slot", "stock");
		headerMap.put("align", align);
		headerMap.put("minWidth", 140);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "产品编号");
		headerMap.put("slot", "bar_code");
		headerMap.put("align", align);
		headerMap.put("minWidth", 140);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "重量(KG)");
		headerMap.put("slot", "weight");
		headerMap.put("align", align);
		headerMap.put("minWidth", 140);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "体积(m³)");
		headerMap.put("slot", "volume");
		headerMap.put("align", align);
		headerMap.put("minWidth", 140);
		headerMapList.add(ObjectUtil.clone(headerMap));

		headerMap.put("title", "所需兑换积分");
		headerMap.put("slot", "integral");
		headerMap.put("align", align);
		headerMap.put("minWidth", 140);
		headerMapList.add(ObjectUtil.clone(headerMap));

		if (isActivity) {
			headerMap.put("title", "拼团价");
			headerMap.put("slot", "pink_price");
			headerMap.put("align", align);
			headerMap.put("minWidth", 140);
			headerMapList.add(ObjectUtil.clone(headerMap));

			headerMap.put("title", "拼团活动库存");
			headerMap.put("slot", "pink_stock");
			headerMap.put("align", align);
			headerMap.put("minWidth", 140);
			headerMapList.add(ObjectUtil.clone(headerMap));

			headerMap.put("title", "秒杀价");
			headerMap.put("slot", "seckill_price");
			headerMap.put("align", align);
			headerMap.put("minWidth", 140);
			headerMapList.add(ObjectUtil.clone(headerMap));

			headerMap.put("title", "秒杀活动库存");
			headerMap.put("slot", "seckill_stock");
			headerMap.put("align", align);
			headerMap.put("minWidth", 140);
			headerMapList.add(ObjectUtil.clone(headerMap));
		}

		headerMap.put("title", "操作");
		headerMap.put("slot", "action");
		headerMap.put("align", align);
		headerMap.put("minWidth", 70);
		headerMapList.add(ObjectUtil.clone(headerMap));
	}

	/**
	 * 组合规则属性算法
	 *
	 * @param fromatDetailDTOList
	 * @return DetailDto
	 */
	private DetailDto attrFormat(List<FromatDetailDto> fromatDetailDTOList) {

		List<String> data = new ArrayList<>();
		List<Map<String, Map<String, String>>> res = new ArrayList<>();

		fromatDetailDTOList.stream()
		.map(FromatDetailDto::getDetail)
		.forEach(i -> {
			if (i == null || i.isEmpty()) {
				throw new YqmShopException("请至少添加一个规格值哦");
			}
			String str = ArrayUtil.join(i.toArray(), ",");
			if (str.contains("-")) {
				throw new YqmShopException("规格值里包含'-',请重新添加");
			}
		});

		if (fromatDetailDTOList.size() > 1) {
			for (int i = 0; i < fromatDetailDTOList.size() - 1; i++) {
				if (i == 0) {
					data = fromatDetailDTOList.get(i).getDetail();
				}
				List<String> tmp = new LinkedList<>();
				for (String v : data) {
					for (String g : fromatDetailDTOList.get(i + 1).getDetail()) {
						String rep2 = "";
						if (i == 0) {
							rep2 = fromatDetailDTOList.get(i).getValue() + "_" + v + "-"
									+ fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
						} else {
							rep2 = v + "-"
									+ fromatDetailDTOList.get(i + 1).getValue() + "_" + g;
						}

						tmp.add(rep2);

						if (i == fromatDetailDTOList.size() - 2) {
							Map<String, Map<String, String>> rep4 = new LinkedHashMap<>();
							Map<String, String> reptemp = new LinkedHashMap<>();
							for (String h : Arrays.asList(rep2.split("-"))) {
								List<String> rep3 = Arrays.asList(h.split("_"));
								if (rep3.size() > 1) {
									reptemp.put(rep3.get(0), rep3.get(1));
								} else {
									reptemp.put(rep3.get(0), "");
								}
							}
							rep4.put("detail", reptemp);

							res.add(rep4);
						}
					}

				}

				if (!tmp.isEmpty()) {
					data = tmp;
				}
			}
		} else {
			List<String> dataArr = new ArrayList<>();
			for (FromatDetailDto fromatDetailDTO : fromatDetailDTOList) {
				for (String str : fromatDetailDTO.getDetail()) {
					Map<String, Map<String, String>> map2 = new LinkedHashMap<>();
					dataArr.add(fromatDetailDTO.getValue() + "_" + str);
					Map<String, String> map1 = new LinkedHashMap<>();
					map1.put(fromatDetailDTO.getValue(), str);
					map2.put("detail", map1);
					res.add(map2);
				}
			}
			String s = CharSequenceUtil.join("-", dataArr);
			data.add(s);
		}

		DetailDto detailDto = new DetailDto();
		detailDto.setData(data);
		detailDto.setRes(res);

		return detailDto;
	}

	@Override
	public void deleteForwardImg(Long id) {
		baseMapper.deleteForwardImg(id, "_product_detail_wap");
	}

}
