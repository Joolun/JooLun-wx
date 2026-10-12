/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.web.controller.weixin;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.enums.DesensitizedType;
import com.joolun.common.utils.SecurityUtils;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderLogistics;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.service.OrderLogisticsService;
import com.joolun.mall.service.OrderOperateLogService;
import com.joolun.mall.service.WxOrderShippingSyncService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序发货同步中心。
 * 以商城订单为数据源，避免运营人员重复填写支付单号、OpenID 和物流信息。
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wxma/operations/shipping")
public class WxMaShippingCenterController extends BaseController {

	private static final int MAX_BATCH_SIZE = 100;

	private final OrderInfoService orderInfoService;
	private final OrderLogisticsService orderLogisticsService;
	private final OrderOperateLogService orderOperateLogService;
	private final WxOrderShippingSyncService wxOrderShippingSyncService;

	@GetMapping("/orders")
	@PreAuthorize("@ss.hasPermi('wxma:operations:query')")
	public AjaxResult orders(Page<OrderInfo> page, OrderInfo query) {
		IPage<OrderInfo> result = orderInfoService.pageShipping(page, query);
		if (result != null && result.getRecords() != null) {
			result.getRecords().forEach(this::maskOrder);
		}
		return AjaxResult.success(result);
	}

	@GetMapping("/orders/summary")
	@PreAuthorize("@ss.hasPermi('wxma:operations:query')")
	public AjaxResult summary(OrderInfo query) {
		return AjaxResult.success(orderInfoService.getShippingSummary(query));
	}

	@PutMapping("/orders/{id}/sync")
	@PreAuthorize("@ss.hasPermi('wxma:operations:edit')")
	public AjaxResult sync(@PathVariable String id) {
		WxOrderShippingSyncService.SyncResult result = syncOne(id);
		return result.success() ? AjaxResult.success(result.message()) : AjaxResult.error(result.message());
	}

	@PutMapping("/orders/sync")
	@PreAuthorize("@ss.hasPermi('wxma:operations:edit')")
	public AjaxResult batchSync(@RequestBody BatchSyncRequest request) {
		List<String> orderIds = request == null ? null : request.orderIds();
		if (CollUtil.isEmpty(orderIds)) {
			return AjaxResult.error("请选择需要同步的订单");
		}
		List<String> distinctIds = orderIds.stream().filter(StrUtil::isNotBlank).distinct().toList();
		if (distinctIds.isEmpty()) {
			return AjaxResult.error("请选择有效的订单");
		}
		if (distinctIds.size() > MAX_BATCH_SIZE) {
			return AjaxResult.error("单次最多同步" + MAX_BATCH_SIZE + "个订单");
		}

		int successCount = 0;
		int skippedCount = 0;
		List<Map<String, Object>> details = new ArrayList<>();
		for (String orderId : distinctIds) {
			WxOrderShippingSyncService.SyncResult result = syncOne(orderId);
			if (result.success()) {
				successCount++;
			} else if (result.skipped()) {
				skippedCount++;
			}
			Map<String, Object> detail = new LinkedHashMap<>();
			detail.put("orderId", orderId);
			detail.put("success", result.success());
			detail.put("skipped", result.skipped());
			detail.put("message", result.message());
			details.add(detail);
		}
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("totalCount", distinctIds.size());
		response.put("successCount", successCount);
		response.put("skippedCount", skippedCount);
		response.put("failedCount", distinctIds.size() - successCount - skippedCount);
		response.put("details", details);
		return AjaxResult.success(response);
	}

	@GetMapping("/orders/{id}/wechat")
	@PreAuthorize("@ss.hasPermi('wxma:operations:query')")
	public AjaxResult queryWechat(@PathVariable String id) {
		OrderInfo orderInfo = orderInfoService.getById(id);
		if (orderInfo == null) {
			return AjaxResult.error("订单不存在");
		}
		try {
			var response = wxOrderShippingSyncService.queryOfficial(orderInfo);
			if (response.getErrCode() != null && response.getErrCode() != 0) {
				return AjaxResult.error(StrUtil.blankToDefault(response.getErrMsg(), "查询微信发货状态失败"));
			}
			return AjaxResult.success(response);
		} catch (Exception e) {
			return AjaxResult.error(StrUtil.blankToDefault(e.getMessage(), "查询微信发货状态失败"));
		}
	}

	private WxOrderShippingSyncService.SyncResult syncOne(String orderId) {
		OrderInfo orderInfo = orderInfoService.getById(orderId);
		if (orderInfo == null) {
			return WxOrderShippingSyncService.SyncResult.skipped("订单不存在");
		}
		OrderLogistics logistics = StrUtil.isBlank(orderInfo.getLogisticsId())
				? null : orderLogisticsService.getById(orderInfo.getLogisticsId());
		WxOrderShippingSyncService.SyncResult result = wxOrderShippingSyncService.sync(orderInfo, logistics);
		orderOperateLogService.saveOperateLog(orderId, null,
				result.success() ? "WX_SHIPPING_SYNC" : result.skipped() ? "WX_SHIPPING_SYNC_SKIPPED" : "WX_SHIPPING_SYNC_FAILED",
				result.success() ? "同步微信发货信息" : result.skipped() ? "跳过微信发货同步" : "微信发货信息同步失败",
				result.message(), "ADMIN", String.valueOf(SecurityUtils.getUserId()), SecurityUtils.getUsername(), null);
		return result;
	}

	private void maskOrder(OrderInfo orderInfo) {
		if (orderInfo == null) {
			return;
		}
		orderInfo.setUserMobile(maskPhone(orderInfo.getUserMobile()));
		if (orderInfo.getOrderLogistics() != null) {
			orderInfo.getOrderLogistics().setTelNum(maskPhone(orderInfo.getOrderLogistics().getTelNum()));
		}
	}

	private String maskPhone(String phone) {
		return StrUtil.isBlank(phone) ? phone : DesensitizedType.PHONE.desensitizer().apply(phone);
	}

	public record BatchSyncRequest(List<String> orderIds) {
	}
}
