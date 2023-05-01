/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.message.redis.listener;


import cn.hutool.core.util.StrUtil;
import com.yqm.constant.ShopConstants;
import com.yqm.enums.OrderInfoEnum;
import com.yqm.message.redis.config.RedisConfigProperties;
import com.yqm.modules.activity.domain.YqmStorePink;
import com.yqm.modules.activity.service.YqmStorePinkService;
import com.yqm.modules.order.domain.YqmStoreOrder;
import com.yqm.modules.order.service.YqmStoreOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * redis过期监听
 * @author weiximei
 * @since 2020-02-27
 */
@Component
@Slf4j
public class RedisKeyExpirationListener implements MessageListener {

	private RedisTemplate<String, String> redisTemplate;
	private RedisConfigProperties redisConfigProperties;
	private YqmStoreOrderService storeOrderService;
	private YqmStorePinkService storePinkService;

	public RedisKeyExpirationListener(RedisTemplate<String, String> redisTemplate,
                                      RedisConfigProperties redisConfigProperties,
									  YqmStoreOrderService storeOrderService,
									  YqmStorePinkService storePinkService){
		this.redisTemplate = redisTemplate;
		this.redisConfigProperties = redisConfigProperties;
		this.storeOrderService = storeOrderService;
		this.storePinkService = storePinkService;
	}
	@Override
	public void onMessage(Message message, byte[] bytes) {
		RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
		String channel = String.valueOf(serializer.deserialize(message.getChannel()));
		String body = String.valueOf(serializer.deserialize(message.getBody()));
		//key过期监听
		if(StrUtil.format("__keyevent@{}__:expired", redisConfigProperties.getDatabase()).equals(channel)){
			//订单自动取消
			if(body.contains(ShopConstants.REDIS_ORDER_OUTTIME_UNPAY)) {
				body = body.replace(ShopConstants.REDIS_ORDER_OUTTIME_UNPAY, "");
				log.info("body:{}",body);
				String orderId = body;
				YqmStoreOrder order = storeOrderService.getOne(new LambdaQueryWrapper<YqmStoreOrder>()
						.eq(YqmStoreOrder::getId, orderId)
                        .eq(YqmStoreOrder::getPaid, OrderInfoEnum.PAY_STATUS_0.getValue()));
				//只有待支付的订单能取消
				if(order != null){
					storeOrderService.cancelOrder(order.getOrderId(),null);
					log.info("订单id:{},未在规定时间支付取消成功",body);
				}
			}
			//订单自动收货
			if(body.contains(ShopConstants.REDIS_ORDER_OUTTIME_UNCONFIRM)) {
				body = body.replace(ShopConstants.REDIS_ORDER_OUTTIME_UNCONFIRM, "");
				log.info("body:{}",body);
				String orderId = body;
				YqmStoreOrder order = storeOrderService.getOne(new LambdaQueryWrapper<YqmStoreOrder>()
                        .eq(YqmStoreOrder::getId, orderId)
						.eq(YqmStoreOrder::getPaid,OrderInfoEnum.PAY_STATUS_1.getValue())
                        .eq(YqmStoreOrder::getStatus,OrderInfoEnum.STATUS_1.getValue()));

				//只有待收货的订单能收货
				if(order != null){
					storeOrderService.takeOrder(order.getOrderId(),null);
					log.info("订单id:{},自动收货成功",body);
				}
			}

			//拼团过期取消
			if(body.contains(ShopConstants.REDIS_PINK_CANCEL_KEY)) {
				body = body.replace(ShopConstants.REDIS_PINK_CANCEL_KEY, "");
				log.info("body:{}",body);
				String pinkId = body;
				YqmStorePink storePink = storePinkService.getOne(Wrappers.<YqmStorePink>lambdaQuery()
						.eq(YqmStorePink::getId,pinkId)
						.eq(YqmStorePink::getStatus,OrderInfoEnum.PINK_STATUS_1.getValue())
                        .eq(YqmStorePink::getIsRefund,OrderInfoEnum.PINK_REFUND_STATUS_0.getValue()));

				//取消拼团
				if(storePink != null){
					storePinkService.removePink(storePink.getUid(),storePink.getCid(),storePink.getId());
					log.info("拼团订单id:{},未在规定时间完成取消成功",body);
				}
			}
		}

	}
}
