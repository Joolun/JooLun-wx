/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.entity.OrderOperateLog;
import com.joolun.mall.mapper.OrderOperateLogMapper;
import com.joolun.mall.service.OrderOperateLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 商城订单操作日志服务实现。
 *
 * @author www.joolun.com
 */
@Service
public class OrderOperateLogServiceImpl extends ServiceImpl<OrderOperateLogMapper, OrderOperateLog> implements OrderOperateLogService {

	private static final String DEFAULT_DEL_FLAG = "0";

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOperateLog(String orderId, String orderItemId, String operateType, String operateTitle, String operateContent,
			String operatorType, String operatorId, String operatorName, String extraInfo) {
		OrderOperateLog operateLog = buildOperateLog(orderId, orderItemId, operateType, operateTitle, operateContent,
				operatorType, operatorId, operatorName, extraInfo);
		if (operateLog == null) {
			return;
		}
		save(operateLog);
	}

	@Override
	public List<OrderOperateLog> listRecentByOrderId(String orderId, int limit) {
		if (StrUtil.isBlank(orderId) || limit <= 0) {
			return Collections.emptyList();
		}
		return list(Wrappers.<OrderOperateLog>lambdaQuery()
				.eq(OrderOperateLog::getOrderId, orderId)
				.eq(OrderOperateLog::getDelFlag, DEFAULT_DEL_FLAG)
				.orderByDesc(OrderOperateLog::getCreateTime)
				.last("limit " + limit));
	}

	/**
	 * 组装订单日志实体。
	 *
	 * @param orderId 订单ID
	 * @param orderItemId 订单项ID
	 * @param operateType 操作类型
	 * @param operateTitle 操作标题
	 * @param operateContent 操作内容
	 * @param operatorType 操作人类型
	 * @param operatorId 操作人ID
	 * @param operatorName 操作人名称
	 * @param extraInfo 扩展信息
	 * @return 订单日志实体
	 */
	private OrderOperateLog buildOperateLog(String orderId, String orderItemId, String operateType, String operateTitle, String operateContent,
			String operatorType, String operatorId, String operatorName, String extraInfo) {
		if (StrUtil.isBlank(orderId) || StrUtil.isBlank(operateContent)) {
			return null;
		}
		LocalDateTime now = LocalDateTime.now();
		OrderOperateLog operateLog = new OrderOperateLog();
		operateLog.setId(IdUtil.simpleUUID());
		operateLog.setOrderId(orderId.trim());
		operateLog.setOrderItemId(StrUtil.emptyToNull(StrUtil.trim(orderItemId)));
		operateLog.setOperateType(StrUtil.blankToDefault(StrUtil.trim(operateType), "ORDER"));
		operateLog.setOperateTitle(StrUtil.blankToDefault(StrUtil.trim(operateTitle), "订单操作"));
		operateLog.setOperateContent(operateContent.trim());
		operateLog.setOperatorType(StrUtil.blankToDefault(StrUtil.trim(operatorType), "SYSTEM"));
		operateLog.setOperatorId(StrUtil.blankToDefault(StrUtil.trim(operatorId), "0"));
		operateLog.setOperatorName(StrUtil.blankToDefault(StrUtil.trim(operatorName), "system"));
		operateLog.setExtraInfo(StrUtil.emptyToNull(StrUtil.trim(extraInfo)));
		operateLog.setCreateTime(now);
		operateLog.setDelFlag(DEFAULT_DEL_FLAG);
		return operateLog;
	}
}
