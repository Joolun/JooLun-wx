/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.config.MallConfigProperties;
import com.joolun.mall.constant.MallConstants;
import com.joolun.mall.dto.PlaceOrderDTO;
import com.joolun.mall.dto.PlaceOrderGoodsDTO;
import com.joolun.mall.entity.GoodsSku;
import com.joolun.mall.entity.GoodsSpu;
import com.joolun.mall.entity.MallUser;
import com.joolun.mall.entity.OrderInfo;
import com.joolun.mall.entity.OrderItem;
import com.joolun.mall.entity.OrderLogistics;
import com.joolun.mall.entity.ShoppingCart;
import com.joolun.mall.entity.UserAddress;
import com.joolun.mall.enums.OrderInfoEnum;
import com.joolun.mall.enums.OrderLogisticsEnum;
import com.joolun.mall.mapper.OrderInfoMapper;
import com.joolun.mall.service.GoodsSkuService;
import com.joolun.mall.service.GoodsSpuService;
import com.joolun.mall.service.MallTradeConfigService;
import com.joolun.mall.service.MallUserOperateLogService;
import com.joolun.mall.service.MallUserService;
import com.joolun.mall.service.OrderInfoService;
import com.joolun.mall.service.OrderItemService;
import com.joolun.mall.service.OrderLogisticsService;
import com.joolun.mall.service.OrderOperateLogService;
import com.joolun.mall.service.ShoppingCartService;
import com.joolun.mall.service.UserAddressService;
import com.joolun.mall.support.MallGoodsSkuSupport;
import com.joolun.weixin.config.WxPayConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 订单服务实现。
 * 多规格改造后，订单提交、库存扣减、支付回调、退款回调都以 SKU 为核心处理，
 * 同时订单、购物车、地址等商城业务链路统一使用 mall_user.id。
 *
 * @author www.joolun.com
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

	private final GoodsSpuService goodsSpuService;
	private final GoodsSkuService goodsSkuService;
	private final MallUserService mallUserService;
	private final MallUserOperateLogService mallUserOperateLogService;
	private final ShoppingCartService shoppingCartService;
	private final UserAddressService userAddressService;
	private final RedisTemplate<String, String> redisTemplate;
	private final OrderItemService orderItemService;
	private final OrderLogisticsService orderLogisticsService;
	private final OrderOperateLogService orderOperateLogService;
	private final MallConfigProperties mallConfigProperties;
	private final MallGoodsSkuSupport mallGoodsSkuSupport;
	private final MallTradeConfigService mallTradeConfigService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(OrderInfo entity) {
		if (StrUtil.isNotBlank(entity.getLogistics()) && StrUtil.isNotBlank(entity.getLogisticsNo())) {
			OrderInfo dbOrderInfo = entity.getId() == null ? null : baseMapper.selectById(entity.getId());
			if (dbOrderInfo == null) {
				throw new IllegalArgumentException("订单不存在");
			}
			entity.setDeliveryTime(LocalDateTime.now());
			if (entity.getLogisticsId() == null) {
				entity.setLogisticsId(dbOrderInfo.getLogisticsId());
			}
			OrderLogistics orderLogistics = orderLogisticsService.getOne(Wrappers.<OrderLogistics>lambdaQuery()
					.eq(OrderLogistics::getId, entity.getLogisticsId()));
			if (orderLogistics == null) {
				throw new IllegalArgumentException("订单物流信息不存在");
			}
			boolean sendRedis = false;
			if (StrUtil.isBlank(orderLogistics.getLogistics()) && StrUtil.isBlank(orderLogistics.getLogisticsNo())) {
				sendRedis = true;
			}
			orderLogistics.setLogistics(entity.getLogistics());
			orderLogistics.setLogisticsNo(entity.getLogisticsNo());
			orderLogistics.setStatus(OrderLogisticsEnum.STATUS_1.getValue());
			orderLogisticsService.updateById(orderLogistics);
			if (sendRedis) {
				String keyRedis = StrUtil.format("{}:{}", MallConstants.REDIS_ORDER_KEY_STATUS_2, entity.getId());
				String orderNo = StrUtil.blankToDefault(entity.getOrderNo(), dbOrderInfo.getOrderNo());
				if (StrUtil.isNotBlank(orderNo)) {
					redisTemplate.opsForValue().set(keyRedis, orderNo, MallConstants.ORDER_TIME_OUT_2, TimeUnit.DAYS);
				}
			}
		}
		return super.updateById(entity);
	}

	@Override
	public IPage<OrderInfo> page1(IPage<OrderInfo> page, Wrapper<OrderInfo> queryWrapper) {
		IPage<OrderInfo> orderPage = baseMapper.selectPage1(page, queryWrapper.getEntity());
		fillOrderItemList(orderPage == null ? null : orderPage.getRecords());
		return orderPage;
	}

	@Override
	public IPage<OrderInfo> page2(IPage<OrderInfo> page, OrderInfo orderInfo) {
		return baseMapper.selectPage2(page, orderInfo);
	}

	@Override
	public OrderInfo getById2(Serializable id) {
		OrderInfo orderInfo = baseMapper.selectById2(id);
		if (orderInfo != null) {
			orderInfo.setOrderOperateLogList(orderOperateLogService.listRecentByOrderId(orderInfo.getId(), 100));
			String keyRedis = null;
			if (CommonConstants.NO.equals(orderInfo.getIsPay())) {
				keyRedis = StrUtil.format("{}:{}", MallConstants.REDIS_ORDER_KEY_IS_PAY_0, orderInfo.getId());
			}
			if (OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())) {
				keyRedis = StrUtil.format("{}:{}", MallConstants.REDIS_ORDER_KEY_STATUS_2, orderInfo.getId());
			}
			if (keyRedis != null) {
				Long outTime = redisTemplate.getExpire(keyRedis);
				if (outTime != null && outTime > 0) {
					orderInfo.setOutTime(outTime);
				}
			}
		}
		return orderInfo;
	}

	@Override
	public Map<String, Object> getAdminSummary(OrderInfo orderInfo) {
		return baseMapper.selectSummary(orderInfo);
	}

	/**
	 * 批量回填订单项列表。
	 * 后台订单分页如果通过 resultMap 的 collection 逐行子查询，会触发典型的 N+1 查询。
	 * 这里改为先查订单主列表，再按当前页订单ID一次性把订单项取回，显著降低列表页和汇总联动时的查询次数。
	 *
	 * @param orderInfoList 当前页订单列表
	 */
	private void fillOrderItemList(List<OrderInfo> orderInfoList) {
		if (orderInfoList == null || orderInfoList.isEmpty()) {
			return;
		}
		List<String> orderIds = orderInfoList.stream()
				.map(OrderInfo::getId)
				.filter(StrUtil::isNotBlank)
				.collect(Collectors.toList());
		if (orderIds.isEmpty()) {
			return;
		}
		Map<String, List<OrderItem>> orderItemMap = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
					.in(OrderItem::getOrderId, orderIds)
					.orderByAsc(OrderItem::getCreateTime)
					.orderByAsc(OrderItem::getId))
				.stream()
				.collect(Collectors.groupingBy(OrderItem::getOrderId, LinkedHashMap::new, Collectors.toList()));
		orderInfoList.forEach(orderInfo -> orderInfo.setListOrderItem(
				orderItemMap.getOrDefault(orderInfo.getId(), Collections.emptyList())));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void orderCancel(OrderInfo orderInfo) {
		// 未支付取消订单时，需要把提交订单阶段预扣的 SKU 库存回滚回来。
		if (CommonConstants.NO.equals(orderInfo.getIsPay()) && !OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus())) {
			orderInfo.setStatus(OrderInfoEnum.STATUS_5.getValue());
			List<OrderItem> listOrderItem = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
					.eq(OrderItem::getOrderId, orderInfo.getId()));
			listOrderItem.forEach(orderItem -> {
				GoodsSku goodsSku = goodsSkuService.getById(orderItem.getSkuId());
				if (goodsSku != null) {
					goodsSku.setStock(goodsSku.getStock() + orderItem.getQuantity());
					goodsSkuService.updateById(goodsSku);
				}
			});
			baseMapper.updateById(orderInfo);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void orderReceive(OrderInfo orderInfo) {
		orderInfo.setStatus(OrderInfoEnum.STATUS_3.getValue());
		orderInfo.setReceiverTime(LocalDateTime.now());
		baseMapper.updateById(orderInfo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		return super.removeById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OrderInfo orderSub(PlaceOrderDTO placeOrderDTO) {
		MallUser mallUser = mallUserService.getById(placeOrderDTO.getUserId());
		if (mallUser == null) {
			throw new IllegalArgumentException("商城用户不存在");
		}
		if (mallTradeConfigService.needMemberPhoneForOrder()
				&& (!"1".equals(mallUser.getMemberFlag()) || StrUtil.isBlank(mallUser.getMobile()))) {
			throw new IllegalArgumentException("请先绑定手机号后再提交订单");
		}

		// 提交流程：
		// 1. 将每一个下单项解析到具体 SKU，并校验库存。
		// 2. 按当前 SPU / SKU 状态生成订单快照。
		// 3. 扣减 SKU 库存，并删除对应购物车记录。
		OrderInfo orderInfo = new OrderInfo();
		BeanUtil.copyProperties(placeOrderDTO, orderInfo);
		orderInfo.setIsPay(CommonConstants.NO);
		orderInfo.setOrderNo(IdUtil.getSnowflake(0, 0).nextIdStr());
		orderInfo.setSalesPrice(BigDecimal.ZERO);
		orderInfo.setPaymentPrice(BigDecimal.ZERO);
		orderInfo.setFreightPrice(BigDecimal.ZERO);
		orderInfo.setCreateTime(LocalDateTime.now());

		List<OrderItem> listOrderItem = new ArrayList<>();
		List<GoodsSku> listGoodsSku = new ArrayList<>();
		for (PlaceOrderGoodsDTO orderGoods : placeOrderDTO.getSkus()) {
			// skuId 才是真正用于结算的主键。
			// spuId 主要用于校验归属关系，以及补齐商品名称等 SPU 级快照字段。
			GoodsSku goodsSku = mallGoodsSkuSupport.resolveSku(orderGoods.getSpuId(), orderGoods.getSkuId(), true);
			if (goodsSku == null || goodsSku.getStock() == null || goodsSku.getStock() < orderGoods.getQuantity()) {
				continue;
			}
			GoodsSpu goodsSpu = goodsSpuService.getOne(Wrappers.<GoodsSpu>lambdaQuery()
					.eq(GoodsSpu::getId, goodsSku.getSpuId())
					.eq(GoodsSpu::getShelf, CommonConstants.YES));
			if (goodsSpu == null) {
				continue;
			}
			OrderItem orderItem = buildOrderItem(orderGoods, goodsSku, goodsSpu);
			listOrderItem.add(orderItem);

			BigDecimal itemSalesTotal = goodsSku.getSalesPrice().multiply(BigDecimal.valueOf(orderGoods.getQuantity()));
			orderInfo.setSalesPrice(orderInfo.getSalesPrice().add(itemSalesTotal));
			orderInfo.setFreightPrice(orderInfo.getFreightPrice().add(orderItem.getFreightPrice()));
			orderInfo.setPaymentPrice(orderInfo.getPaymentPrice().add(orderItem.getPaymentPrice()));

			goodsSku.setStock(goodsSku.getStock() - orderItem.getQuantity());
			listGoodsSku.add(goodsSku);

			shoppingCartService.remove(Wrappers.<ShoppingCart>lambdaQuery()
					.eq(ShoppingCart::getSkuId, goodsSku.getId())
					.eq(ShoppingCart::getUserId, placeOrderDTO.getUserId()));
		}
		if (listOrderItem.isEmpty()) {
			return null;
		}

		UserAddress userAddress = userAddressService.getOne(Wrappers.<UserAddress>lambdaQuery()
				.eq(UserAddress::getId, placeOrderDTO.getUserAddressId())
				.eq(UserAddress::getUserId, placeOrderDTO.getUserId())
				.last("limit 1"));
		if (userAddress == null) {
			throw new IllegalArgumentException("收货地址不存在");
		}

		OrderLogistics orderLogistics = new OrderLogistics();
		orderLogistics.setPostalCode(userAddress.getPostalCode());
		orderLogistics.setUserName(userAddress.getUserName());
		orderLogistics.setTelNum(userAddress.getTelNum());
		orderLogistics.setAddress(userAddress.getProvinceName() + userAddress.getCityName() + userAddress.getCountyName() + userAddress.getDetailInfo());
		orderLogisticsService.save(orderLogistics);

		orderInfo.setLogisticsId(orderLogistics.getId());
		orderInfo.setName(listOrderItem.get(0).getSpuName());
		super.save(orderInfo);

		listOrderItem.forEach(orderItem -> orderItem.setOrderId(orderInfo.getId()));
		orderItemService.saveBatch(listOrderItem);
		listGoodsSku.forEach(goodsSkuService::updateById);
		mallUserService.recordOrder(orderInfo.getUserId());
		orderOperateLogService.saveOperateLog(orderInfo.getId(), null, "CREATE", "提交订单",
				"用户已提交订单，订单编号 " + orderInfo.getOrderNo()
						+ "，订单金额 " + defaultDecimal(orderInfo.getPaymentPrice(), BigDecimal.ZERO) + " 元，商品摘要："
						+ buildOrderItemSummary(listOrderItem),
				"USER", mallUser.getId(), resolveMallUserOperatorName(mallUser), null);

		String keyRedis = StrUtil.format("{}:{}", MallConstants.REDIS_ORDER_KEY_IS_PAY_0, orderInfo.getId());
		redisTemplate.opsForValue().set(keyRedis, orderInfo.getOrderNo(), MallConstants.ORDER_TIME_OUT_0, TimeUnit.MINUTES);
		return orderInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void notifyOrder(OrderInfo orderInfo) {
		// 支付回调只负责补记支付态和 SPU 销量。
		// SKU 库存在提交订单时已经完成扣减，这里不再重复处理。
		if (CommonConstants.NO.equals(orderInfo.getIsPay())) {
			orderInfo.setIsPay(CommonConstants.YES);
			orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
			List<OrderItem> listOrderItem = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
					.eq(OrderItem::getOrderId, orderInfo.getId()));
			Map<String, List<OrderItem>> resultList = listOrderItem.stream().collect(Collectors.groupingBy(OrderItem::getSpuId));
			List<GoodsSpu> listGoodsSpu = goodsSpuService.listByIds(resultList.keySet());
			listGoodsSpu.forEach(goodsSpu -> {
				resultList.get(goodsSpu.getId()).forEach(orderItem -> goodsSpu.setSaleNum(goodsSpu.getSaleNum() + orderItem.getQuantity()));
				goodsSpuService.updateById(goodsSpu);
			});
			baseMapper.updateById(orderInfo);
			mallUserService.recordConsume(orderInfo.getUserId(), orderInfo.getPaymentPrice());
			orderOperateLogService.saveOperateLog(orderInfo.getId(), null, "PAY_SUCCESS", "订单支付成功",
					"系统已确认支付成功，支付金额 " + defaultDecimal(orderInfo.getPaymentPrice(), BigDecimal.ZERO) + " 元，支付流水号："
							+ StrUtil.blankToDefault(orderInfo.getTransactionId(), "未记录"),
					"SYSTEM", "0", "支付回调", null);
		}
	}

	@Override
	public void saveRefunds(OrderItem orderItem) {
		OrderItem dbOrderItem = orderItemService.getById(orderItem.getId());
		if (dbOrderItem != null
				&& CommonConstants.NO.equals(dbOrderItem.getIsRefund())
				&& ("0".equals(dbOrderItem.getStatus()) || "2".equals(dbOrderItem.getStatus()))) {
			OrderInfo orderInfo = baseMapper.selectById(dbOrderItem.getOrderId());
			MallUser mallUser = orderInfo == null ? null : mallUserService.getById(orderInfo.getUserId());
			LocalDateTime now = LocalDateTime.now();
			dbOrderItem.setStatus("1");
			dbOrderItem.setRefundReason(StrUtil.blankToDefault(StrUtil.trim(orderItem.getRefundReason()), "用户申请退款"));
			dbOrderItem.setRefundApplyTime(now);
			dbOrderItem.setRefundAuditRemark(null);
			dbOrderItem.setRefundAuditTime(null);
			dbOrderItem.setRefundSuccessTime(null);
			orderItemService.updateById(dbOrderItem);
			orderOperateLogService.saveOperateLog(dbOrderItem.getOrderId(), dbOrderItem.getId(), "REFUND_APPLY", "发起退款申请",
					"用户已发起退款申请，申请金额 " + defaultDecimal(dbOrderItem.getPaymentPrice(), BigDecimal.ZERO) + " 元，申请原因："
							+ StrUtil.blankToDefault(dbOrderItem.getRefundReason(), "用户申请退款"),
					"USER", orderInfo == null ? "0" : orderInfo.getUserId(), resolveMallUserOperatorName(mallUser), null);
		}
	}

	@Override
	public void doOrderRefunds(OrderItem orderItem) {
		OrderItem orderItem2 = orderItemService.getById(orderItem.getId());
		if (orderItem2 != null) {
			OrderInfo orderInfo = baseMapper.selectById(orderItem2.getOrderId());
			LocalDateTime now = LocalDateTime.now();
			String auditRemark = StrUtil.trim(orderItem.getRefundAuditRemark());
			if ("3".equals(orderItem.getStatus())) {
				WxPayRefundRequest request = new WxPayRefundRequest();
				request.setTransactionId(orderInfo.getTransactionId());
				request.setOutRefundNo(orderItem2.getId());
				request.setTotalFee(orderItem2.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
				request.setRefundFee(orderItem2.getPaymentPrice().multiply(new BigDecimal(100)).intValue());
				request.setNotifyUrl(mallConfigProperties.getNotifyHost() + "/weixin/api/ma/orderinfo/notify-refunds");
				WxPayService wxPayService = WxPayConfiguration.getPayService();
				try {
					wxPayService.refund(request);
					orderItem2.setStatus(orderItem.getStatus());
					orderItem2.setRefundAuditRemark(StrUtil.blankToDefault(auditRemark, "后台审核通过，已发起退款"));
					orderItem2.setRefundAuditTime(now);
					orderItemService.updateById(orderItem2);
					orderOperateLogService.saveOperateLog(orderItem2.getOrderId(), orderItem2.getId(), "REFUND_APPROVE", "审核通过退款",
							"后台已审核通过退款申请，退款金额 " + defaultDecimal(orderItem2.getPaymentPrice(), BigDecimal.ZERO) + " 元，审核备注："
									+ StrUtil.blankToDefault(orderItem2.getRefundAuditRemark(), "后台审核通过，已发起退款"),
							"ADMIN", "0", "后台管理员", null);
				} catch (WxPayException e) {
					throw new RuntimeException(e.getReturnMsg() + e.getCustomErrorMsg() + e.getErrCodeDes());
				}
			} else if ("2".equals(orderItem.getStatus())) {
				orderItem2.setStatus(orderItem.getStatus());
				orderItem2.setRefundAuditRemark(StrUtil.blankToDefault(auditRemark, "后台审核拒绝退款申请"));
				orderItem2.setRefundAuditTime(now);
				orderItemService.updateById(orderItem2);
				orderOperateLogService.saveOperateLog(orderItem2.getOrderId(), orderItem2.getId(), "REFUND_REJECT", "拒绝退款申请",
						"后台已拒绝退款申请，审核备注：" + StrUtil.blankToDefault(orderItem2.getRefundAuditRemark(), "后台审核拒绝退款申请"),
						"ADMIN", "0", "后台管理员", null);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void notifyRefunds(WxPayRefundNotifyResult notifyResult) {
		OrderItem orderItem = orderItemService.getById(notifyResult.getReqInfo().getOutRefundNo());
		if (orderItem == null) {
			return;
		}
		OrderInfo orderInfo = baseMapper.selectById(orderItem.getOrderId());
		if ("3".equals(orderItem.getStatus()) && CommonConstants.NO.equals(orderItem.getIsRefund())) {
			orderItem.setIsRefund(CommonConstants.YES);
			orderItem.setRefundSuccessTime(LocalDateTime.now());
			orderInfo.setStatus(OrderInfoEnum.STATUS_5.getValue());
			orderItemService.updateById(orderItem);
			baseMapper.updateById(orderInfo);
			mallUserService.recordRefund(orderInfo.getUserId());
			orderOperateLogService.saveOperateLog(orderInfo.getId(), orderItem.getId(), "REFUND_SUCCESS", "退款成功",
					"系统已完成退款回调，退款金额 " + defaultDecimal(orderItem.getPaymentPrice(), BigDecimal.ZERO) + " 元",
					"SYSTEM", "0", "退款回调", null);
			saveMallUserAfterSaleOperateLog(orderInfo.getUserId(), "REFUND_SUCCESS", "退款到账完成",
					"订单 " + StrUtil.blankToDefault(orderInfo.getOrderNo(), "未记录")
							+ " 的退款已回调成功，退款金额 "
							+ defaultDecimal(orderItem.getPaymentPrice(), BigDecimal.ZERO)
							+ " 元，系统已确认到账。", "退款回调",
					buildMallUserOperateExtraInfo(orderInfo, orderItem));
		}
	}

	/**
	 * 生成订单项快照。
	 * 下单成功后，后续订单展示不再依赖商品当前状态，避免改价或改规格影响历史订单。
	 *
	 * @param orderGoods 下单商品
	 * @param goodsSku 当前 SKU
	 * @param goodsSpu 当前 SPU
	 * @return 订单项快照
	 */
	private OrderItem buildOrderItem(PlaceOrderGoodsDTO orderGoods, GoodsSku goodsSku, GoodsSpu goodsSpu) {
		OrderItem orderItem = new OrderItem();
		orderItem.setSpuId(goodsSpu.getId());
		orderItem.setSkuId(goodsSku.getId());
		orderItem.setSpuName(goodsSpu.getName());
		orderItem.setPicUrl(StrUtil.blankToDefault(goodsSku.getPicUrl(), firstPic(goodsSpu.getPicUrls())));
		orderItem.setQuantity(orderGoods.getQuantity());
		orderItem.setSalesPrice(goodsSku.getSalesPrice());
		orderItem.setFreightPrice(defaultDecimal(orderGoods.getFreightPrice(), BigDecimal.ZERO));
		orderItem.setPaymentPrice(goodsSku.getSalesPrice()
				.multiply(BigDecimal.valueOf(orderGoods.getQuantity()))
				.add(orderItem.getFreightPrice()));
		orderItem.setSpecInfo(mallGoodsSkuSupport.buildSpecInfo(goodsSku.getId()));
		return orderItem;
	}

	private BigDecimal defaultDecimal(BigDecimal value, BigDecimal defaultValue) {
		return value == null ? defaultValue : value;
	}

	/**
	 * 记录会员维度的售后服务日志。
	 * 退款回调属于异步系统动作，不能依赖后台控制器补日志，因此在服务层单独落一份到会员运营日志。
	 *
	 * @param userId 商城用户ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 * @param operatorName 操作人名称
	 */
	private void saveMallUserAfterSaleOperateLog(String userId, String operateType, String operateTitle,
			String operateContent, String operatorName, String extraInfo) {
		if (StrUtil.isBlank(userId) || StrUtil.isBlank(operateContent)) {
			return;
		}
		mallUserOperateLogService.saveOperateLog(userId, operateType, operateTitle, operateContent,
				"0", StrUtil.blankToDefault(StrUtil.trim(operatorName), "system"), extraInfo);
	}

	/**
	 * 组装会员服务日志扩展信息。
	 *
	 * @param orderInfo 订单
	 * @param orderItem 订单项
	 * @return 扩展信息
	 */
	private String buildMallUserOperateExtraInfo(OrderInfo orderInfo, OrderItem orderItem) {
		if (orderInfo == null) {
			return null;
		}
		return JSONUtil.createObj()
				.set("scene", "AFTER_SALE")
				.set("orderId", orderInfo.getId())
				.set("orderNo", StrUtil.blankToDefault(orderInfo.getOrderNo(), ""))
				.set("orderItemId", orderItem == null ? "" : StrUtil.blankToDefault(orderItem.getId(), ""))
				.set("afterSaleStatus", orderItem == null ? "" : "4")
				.toString();
	}

	private String firstPic(String[] picUrls) {
		if (picUrls == null || picUrls.length == 0) {
			return null;
		}
		return picUrls[0];
	}

	/**
	 * 生成订单商品摘要，避免日志内容过长。
	 *
	 * @param listOrderItem 订单项列表
	 * @return 商品摘要
	 */
	private String buildOrderItemSummary(List<OrderItem> listOrderItem) {
		if (listOrderItem == null || listOrderItem.isEmpty()) {
			return "无商品";
		}
		String summary = listOrderItem.stream()
				.map(orderItem -> orderItem.getSpuName() + " x" + orderItem.getQuantity())
				.collect(Collectors.joining("；"));
		return summary.length() > 180 ? summary.substring(0, 180) + "..." : summary;
	}

	/**
	 * 生成商城用户操作名称。
	 *
	 * @param mallUser 商城用户
	 * @return 操作者名称
	 */
	private String resolveMallUserOperatorName(MallUser mallUser) {
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
