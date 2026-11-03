/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.spring.service.IService;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.joolun.mall.dto.PlaceOrderDTO;
import com.joolun.mall.dto.OrderPriceAdjustDTO;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;

import java.io.Serializable;
import java.util.Map;

/**
 * 商城订单
 *
 * @author www.joolun.com
 * @date 2019-09-10 15:21:22
 */
public interface OrderInfoService extends IService<OrderInfo> {

	IPage<OrderInfo> page1(IPage<OrderInfo> page, Wrapper<OrderInfo> queryWrapper);

	/**
	 * 下单
	 * @param placeOrderDTO
	 */
	OrderInfo orderSub(PlaceOrderDTO placeOrderDTO);

	IPage<OrderInfo> page2(IPage<OrderInfo> page, OrderInfo orderInfo);

	/**
	 * 分页查询微信发货同步中心订单。
	 */
	IPage<OrderInfo> pageShipping(IPage<OrderInfo> page, OrderInfo orderInfo);

	OrderInfo getById2(Serializable id);

	/**
	 * 查询后台订单概览统计。
	 *
	 * @param orderInfo 查询条件
	 * @return 统计结果
	 */
	Map<String, Object> getAdminSummary(OrderInfo orderInfo);

	/**
	 * 查询微信发货同步中心统计。
	 */
	Map<String, Object> getShippingSummary(OrderInfo orderInfo);

	/**
	 * 取消订单
	 * @param orderInfo
	 */
	void orderCancel(OrderInfo orderInfo);
	/**
	 * 订单收货
	 * @param orderInfo
	 */
	void orderReceive(OrderInfo orderInfo);

	/**
	 * 处理订单回调
	 * @param orderInfo
	 */
	void notifyOrder(OrderInfo orderInfo);

	/**
	 * 发起退款
	 * @param orderItem
	 */
	void saveRefunds(OrderItem orderItem);

	/**
	 * 操作退款
	 * @param orderItem
	 */
	void doOrderRefunds(OrderItem orderItem);

	/**
	 * 处理微信支付 API v3 退款回调。
	 *
	 * @param notifyResult 已验签并解密的退款结果
	 */
	void notifyRefundsV3(WxPayRefundNotifyV3Result.DecryptNotifyResult notifyResult);

	/**
	 * 后台调整订单应付金额。
	 * 仅允许未支付且未取消的订单改价，改价差额按订单项金额占比分摊回明细。
	 *
	 * @param dto 改价参数
	 * @return 改价后的订单
	 */
	OrderInfo adjustPrice(OrderPriceAdjustDTO dto);

	/**
	 * 解析本次支付使用的商户订单号，并持久化到订单。
	 * 未改价订单沿用订单号；改价过的订单使用独立支付单号，
	 * 避免微信“同一订单号下单金额必须一致”的限制。
	 *
	 * @param orderInfo 订单
	 * @return 支付单号
	 */
	String resolvePayTradeNo(OrderInfo orderInfo);
}
