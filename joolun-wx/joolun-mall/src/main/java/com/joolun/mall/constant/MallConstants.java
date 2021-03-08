package com.joolun.mall.constant;

/**
 * @author
 */
public interface MallConstants {
	/**
	 * 商品规格类型：0统一规格；1多规格
	 */
	String SPU_SPEC_TYPE_0 = "0";
	/**
	 * 商品规格类型：0统一规格；1多规格
	 */
	String SPU_SPEC_TYPE_1 = "1";
	/**
	 * 收藏类型1、商品
	 */
	String COLLECT_TYPE_1 = "1";
	/**
	 * 订单自动取消时间（分钟）
	 */
	long ORDER_TIME_OUT_0 = 30;
	/**
	 * 秒杀订单自动取消时间（分钟）
	 */
	long ORDER_TIME_OUT_0_SECKILL = 5;
	/**
	 * 订单自动收货时间（天）
	 */
	long ORDER_TIME_OUT_2 = 7;
	/**
	 * redis订单key
	 */
	String REDIS_ORDER_KEY_IS_PAY_0 = "mall:order:is_pay_0:";
	/**
	 * redis订单key
	 */
	String REDIS_ORDER_KEY_STATUS_2 = "mall:order:status_2:";
	/**
	 * 应用类型1小程序
	 */
	String APP_TYPE_1 = "1";
	/**
	 * 支付方式1、货到付款；2、在线支付
	 */
	String PAYMENT_WAY_1 = "1";
	/**
	 * 支付方式1、货到付款；2、在线支付
	 */
	String PAYMENT_WAY_2 = "2";
	/**
	 * 评价状态0、未评；1、已评；2、已追评
	 */
	String APPRAISES_STATUS_0 = "0";
	/**
	 * 评价状态0、未评；1、已评；2、已追评
	 */
	String APPRAISES_STATUS_1 = "1";
	/**
	 * 评价状态0、未评；1、已评；2、已追评
	 */
	String APPRAISES_STATUS_2 = "2";
	/**
	 * 用户等级（0：普通用户，1：普通会员）
	 */
	Integer USER_GRADE_0 = 0;
	/**
	 * 用户等级（0：普通用户，1：普通会员）
	 */
	Integer USER_GRADE_1 = 1;
	/**
	 * 记录类型0、用户初始化；1、会员初始化；2、商品赠送；3、退款赠送减回；4、商品抵扣；5、订单取消抵扣加回；6、退款抵扣加回
	 */
	String POINTS_RECORD_TYPE_0 = "0";
	String POINTS_RECORD_TYPE_1 = "1";
	String POINTS_RECORD_TYPE_2 = "2";
	String POINTS_RECORD_TYPE_3 = "3";
	String POINTS_RECORD_TYPE_4 = "4";
	String POINTS_RECORD_TYPE_5 = "5";
	String POINTS_RECORD_TYPE_6 = "6";

	/**
	 * 电子券适用类型1、全部商品；2、指定商品可用；
	 */
	String COUPON_SUIT_TYPE_1 = "1";
	String COUPON_SUIT_TYPE_2 = "2";

	/**
	 * 到期类型1、领券后生效；2：固定时间段
	 */
	String COUPON_EXPIRE_TYPE_1 = "1";
	String COUPON_EXPIRE_TYPE_2 = "2";

	/**
	 * 用户电子券状态0、未使用；1、已使用；2、已过期
	 */
	String COUPON_USER_STATUS_0 = "0";
	String COUPON_USER_STATUS_1 = "1";
	String COUPON_USER_STATUS_2 = "2";

	/**
	 * 砍价状态（0：未开始；1：活动中；2：已过期）
	 */
	String BARGAIN_INFO_STATUS_0 = "0";
	String BARGAIN_INFO_STATUS_1 = "1";
	String BARGAIN_INFO_STATUS_2 = "2";

	/**
	 * 砍价记录状态（0：砍价中；1：完成砍价；2：已过期）
	 */
	String BARGAIN_USER_STATUS_0 = "0";
	String BARGAIN_USER_STATUS_1 = "1";
	String BARGAIN_USER_STATUS_2 = "2";

	/**
	 * 拼团状态（0：未开始；1：活动中；2：已过期）
	 */
	String GROUPON_INFO_STATUS_0 = "0";
	String GROUPON_INFO_STATUS_1 = "1";
	String GROUPON_INFO_STATUS_2 = "2";

	/**
	 * 拼团记录状态（0：拼团中；1：完成拼团；2：已过期）
	 */
	String GROUPON_USER_STATUS_0 = "0";
	String GROUPON_USER_STATUS_1 = "1";
	String GROUPON_USER_STATUS_2 = "2";

	/**
	 * 订单类型（0、普通订单；1、砍价订单；2、拼团订单；3、秒杀订单）
	 */
	String ORDER_TYPE_0 = "0";
	String ORDER_TYPE_1 = "1";
	String ORDER_TYPE_2 = "2";
	String ORDER_TYPE_3 = "3";

	/**
	 * 配送方式1、普通快递；2、上门自提
	 */
	String DELIVERY_WAY_1 = "1";
	String DELIVERY_WAY_2 = "2";
}
