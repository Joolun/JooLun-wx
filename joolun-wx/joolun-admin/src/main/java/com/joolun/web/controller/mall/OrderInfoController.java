/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.common.enums.DesensitizedType;
import com.joolun.common.utils.SecurityUtils;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.entity.OrderLogistics;
import com.joolun.mall.service.MallUserOperateLogService;
import com.joolun.mall.service.MallUserService;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.service.OrderItemService;
import com.joolun.mall.service.OrderLogisticsService;
import com.joolun.mall.service.OrderOperateLogService;
import com.joolun.weixin.constant.MyReturnCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城订单后台控制器。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/orderinfo")
public class OrderInfoController extends BaseController {

	private final OrderInfoService orderInfoService;
	private final OrderLogisticsService orderLogisticsService;
	private final OrderItemService orderItemService;
	private final MallUserService mallUserService;
	private final MallUserOperateLogService mallUserOperateLogService;
	private final OrderOperateLogService orderOperateLogService;

	/**
	 * 分页查询订单列表。
	 *
	 * @param page 分页对象
	 * @param orderInfo 查询条件
	 * @return 订单分页数据
	 */
	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:index')")
	public AjaxResult getOrderInfoPage(Page page, OrderInfo orderInfo) {
		IPage<OrderInfo> resultPage = orderInfoService.page1(page, Wrappers.query(orderInfo));
		maskOrderInfoPage(resultPage);
		return AjaxResult.success(resultPage);
	}

	/**
	 * 查询订单数量。
	 *
	 * @param orderInfo 查询条件
	 * @return 订单数量
	 */
	@GetMapping("/count")
	public AjaxResult getCount(OrderInfo orderInfo) {
		return AjaxResult.success(orderInfoService.count(Wrappers.query(orderInfo)));
	}

	/**
	 * 查询订单概览统计。
	 * 统计口径和后台列表筛选条件保持一致，用于顶部卡片展示真实全量结果。
	 *
	 * @param orderInfo 查询条件
	 * @return 订单概览统计
	 */
	@GetMapping("/summary")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:index')")
	public AjaxResult getSummary(OrderInfo orderInfo) {
		return AjaxResult.success(orderInfoService.getAdminSummary(orderInfo));
	}

	/**
	 * 根据订单主键查询订单详情。
	 * 这里会补齐物流信息和商城用户信息，便于后台详情页直接展示。
	 *
	 * @param id 订单主键
	 * @return 订单详情
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:get')")
	public AjaxResult getById(@PathVariable("id") String id) {
		OrderInfo orderInfo = orderInfoService.getById2(id);
		if (orderInfo == null) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if (orderInfo.getOrderLogistics() == null && orderInfo.getLogisticsId() != null) {
			OrderLogistics orderLogistics = orderLogisticsService.getById(orderInfo.getLogisticsId());
			orderInfo.setOrderLogistics(orderLogistics);
		}
		if (orderInfo.getUserInfo() == null && orderInfo.getUserId() != null) {
			orderInfo.setUserInfo(mallUserService.getById(orderInfo.getUserId()));
		}
		maskOrderInfo(orderInfo);
		return AjaxResult.success(orderInfo);
	}

	/**
	 * 新增订单。
	 *
	 * @param orderInfo 订单数据
	 * @return 处理结果
	 */
	@PostMapping
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:add')")
	public AjaxResult save(@RequestBody OrderInfo orderInfo) {
		return AjaxResult.success(orderInfoService.save(orderInfo));
	}

	/**
	 * 修改订单。
	 *
	 * @param orderInfo 订单数据
	 * @return 处理结果
	 */
	@PutMapping
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
	public AjaxResult updateById(@RequestBody OrderInfo orderInfo) {
		OrderInfo currentOrderInfo = orderInfo.getId() == null ? null : orderInfoService.getById(orderInfo.getId());
		boolean result = orderInfoService.updateById(orderInfo);
		if (result
				&& currentOrderInfo != null
				&& currentOrderInfo.getDeliveryTime() == null
				&& StrUtil.isNotBlank(orderInfo.getLogistics())
				&& StrUtil.isNotBlank(orderInfo.getLogisticsNo())) {
			saveAdminOperateLog(orderInfo.getId(), null, "DELIVERY", "商家发货",
					"后台已完成发货，订单编号 " + StrUtil.blankToDefault(currentOrderInfo.getOrderNo(), "未记录")
							+ "，物流公司：" + orderInfo.getLogistics()
							+ "，物流单号：" + orderInfo.getLogisticsNo());
			saveMallUserOperateLog(currentOrderInfo.getUserId(), "DELIVERY", "后台发货",
					"后台已为订单 " + StrUtil.blankToDefault(currentOrderInfo.getOrderNo(), "未记录")
							+ " 完成发货，物流公司：" + orderInfo.getLogistics()
							+ "，物流单号：" + orderInfo.getLogisticsNo(),
					buildMallUserOperateExtraInfo(currentOrderInfo, null, "ORDER_SERVICE"));
		}
		return AjaxResult.success(result);
	}

	/**
	 * 删除订单。
	 *
	 * @param id 订单主键
	 * @return 处理结果
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:del')")
	public AjaxResult removeById(@PathVariable String id) {
		return AjaxResult.success(orderInfoService.removeById(id));
	}

	/**
	 * 取消订单。
	 * 仅允许取消未支付订单。
	 *
	 * @param id 订单主键
	 * @return 处理结果
	 */
	@PutMapping("/cancel/{id}")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:edit')")
	public AjaxResult orderCancel(@PathVariable String id) {
		OrderInfo orderInfo = orderInfoService.getById(id);
		if (orderInfo == null) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if (!CommonConstants.NO.equals(orderInfo.getIsPay())) {
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderCancel(orderInfo);
		saveAdminOperateLog(orderInfo.getId(), null, "ADMIN_CANCEL", "后台取消订单",
				"后台已取消未支付订单，订单编号 " + StrUtil.blankToDefault(orderInfo.getOrderNo(), "未记录"));
		saveMallUserOperateLog(orderInfo.getUserId(), "ADMIN_CANCEL", "后台取消订单",
				"后台已取消未支付订单，订单编号 " + StrUtil.blankToDefault(orderInfo.getOrderNo(), "未记录"),
				buildMallUserOperateExtraInfo(orderInfo, null, "ORDER_SERVICE"));
		return AjaxResult.success();
	}

	/**
	 * 后台执行退款处理。
	 *
	 * @param orderItem 退款订单项
	 * @return 处理结果
	 */
	@PutMapping("/doOrderRefunds")
	@PreAuthorize("@ss.hasPermi('mall:orderinfo:edit') or @ss.hasPermi('mall:aftersale:edit')")
	public AjaxResult doOrderRefunds(@RequestBody OrderItem orderItem) {
		OrderItem currentOrderItem = orderItem == null || StrUtil.isBlank(orderItem.getId()) ? null : orderItemService.getById(orderItem.getId());
		if (currentOrderItem == null) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		OrderInfo currentOrderInfo = orderInfoService.getById(currentOrderItem.getOrderId());
		orderInfoService.doOrderRefunds(orderItem);
		OrderItem latestOrderItem = orderItemService.getById(currentOrderItem.getId());
		if (currentOrderInfo != null && latestOrderItem != null) {
			if ("3".equals(latestOrderItem.getStatus())) {
				saveMallUserOperateLog(currentOrderInfo.getUserId(), "REFUND_APPROVE", "客服审核通过退款",
						buildRefundAuditOperateContent(currentOrderInfo, latestOrderItem, "已审核通过退款"),
						buildMallUserOperateExtraInfo(currentOrderInfo, latestOrderItem, "AFTER_SALE"));
			} else if ("2".equals(latestOrderItem.getStatus())) {
				saveMallUserOperateLog(currentOrderInfo.getUserId(), "REFUND_REJECT", "客服拒绝退款申请",
						buildRefundAuditOperateContent(currentOrderInfo, latestOrderItem, "已拒绝退款申请"),
						buildMallUserOperateExtraInfo(currentOrderInfo, latestOrderItem, "AFTER_SALE"));
			}
		}
		return AjaxResult.success();
	}

	/**
	 * 组装退款审核日志内容。
	 *
	 * @param orderInfo 订单
	 * @param orderItem 订单项
	 * @param actionText 动作文案
	 * @return 日志内容
	 */
	private String buildRefundAuditOperateContent(OrderInfo orderInfo, OrderItem orderItem, String actionText) {
		String specInfo = StrUtil.isBlank(orderItem.getSpecInfo()) ? "" : "，规格：" + orderItem.getSpecInfo();
		return "订单 " + StrUtil.blankToDefault(orderInfo.getOrderNo(), "未记录")
				+ " 的商品“" + StrUtil.blankToDefault(orderItem.getSpuName(), "未命名商品") + "”"
				+ actionText
				+ "，退款金额 " + (orderItem.getPaymentPrice() == null ? "0" : orderItem.getPaymentPrice()) + " 元"
				+ specInfo
				+ "，审核备注：" + StrUtil.blankToDefault(StrUtil.trim(orderItem.getRefundAuditRemark()), "无");
	}

	/**
	 * 记录后台订单操作日志。
	 *
	 * @param orderId 订单ID
	 * @param orderItemId 订单项ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 */
	private void saveAdminOperateLog(String orderId, String orderItemId, String operateType, String operateTitle, String operateContent) {
		orderOperateLogService.saveOperateLog(orderId, orderItemId, operateType, operateTitle, operateContent,
				"ADMIN", String.valueOf(SecurityUtils.getUserId()), SecurityUtils.getUsername(), null);
	}

	/**
	 * 记录会员维度的后台服务日志。
	 * 会员详情页会直接读取这份日志，方便客服回看后台处理动作，而不必再跳订单详情翻轨迹。
	 *
	 * @param userId 商城用户ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 */
	private void saveMallUserOperateLog(String userId, String operateType, String operateTitle, String operateContent, String extraInfo) {
		if (StrUtil.isBlank(userId) || StrUtil.isBlank(operateContent)) {
			return;
		}
		mallUserOperateLogService.saveOperateLog(userId, operateType, operateTitle, operateContent,
				String.valueOf(SecurityUtils.getUserId()), SecurityUtils.getUsername(), extraInfo);
	}

	/**
	 * 组装会员运营日志扩展信息。
	 * 用于会员详情页一键定位到订单或售后工作台，不再依赖解析纯文本内容。
	 *
	 * @param orderInfo 订单
	 * @param orderItem 订单项
	 * @param scene 场景
	 * @return 扩展信息
	 */
	private String buildMallUserOperateExtraInfo(OrderInfo orderInfo, OrderItem orderItem, String scene) {
		if (orderInfo == null) {
			return null;
		}
		return JSONUtil.createObj()
				.set("scene", StrUtil.blankToDefault(scene, "ORDER_SERVICE"))
				.set("orderId", orderInfo.getId())
				.set("orderNo", StrUtil.blankToDefault(orderInfo.getOrderNo(), ""))
				.set("orderItemId", orderItem == null ? "" : StrUtil.blankToDefault(orderItem.getId(), ""))
				.set("afterSaleStatus", orderItem == null ? "" : resolveAfterSaleStatus(orderItem))
				.toString();
	}

	/**
	 * 解析售后筛选值。
	 *
	 * @param orderItem 订单项
	 * @return 售后筛选值
	 */
	/**
	 * 脱敏订单分页中的商城用户手机号。
	 *
	 * @param resultPage 订单分页结果
	 */
	private void maskOrderInfoPage(IPage<OrderInfo> resultPage) {
		if (resultPage == null || resultPage.getRecords() == null) {
			return;
		}
		resultPage.getRecords().forEach(this::maskOrderInfo);
	}

	/**
	 * 脱敏订单中的商城用户手机号。
	 *
	 * @param orderInfo 订单信息
	 */
	private void maskOrderInfo(OrderInfo orderInfo) {
		if (orderInfo == null) {
			return;
		}
		orderInfo.setUserMobile(maskPhone(orderInfo.getUserMobile()));
		if (orderInfo.getUserInfo() != null) {
			orderInfo.getUserInfo().setMobile(maskPhone(orderInfo.getUserInfo().getMobile()));
		}
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

	private String resolveAfterSaleStatus(OrderItem orderItem) {
		if (orderItem == null) {
			return "";
		}
		if (CommonConstants.YES.equals(orderItem.getIsRefund())) {
			return "4";
		}
		return StrUtil.blankToDefault(orderItem.getStatus(), "");
	}
}
