/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.config.MallConfigProperties;
import com.joolun.mall.constant.MallConstants;
import com.joolun.mall.dto.PlaceOrderDTO;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.enums.OrderInfoEnum;
import com.joolun.mall.service.MallUserService;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.service.OrderItemService;
import com.joolun.mall.service.OrderOperateLogService;
import com.joolun.web.api.support.MallUserSessionService;
import com.joolun.weixin.config.WxPayConfiguration;
import com.joolun.weixin.constant.MyReturnCode;
import com.joolun.weixin.utils.LocalDateTimeUtils;
import com.joolun.weixin.utils.WxMaUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序商城订单接口。
 * 订单、支付、退款等商城业务 userId 统一使用 mall_user.id，并在接口层校验订单归属。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/orderinfo")
public class OrderInfoApi {

	private final OrderInfoService orderInfoService;
	private final OrderItemService orderItemService;
	private final MallUserService mallUserService;
	private final OrderOperateLogService orderOperateLogService;
	private final MallUserSessionService mallUserSessionService;
	private final MallConfigProperties mallConfigProperties;

	/**
	 * 分页查询当前用户订单列表。
	 *
	 * @param page 分页对象
	 * @param orderInfo 查询条件
	 * @return 订单分页数据
	 */
	@GetMapping("/page")
	public AjaxResult getOrderInfoPage(Page page, OrderInfo orderInfo) {
		orderInfo.setUserId(mallUserSessionService.getCurrentMallUserId());
		return AjaxResult.success(orderInfoService.page2(page, orderInfo));
	}

	/**
	 * 查询当前用户订单详情。
	 *
	 * @param id 订单主键
	 * @return 订单详情
	 */
	@GetMapping("/{id}")
	public AjaxResult getById(@PathVariable("id") String id) {
		OrderInfo orderInfo = orderInfoService.getById2(id);
		if (!isCurrentUserOrder(orderInfo)) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		return AjaxResult.success(orderInfo);
	}

	/**
	 * 提交订单。
	 *
	 * @param placeOrderDTO 下单参数
	 * @return 下单结果
	 */
	@PostMapping
	public AjaxResult save(@RequestBody PlaceOrderDTO placeOrderDTO) {
		try {
			placeOrderDTO.setUserId(mallUserSessionService.getCurrentMallUserId());
			placeOrderDTO.setPaymentWay(MallConstants.PAYMENT_WAY_2);
			OrderInfo orderInfo = orderInfoService.orderSub(placeOrderDTO);
			if (orderInfo == null) {
				return AjaxResult.error(MyReturnCode.ERR_70003.getCode(), MyReturnCode.ERR_70003.getMsg());
			}
			return AjaxResult.success(orderInfo);
		} catch (IllegalArgumentException e) {
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 删除当前用户订单。
	 * 只允许删除属于当前用户且已关闭、未支付的订单。
	 *
	 * @param id 订单主键
	 * @return 处理结果
	 */
	@DeleteMapping("/{id}")
	public AjaxResult removeById(@PathVariable String id) {
		OrderInfo orderInfo = orderInfoService.getById(id);
		if (!isCurrentUserOrder(orderInfo)) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if (!OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus()) || CommonConstants.YES.equals(orderInfo.getIsPay())) {
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		return AjaxResult.success(orderInfoService.removeById(id));
	}

	/**
	 * 取消当前用户订单。
	 *
	 * @param id 订单主键
	 * @return 处理结果
	 */
	@PutMapping("/cancel/{id}")
	public AjaxResult orderCancel(@PathVariable String id) {
		OrderInfo orderInfo = orderInfoService.getById(id);
		if (!isCurrentUserOrder(orderInfo)) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if (!CommonConstants.NO.equals(orderInfo.getIsPay())) {
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderCancel(orderInfo);
		saveMallUserOperateLog(orderInfo.getId(), null, "USER_CANCEL", "用户取消订单",
				"用户已取消未支付订单，订单编号 " + StrUtil.blankToDefault(orderInfo.getOrderNo(), "未记录"), orderInfo.getUserId());
		return AjaxResult.success();
	}

	/**
	 * 确认收货。
	 *
	 * @param id 订单主键
	 * @return 处理结果
	 */
	@PutMapping("/receive/{id}")
	public AjaxResult orderReceive(@PathVariable String id) {
		OrderInfo orderInfo = orderInfoService.getById(id);
		if (!isCurrentUserOrder(orderInfo)) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if (!OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())) {
			return AjaxResult.error(MyReturnCode.ERR_70001.getCode(), MyReturnCode.ERR_70001.getMsg());
		}
		orderInfoService.orderReceive(orderInfo);
		saveMallUserOperateLog(orderInfo.getId(), null, "RECEIVE", "确认收货",
				"用户已确认收货，订单编号 " + StrUtil.blankToDefault(orderInfo.getOrderNo(), "未记录"), orderInfo.getUserId());
		return AjaxResult.success();
	}

	/**
	 * 调用统一下单接口，并组装小程序支付参数。
	 *
	 * @param request 请求对象
	 * @param orderInfo 订单参数
	 * @return 支付参数
	 * @throws WxPayException 微信支付异常
	 */
	@PostMapping("/unifiedOrder")
	public AjaxResult unifiedOrder(HttpServletRequest request, @RequestBody OrderInfo orderInfo) throws WxPayException {
		String currentMallUserId = mallUserSessionService.getCurrentMallUserId();

		orderInfo = orderInfoService.getById(orderInfo.getId());
		if (!isCurrentUserOrder(orderInfo, currentMallUserId)) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		if (!CommonConstants.NO.equals(orderInfo.getIsPay())) {
			return AjaxResult.error(MyReturnCode.ERR_70004.getCode(), MyReturnCode.ERR_70004.getMsg());
		}
		if (orderInfo.getPaymentPrice().compareTo(BigDecimal.ZERO) == 0) {
			orderInfo.setPaymentTime(LocalDateTime.now());
			orderInfoService.notifyOrder(orderInfo);
			return AjaxResult.success();
		}

		String appId = WxMaUtil.getAppId(request);
		WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
		wxPayUnifiedOrderRequest.setAppid(appId);
		String body = orderInfo.getName();
		body = body.length() > 40 ? body.substring(0, 39) : body;
		wxPayUnifiedOrderRequest.setBody(body);
		wxPayUnifiedOrderRequest.setOutTradeNo(orderInfo.getOrderNo());
		wxPayUnifiedOrderRequest.setTotalFee(orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
		wxPayUnifiedOrderRequest.setTradeType("JSAPI");
		wxPayUnifiedOrderRequest.setNotifyUrl(mallConfigProperties.getNotifyHost() + "/weixin/api/ma/orderinfo/notify-order");
		wxPayUnifiedOrderRequest.setSpbillCreateIp("127.0.0.1");
		wxPayUnifiedOrderRequest.setOpenid(mallUserSessionService.getCurrentOpenId());
		WxPayService wxPayService = WxPayConfiguration.getPayService();
		return AjaxResult.success(JSONUtil.parse(wxPayService.createOrder(wxPayUnifiedOrderRequest)));
	}

	/**
	 * 支付回调。
	 *
	 * @param xmlData 回调报文
	 * @return 回调处理结果
	 * @throws WxPayException 微信支付异常
	 */
	@PostMapping("/notify-order")
	public String notifyOrder(@RequestBody String xmlData) throws WxPayException {
		log.info("支付回调:{}", xmlData);
		WxPayService wxPayService = WxPayConfiguration.getPayService();
		WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
		OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery()
				.eq(OrderInfo::getOrderNo, notifyResult.getOutTradeNo()));
		if (orderInfo != null) {
			if (orderInfo.getPaymentPrice().multiply(new BigDecimal(100)).intValue() == notifyResult.getTotalFee()) {
				String timeEnd = notifyResult.getTimeEnd();
				LocalDateTime paymentTime = LocalDateTimeUtils.parse(timeEnd);
				orderInfo.setPaymentTime(paymentTime);
				orderInfo.setTransactionId(notifyResult.getTransactionId());
				orderInfoService.notifyOrder(orderInfo);
				return WxPayNotifyResponse.success("成功");
			}
			return WxPayNotifyResponse.fail("付款金额与订单金额不一致");
		}
		return WxPayNotifyResponse.fail("无此订单");
	}

	/**
	 * 物流信息回调。
	 *
	 * @param request 请求对象
	 * @param response 响应对象
	 * @return 回调结果
	 */
	@PostMapping("/notify-logisticsr")
	public String notifyLogisticsr(HttpServletRequest request, HttpServletResponse response) {
		String param = request.getParameter("param");
		Map<String, Object> map = new HashMap<>();
		map.put("result", false);
		map.put("returnCode", "500");
		map.put("message", "保存失败");
		try {
			JSONObject jsonObject = JSONUtil.parseObj(param);
//			orderInfoService.notifyLogisticsr(logisticsId, jsonObject);
			map.put("result", true);
			map.put("returnCode", "200");
			map.put("message", "保存成功");
			response.getWriter().print(JSONUtil.parseObj(map));
		} catch (Exception e) {
			map.put("message", "保存失败" + e.getMessage());
			try {
				response.getWriter().print(JSONUtil.parseObj(map));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 统计当前用户各状态订单数量。
	 *
	 * @param orderInfo 查询条件
	 * @return 各状态订单数量
	 */
	@GetMapping("/countAll")
	public AjaxResult count(OrderInfo orderInfo) {
		orderInfo.setUserId(mallUserSessionService.getCurrentMallUserId());
		Map<String, Long> countAll = new HashMap<>();
		countAll.put(OrderInfoEnum.STATUS_0.getValue(), orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.isNull(OrderInfo::getStatus)
				.eq(OrderInfo::getIsPay, CommonConstants.NO)));

		countAll.put(OrderInfoEnum.STATUS_1.getValue(), orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_1.getValue())
				.eq(OrderInfo::getIsPay, CommonConstants.YES)));

		countAll.put(OrderInfoEnum.STATUS_2.getValue(), orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_2.getValue())
				.eq(OrderInfo::getIsPay, CommonConstants.YES)));

		countAll.put(OrderInfoEnum.STATUS_3.getValue(), orderInfoService.count(Wrappers.query(orderInfo).lambda()
				.eq(OrderInfo::getStatus, OrderInfoEnum.STATUS_3.getValue())
				.eq(OrderInfo::getIsPay, CommonConstants.YES)));
		return AjaxResult.success(countAll);
	}

	/**
	 * 发起退款申请。
	 * 申请前校验订单项是否属于当前商城用户，避免按任意订单项主键发起退款。
	 *
	 * @param orderItem 订单项参数
	 * @return 处理结果
	 */
	@PostMapping("/refunds")
	public AjaxResult saveRefunds(@RequestBody OrderItem orderItem) {
		OrderItem currentOrderItem = orderItemService.getById(orderItem.getId());
		if (currentOrderItem == null) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		OrderInfo orderInfo = orderInfoService.getById(currentOrderItem.getOrderId());
		if (!isCurrentUserOrder(orderInfo)) {
			return AjaxResult.error(MyReturnCode.ERR_70005.getCode(), MyReturnCode.ERR_70005.getMsg());
		}
		orderInfoService.saveRefunds(orderItem);
		return AjaxResult.success();
	}

	/**
	 * 退款回调。
	 *
	 * @param xmlData 回调报文
	 * @return 回调结果
	 */
	@PostMapping("/notify-refunds")
	public String notifyRefunds(@RequestBody String xmlData) {
		log.info("退款回调:{}", xmlData);
		WxPayService wxPayService = WxPayConfiguration.getPayService();
		try {
			WxPayRefundNotifyResult notifyResult = wxPayService.parseRefundNotifyResult(xmlData);
			orderInfoService.notifyRefunds(notifyResult);
			return WxPayNotifyResponse.success("成功");
		} catch (Exception e) {
			e.printStackTrace();
			return WxPayNotifyResponse.fail(e.getMessage());
		}
	}

	/**
	 * 判断订单是否属于当前商城用户。
	 *
	 * @param orderInfo 订单
	 * @return true 表示属于当前用户
	 */
	private boolean isCurrentUserOrder(OrderInfo orderInfo) {
		return isCurrentUserOrder(orderInfo, mallUserSessionService.getCurrentMallUserId());
	}

	private boolean isCurrentUserOrder(OrderInfo orderInfo, String mallUserId) {
		return orderInfo != null && mallUserId.equals(orderInfo.getUserId());
	}

	/**
	 * 记录商城用户订单操作日志。
	 *
	 * @param orderId 订单ID
	 * @param orderItemId 订单项ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 * @param mallUserId 商城用户ID
	 */
	private void saveMallUserOperateLog(String orderId, String orderItemId, String operateType, String operateTitle,
			String operateContent, String mallUserId) {
		MallUser mallUser = mallUserId == null ? null : mallUserService.getById(mallUserId);
		orderOperateLogService.saveOperateLog(orderId, orderItemId, operateType, operateTitle, operateContent,
				"USER", mallUserId == null ? "0" : mallUserId, resolveMallUserName(mallUser), null);
	}

	/**
	 * 解析商城用户展示名称。
	 *
	 * @param mallUser 商城用户
	 * @return 展示名称
	 */
	private String resolveMallUserName(MallUser mallUser) {
		if (mallUser == null) {
			return "商城用户";
		}
		if (StrUtil.isNotBlank(mallUser.getNickName())) {
			return mallUser.getNickName();
		}
		if (StrUtil.isNotBlank(mallUser.getRealName())) {
			return mallUser.getRealName();
		}
		if (StrUtil.isNotBlank(mallUser.getMobile())) {
			return mallUser.getMobile();
		}
		return "商城用户";
	}
}
