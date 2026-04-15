/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.enums.DesensitizedType;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.service.OrderItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城售后工作台控制器。
 * 按订单项维度输出售后数据，方便客服逐条审核退款申请。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderitem")
public class OrderItemController extends BaseController {

	private final OrderItemService orderItemService;

	/**
	 * 分页查询售后订单项。
	 *
	 * @param page 分页对象
	 * @param orderItem 查询条件
	 * @return 售后订单项分页结果
	 */
	@GetMapping("/afterSalePage")
	@PreAuthorize("@ss.hasPermi('mall:aftersale:index')")
	public AjaxResult getAfterSalePage(Page page, OrderItem orderItem) {
		IPage<OrderItem> resultPage = orderItemService.pageAfterSale(page, orderItem);
		maskOrderItemPage(resultPage);
		return AjaxResult.success(resultPage);
	}

	/**
	 * 查询售后概览统计。
	 *
	 * @param orderItem 查询条件
	 * @return 概览统计
	 */
	@GetMapping("/afterSaleSummary")
	@PreAuthorize("@ss.hasPermi('mall:aftersale:index')")
	public AjaxResult getAfterSaleSummary(OrderItem orderItem) {
		return AjaxResult.success(orderItemService.getAfterSaleSummary(orderItem));
	}

	/**
	 * 脱敏售后列表中的商城用户手机号。
	 *
	 * @param resultPage 售后分页结果
	 */
	private void maskOrderItemPage(IPage<OrderItem> resultPage) {
		if (resultPage == null || resultPage.getRecords() == null) {
			return;
		}
		resultPage.getRecords().forEach(orderItemRecord ->
				orderItemRecord.setUserMobile(maskPhone(orderItemRecord.getUserMobile())));
	}

	/**
	 * 统一按手机号规则脱敏。
	 *
	 * @param phone 原始手机号
	 * @return 脱敏后的手机号
	 */
	private String maskPhone(String phone) {
		if (StrUtil.isBlank(phone)) {
			return phone;
		}
		return DesensitizedType.PHONE.desensitizer().apply(phone);
	}
}
