/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com

 */
package com.yqm.constant;

/**
 * 商城统一常量
 * @author weiximei
 * @since 2020-02-27
 */
public interface ShopConstants {

	/**
	 * 订单自动取消时间（分钟）
	 */
	long ORDER_OUTTIME_UNPAY = 30;
	/**
	 * 订单自动收货时间（天）
	 */
	long ORDER_OUTTIME_UNCONFIRM = 7;
	/**
	 * redis订单未付款key
	 */
	String REDIS_ORDER_OUTTIME_UNPAY = "order:unpay:";
	/**
	 * redis订单收货key
	 */
	String REDIS_ORDER_OUTTIME_UNCONFIRM = "order:unconfirm:";

	/**
	 * redis拼团key
	 */
	String REDIS_PINK_CANCEL_KEY = "pink:cancel:";

	/**
	 * 微信支付service
	 */
	String YQM_SHOP_WEIXIN_PAY_SERVICE = "yqm-shop_weixin_pay_service";

	/**
	 * 微信支付小程序service
	 */
	String YQM_SHOP_WEIXIN_MINI_PAY_SERVICE = "yqm-shop_weixin_mini_pay_service";

	/**
	 * 微信支付app service
	 */
	String YQM_SHOP_WEIXIN_APP_PAY_SERVICE = "yqm-shop_weixin_app_pay_service";

	/**
	 * 微信公众号service
	 */
	String YQM_SHOP_WEIXIN_MP_SERVICE = "yqm-shop_weixin_mp_service";
	/**
	 * 微信小程序service
	 */
	String YQM_SHOP_WEIXIN_MA_SERVICE = "yqm-shop_weixin_ma_service";

	/**
	 * 商城默认密码
	 */
	String YQM_SHOP_DEFAULT_PWD = "123456";

	/**
	 * 商城默认注册图片
	 */
	String YQM_SHOP_DEFAULT_AVATAR = "https://image.dayouqiantu.cn/5e79f6cfd33b6.png";

	/**
	 * 腾讯地图地址解析
	 */
	String QQ_MAP_URL = "https://apis.map.qq.com/ws/geocoder/v1/";

	/**
	 * redis首页键
	 */
	String YQM_SHOP_REDIS_INDEX_KEY = "yqm-shop:index_data";

	/**
	 * 配置列表缓存
	 */
	String YQM_SHOP_REDIS_CONFIG_DATAS = "yqm-shop:config_datas";

	/**
	 * 充值方案
	 */
	String YQM_SHOP_RECHARGE_PRICE_WAYS = "yqm-shop_recharge_price_ways";
	/**
	 * 首页banner
	 */
	String YQM_SHOP_HOME_BANNER = "yqm-shop_home_banner";
	/**
	 * 首页菜单
	 */
	String YQM_SHOP_HOME_MENUS = "yqm-shop_home_menus";
	/**
	 * 首页滚动新闻
	 */
	String YQM_SHOP_HOME_ROLL_NEWS = "yqm-shop_home_roll_news";
	/**
	 * 热门搜索
	 */
	String YQM_SHOP_HOT_SEARCH = "yqm-shop_hot_search";
	/**
	 * 个人中心菜单
	 */
	String YQM_SHOP_MY_MENUES = "yqm-shop_my_menus";
	/**
	 * 秒杀时间段
	 */
	String YQM_SHOP_SECKILL_TIME = "yqm-shop_seckill_time";
	/**
	 * 签到天数
	 */
	String YQM_SHOP_SIGN_DAY_NUM = "yqm-shop_sign_day_num";

	/**
	 * 打印机配置
	 */
	String YQM_SHOP_ORDER_PRINT_COUNT = "order_print_count";
	/**
	 * 飞蛾用户信息
	 */
	String YQM_SHOP_FEI_E_USER = "fei_e_user";
	/**
	 * 飞蛾用户密钥
	 */
	String YQM_SHOP_FEI_E_UKEY= "fei_e_ukey";

	/**
	 * 打印机配置
	 */
	String YQM_SHOP_ORDER_PRINT_COUNT_DETAIL = "order_print_count_detail";

	/**
	 * 短信验证码长度
	 */
	int YQM_SHOP_SMS_SIZE = 6;

	/**
	 * 短信缓存时间
	 */
	long YQM_SHOP_SMS_REDIS_TIME = 600L;

	//零标识
	String YQM_SHOP_ZERO =  "0";

	//业务标识标识
	String YQM_SHOP_ONE =  "1";

	//目前完成任务数量是3
	int TASK_FINISH_COUNT = 3;

	int YQM_SHOP_ONE_NUM = 1;

	String YQM_SHOP_ORDER_CACHE_KEY = "yqm-shop:order";

	long YQM_SHOP_ORDER_CACHE_TIME = 600L;

	String WECHAT_MENUS =  "wechat_menus";

	String YQM_SHOP_EXPRESS_SERVICE = "yqm-shop_express_service";

	String YQM_SHOP_REDIS_SYS_CITY_KEY = "yqm-shop:city_list";

	String YQM_SHOP_REDIS_CITY_KEY = "yqm-shop:city";

	String YQM_SHOP_APP_LOGIN_USER = "app-online-token:";

	String YQM_SHOP_WECHAT_PUSH_REMARK = "yqm-shop为您服务！";

	String DEFAULT_UNI_H5_URL = "https://h5.yqm.co";

	String YQM_SHOP_MINI_SESSION_KET = "yqm-shop:session_key:";

	/**公众号二维码*/
	String WECHAT_FOLLOW_IMG="wechat_follow_img";
	/**后台api地址*/
	String ADMIN_API_URL="admin_api_url";
}
