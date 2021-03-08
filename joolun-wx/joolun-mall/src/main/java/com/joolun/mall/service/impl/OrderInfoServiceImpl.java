/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.constant.MallConstants;
import com.joolun.mall.dto.PlaceOrderDTO;
import com.joolun.mall.entity.*;
import com.joolun.mall.enums.OrderInfoEnum;
import com.joolun.mall.enums.OrderLogisticsEnum;
import com.joolun.mall.mapper.OrderInfoMapper;
import com.joolun.mall.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商城订单
 *
 * @author JL
 * @date 2019-09-10 15:21:22
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

	private final GoodsSpuService goodsSpuService;
	private final ShoppingCartService shoppingCartService;
	private final UserAddressService userAddressService;
	private final RedisTemplate<String, String> redisTemplate;

	private final OrderItemService orderItemService;
	private final OrderLogisticsService orderLogisticsService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(OrderInfo entity) {
		if(StrUtil.isNotBlank(entity.getLogistics()) && StrUtil.isNotBlank(entity.getLogisticsNo())){//发货。更新快递单号
			entity.setDeliveryTime(LocalDateTime.now());
			OrderLogistics orderLogistics = orderLogisticsService.getOne(Wrappers.<OrderLogistics>lambdaQuery()
					.eq(OrderLogistics::getId,entity.getLogisticsId()));
			//第一次发货调起收到倒计时
			boolean sendRedis = false;
			if(StrUtil.isBlank(orderLogistics.getLogistics()) && StrUtil.isBlank(orderLogistics.getLogisticsNo())){
				sendRedis = true;
			}
			orderLogistics.setLogistics(entity.getLogistics());
			orderLogistics.setLogisticsNo(entity.getLogisticsNo());
			orderLogistics.setStatus(OrderLogisticsEnum.STATUS_1.getValue());
			orderLogisticsService.updateById(orderLogistics);
			if(sendRedis){
				//加入redis，7天后自动确认收货
				String keyRedis = String.valueOf(StrUtil.format("{}:{}",MallConstants.REDIS_ORDER_KEY_STATUS_2,entity.getId()));
				redisTemplate.opsForValue().set(keyRedis, entity.getOrderNo() , MallConstants.ORDER_TIME_OUT_2, TimeUnit.DAYS);//设置过期时间
			}
		}
		return super.updateById(entity);
	}

	@Override
	public IPage<OrderInfo> page1(IPage<OrderInfo> page, Wrapper<OrderInfo> queryWrapper) {
		return baseMapper.selectPage1(page,queryWrapper.getEntity());
	}

	@Override
	public IPage<OrderInfo> page2(IPage<OrderInfo> page, OrderInfo orderInfo) {
		return baseMapper.selectPage2(page,orderInfo);
	}

	@Override
	public OrderInfo getById2(Serializable id) {
		OrderInfo orderInfo = baseMapper.selectById2(id);
		if(orderInfo != null){
			String keyRedis = null;
			//获取自动取消倒计时
			if(CommonConstants.NO.equals(orderInfo.getIsPay())){
				keyRedis = String.valueOf(StrUtil.format("{}:{}", MallConstants.REDIS_ORDER_KEY_IS_PAY_0,orderInfo.getId()));
			}
			//获取自动收货倒计时
			if(OrderInfoEnum.STATUS_2.getValue().equals(orderInfo.getStatus())){
				keyRedis = String.valueOf(StrUtil.format("{}:{}",MallConstants.REDIS_ORDER_KEY_STATUS_2,orderInfo.getId()));
			}
			if(keyRedis != null){
				Long outTime = redisTemplate.getExpire(keyRedis);
				if(outTime != null && outTime > 0){
					orderInfo.setOutTime(outTime);
				}
			}
		}
		return orderInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void orderCancel(OrderInfo orderInfo) {
		if(CommonConstants.NO.equals(orderInfo.getIsPay()) && !OrderInfoEnum.STATUS_5.getValue().equals(orderInfo.getStatus())){//校验
			orderInfo.setStatus(OrderInfoEnum.STATUS_5.getValue());
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
		OrderInfo orderInfo = new OrderInfo();
		BeanUtil.copyProperties(placeOrderDTO,orderInfo);
		orderInfo.setIsPay(CommonConstants.NO);
		orderInfo.setOrderNo(IdUtil.getSnowflake(0,0).nextIdStr());
		orderInfo.setSalesPrice(BigDecimal.ZERO);
		orderInfo.setPaymentPrice(BigDecimal.ZERO);
		orderInfo.setFreightPrice(BigDecimal.ZERO);
		orderInfo.setCreateTime(LocalDateTime.now());
		List<OrderItem> listOrderItem = new ArrayList<>();
		List<GoodsSpu> listGoodsSpu = new ArrayList<>();
		placeOrderDTO.getSkus().forEach(orderGoods -> {//过滤
			GoodsSpu goodsSpu = goodsSpuService.getOne(Wrappers.<GoodsSpu>lambdaQuery()
					.eq(GoodsSpu::getId,orderGoods.getSpuId())
					.eq(GoodsSpu::getShelf, CommonConstants.YES));
			if(goodsSpu != null){
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderId(orderInfo.getId());
				orderItem.setSpuId(goodsSpu.getId());
				orderItem.setSpuName(goodsSpu.getName());
				orderItem.setPicUrl(goodsSpu.getPicUrls()[0]);
				orderItem.setQuantity(orderGoods.getQuantity());
				orderItem.setSalesPrice(goodsSpu.getSalesPrice());
				orderItem.setFreightPrice(orderGoods.getFreightPrice());
				orderItem.setPaymentPrice(orderGoods.getPaymentPrice().add(orderItem.getFreightPrice()));
				BigDecimal quantity = new BigDecimal(orderGoods.getQuantity());
				listOrderItem.add(orderItem);
				orderInfo.setSalesPrice(orderInfo.getSalesPrice().add(goodsSpu.getSalesPrice().multiply(quantity)));
				orderInfo.setFreightPrice(orderInfo.getFreightPrice().add(orderItem.getFreightPrice()));
				orderInfo.setPaymentPrice(orderInfo.getPaymentPrice().add(orderItem.getPaymentPrice()));
				goodsSpu.setStock(goodsSpu.getStock() - orderItem.getQuantity());
				listGoodsSpu.add(goodsSpu);
				//删除购物车
				shoppingCartService.remove(Wrappers.<ShoppingCart>lambdaQuery()
						.eq(ShoppingCart::getSpuId,goodsSpu.getId())
						.eq(ShoppingCart::getUserId,placeOrderDTO.getUserId()));
			}
		});
		if(listOrderItem.size() > 0){
			UserAddress userAddress = userAddressService.getById(placeOrderDTO.getUserAddressId());
			OrderLogistics orderLogistics = new OrderLogistics();
			orderLogistics.setPostalCode(userAddress.getPostalCode());
			orderLogistics.setUserName(userAddress.getUserName());
			orderLogistics.setTelNum(userAddress.getTelNum());
			orderLogistics.setAddress(userAddress.getProvinceName()+userAddress.getCityName()+userAddress.getCountyName()+userAddress.getDetailInfo());
			//新增订单物流
			orderLogisticsService.save(orderLogistics);
			orderInfo.setLogisticsId(orderLogistics.getId());
			orderInfo.setName(listOrderItem.get(0).getSpuName());
			super.save(orderInfo);//保存订单
			listOrderItem.forEach(orderItem -> orderItem.setOrderId(orderInfo.getId()));
			//保存订单详情
			orderItemService.saveBatch(listOrderItem);
			listGoodsSpu.forEach(goodsSpuItem -> {
				goodsSpuService.updateById(goodsSpuItem);//更新库存
			});
			//订单自动取消时间
			long orderTimeOut = MallConstants.ORDER_TIME_OUT_0;
			//加入redis，30分钟自动取消
			String keyRedis = String.valueOf(StrUtil.format("{}:{}",MallConstants.REDIS_ORDER_KEY_IS_PAY_0, orderInfo.getId()));
			redisTemplate.opsForValue().set(keyRedis, orderInfo.getOrderNo() , orderTimeOut, TimeUnit.MINUTES);//设置过期时间
		}else{
			return null;
		}
		return orderInfo;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void notifyOrder(OrderInfo orderInfo) {
		if(CommonConstants.NO.equals(orderInfo.getIsPay())){//只有未支付订单能操作
			orderInfo.setIsPay(CommonConstants.YES);
			orderInfo.setStatus(OrderInfoEnum.STATUS_1.getValue());
			List<OrderItem> listOrderItem = orderItemService.list(Wrappers.<OrderItem>lambdaQuery()
					.eq(OrderItem::getOrderId,orderInfo.getId()));
			Map<String, List<OrderItem>> resultList = listOrderItem.stream().collect(Collectors.groupingBy(OrderItem::getSpuId));
			List<GoodsSpu> listGoodsSpu = goodsSpuService.listByIds(resultList.keySet());
			listGoodsSpu.forEach(goodsSpu -> {
				resultList.get(goodsSpu.getId()).forEach(orderItem -> {
					//更新销量
					goodsSpu.setSaleNum(goodsSpu.getSaleNum()+orderItem.getQuantity());
				});
				goodsSpuService.updateById(goodsSpu);
				baseMapper.updateById(orderInfo);//更新订单
			});
		}
	}

}
