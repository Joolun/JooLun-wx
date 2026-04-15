/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.OrderOperateLog;

import java.util.List;

/**
 * 商城订单操作日志服务接口。
 *
 * @author www.joolun.com
 */
public interface OrderOperateLogService extends IService<OrderOperateLog> {

	/**
	 * 记录一条订单操作日志。
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
	 */
	void saveOperateLog(String orderId, String orderItemId, String operateType, String operateTitle, String operateContent,
			String operatorType, String operatorId, String operatorName, String extraInfo);

	/**
	 * 查询订单最近操作日志。
	 *
	 * @param orderId 订单ID
	 * @param limit 条数上限
	 * @return 日志列表
	 */
	List<OrderOperateLog> listRecentByOrderId(String orderId, int limit);
}
