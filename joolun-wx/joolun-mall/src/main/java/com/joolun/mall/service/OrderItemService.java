/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.OrderItem;

import java.util.Map;

/**
 * 商城订单详情
 *
 * @author www.joolun.com
 * @date 2019-09-10 15:31:40
 */
public interface OrderItemService extends IService<OrderItem> {

	/**
	 * 后台售后工作台分页查询。
	 *
	 * @param page 分页对象
	 * @param orderItem 查询条件
	 * @return 售后订单项分页数据
	 */
	IPage<OrderItem> pageAfterSale(IPage<OrderItem> page, OrderItem orderItem);

	/**
	 * 后台售后工作台概览统计。
	 *
	 * @param orderItem 查询条件
	 * @return 售后概览
	 */
	Map<String, Object> getAfterSaleSummary(OrderItem orderItem);
}
