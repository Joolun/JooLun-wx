/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.joolun.mall.dto.PlaceOrderDTO;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;

import java.io.Serializable;

/**
 * 商城订单
 *
 * @author JL
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

	OrderInfo getById2(Serializable id);

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
	 * 退款回调
	 * @param notifyResult
	 */
	void notifyRefunds(WxPayRefundNotifyResult notifyResult);
}
