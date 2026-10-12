/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.ContactBean;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.OrderKeyBean;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.PayerBean;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.ShippingListBean;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.WxMaOrderShippingInfoGetRequest;
import cn.binarywang.wx.miniapp.bean.shop.request.shipping.WxMaOrderShippingInfoUploadRequest;
import cn.binarywang.wx.miniapp.bean.shop.response.WxMaOrderShippingInfoGetResponse;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.entity.OrderLogistics;
import com.joolun.mall.mapper.OrderInfoMapper;
import com.joolun.weixin.config.WxMaConfiguration;
import com.joolun.weixin.config.WxMaProperties;
import com.joolun.weixin.config.WxRuntimeConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商城发货信息同步到微信小程序交易管理服务。
 */
@Slf4j
@Service
@AllArgsConstructor
public class WxOrderShippingSyncService {
	public static final String STATUS_PENDING = "0";
	public static final String STATUS_SUCCESS = "1";
	public static final String STATUS_FAILED = "2";
	public static final String STATUS_SKIPPED = "3";
	private static final Map<String, String> WECHAT_EXPRESS_CODES = Map.ofEntries(
			Map.entry("tiantian", "TTKDEX"),
			Map.entry("huitongkuaidi", "BEST"),
			Map.entry("yunda", "YUNDA"),
			Map.entry("yuantong", "YTO"),
			Map.entry("debangwuliu", "DBL"),
			Map.entry("ems", "EMS"),
			Map.entry("shunfeng", "SF"),
			Map.entry("zhongtong", "ZTO"),
			Map.entry("shentong", "STO")
	);

	private final WxRuntimeConfigService wxRuntimeConfigService;
	private final MallUserService mallUserService;
	private final OrderItemService orderItemService;
	private final OrderInfoMapper orderInfoMapper;

	public SyncResult sync(OrderInfo orderInfo, OrderLogistics logistics) {
		if (orderInfo == null) {
			return SyncResult.skipped("订单不存在");
		}
		if (logistics == null) {
			return finish(orderInfo, SyncResult.skipped("订单物流信息不存在"));
		}
		if (!CommonConstants.YES.equals(orderInfo.getIsPay())) {
			return finish(orderInfo, SyncResult.skipped("未支付订单无需同步微信发货信息"));
		}
		if (StrUtil.isBlank(logistics.getLogistics()) || StrUtil.isBlank(logistics.getLogisticsNo())) {
			return finish(orderInfo, SyncResult.skipped("物流公司或物流单号为空"));
		}
		MallUser mallUser = mallUserService.getById(orderInfo.getUserId());
		if (mallUser == null || StrUtil.isBlank(mallUser.getOpenId())) {
			return finish(orderInfo, SyncResult.skipped("订单用户没有小程序 OpenID"));
		}

		try {
			WxMaProperties.Config config = wxRuntimeConfigService.getMaConfigs().get(0);
			WxMaService wxMaService = WxMaConfiguration.getMaService(config.getAppId());
			OrderKeyBean orderKey = new OrderKeyBean();
			if (StrUtil.isNotBlank(orderInfo.getTransactionId())) {
				orderKey.setOrderNumberType(2);
				orderKey.setTransactionId(orderInfo.getTransactionId());
			} else {
				orderKey.setOrderNumberType(1);
				orderKey.setMchId(config.getMchId());
				orderKey.setOutTradeNo(orderInfo.getOrderNo());
			}

			ShippingListBean shipping = new ShippingListBean();
			shipping.setExpressCompany(resolveWechatExpressCode(logistics.getLogistics()));
			shipping.setTrackingNo(logistics.getLogisticsNo());
			shipping.setItemDesc(buildItemDescription(orderInfo.getId()));
			if (StrUtil.isNotBlank(logistics.getTelNum())) {
				ContactBean contact = new ContactBean();
				contact.setReceiverContact(StrUtil.trim(logistics.getTelNum()));
				shipping.setContact(contact);
			}

			WxMaOrderShippingInfoUploadRequest request = new WxMaOrderShippingInfoUploadRequest();
			request.setOrderKey(orderKey);
			request.setLogisticsType(1);
			request.setDeliveryMode(1);
			request.setIsAllDelivered(Boolean.TRUE);
			request.setShippingList(List.of(shipping));
			request.setUploadTime(OffsetDateTime.now(ZoneId.of("Asia/Shanghai"))
					.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			request.setPayer(PayerBean.builder().openid(mallUser.getOpenId()).build());

			var response = wxMaService.getWxMaOrderShippingService().upload(request);
			if (response.getErrCode() != null && response.getErrCode() != 0) {
				return finish(orderInfo, SyncResult.failed(response.getErrMsg()));
			}
			return finish(orderInfo, SyncResult.succeeded());
		} catch (Exception e) {
			log.error("同步订单 {} 的微信发货信息失败", orderInfo.getOrderNo(), e);
			return finish(orderInfo, SyncResult.failed(e.getMessage()));
		}
	}

	/**
	 * 按本地订单信息查询微信官方发货状态。
	 */
	public WxMaOrderShippingInfoGetResponse queryOfficial(OrderInfo orderInfo) throws Exception {
		if (orderInfo == null) {
			throw new IllegalArgumentException("订单不存在");
		}
		WxMaProperties.Config config = wxRuntimeConfigService.getMaConfigs().get(0);
		WxMaService wxMaService = WxMaConfiguration.getMaService(config.getAppId());
		WxMaOrderShippingInfoGetRequest request = new WxMaOrderShippingInfoGetRequest();
		if (StrUtil.isNotBlank(orderInfo.getTransactionId())) {
			request.setTransactionId(orderInfo.getTransactionId());
		} else {
			request.setMerchantId(config.getMchId());
			request.setMerchantTradeNo(orderInfo.getOrderNo());
		}
		return wxMaService.getWxMaOrderShippingService().get(request);
	}

	/**
	 * 将每一次同步结果写回订单，供同步中心筛选、定位和重试。
	 */
	private SyncResult finish(OrderInfo orderInfo, SyncResult result) {
		if (orderInfo == null || StrUtil.isBlank(orderInfo.getId())) {
			return result;
		}
		String status = result.success() ? STATUS_SUCCESS : result.skipped() ? STATUS_SKIPPED : STATUS_FAILED;
		String message = result.success() ? null : StrUtil.maxLength(result.message(), 1000);
		orderInfoMapper.update(null, Wrappers.<OrderInfo>lambdaUpdate()
				.eq(OrderInfo::getId, orderInfo.getId())
				.set(OrderInfo::getWxShippingStatus, status)
				.set(OrderInfo::getWxShippingSyncTime, LocalDateTime.now())
				.set(OrderInfo::getWxShippingError, message)
				.setSql("wx_shipping_retry_count = COALESCE(wx_shipping_retry_count, 0) + 1"));
		return result;
	}

	private String buildItemDescription(String orderId) {
		String description = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
				.eq(OrderItem::getOrderId, orderId)).stream()
				.map(item -> StrUtil.blankToDefault(item.getSpuName(), "商品") + "*" + item.getQuantity())
				.collect(Collectors.joining("，"));
		return StrUtil.maxLength(StrUtil.blankToDefault(description, "商城商品"), 120);
	}

	private String resolveWechatExpressCode(String logisticsCode) {
		return WECHAT_EXPRESS_CODES.getOrDefault(StrUtil.trim(logisticsCode).toLowerCase(), logisticsCode);
	}

	public record SyncResult(boolean success, boolean skipped, String message) {
		public static SyncResult succeeded() {
			return new SyncResult(true, false, "微信发货信息同步成功");
		}

		public static SyncResult skipped(String message) {
			return new SyncResult(false, true, message);
		}

		public static SyncResult failed(String message) {
			return new SyncResult(false, false, StrUtil.blankToDefault(message, "微信发货信息同步失败"));
		}
	}
}
