/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商城订单操作日志实体。
 * 用于记录订单创建、支付、发货、收货、取消以及售后申请、审核、退款完成等关键动作。
 *
 * @author www.joolun.com
 */
@Data
@TableName("mall_order_operate_log")
@EqualsAndHashCode(callSuper = true)
public class OrderOperateLog extends Model<OrderOperateLog> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键。
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 订单ID。
	 */
	private String orderId;

	/**
	 * 订单项ID。
	 * 售后类日志会挂到具体订单项，普通订单动作可为空。
	 */
	private String orderItemId;

	/**
	 * 操作类型。
	 * 例如：CREATE、PAY_SUCCESS、DELIVERY、RECEIVE、USER_CANCEL、ADMIN_CANCEL、
	 * REFUND_APPLY、REFUND_REJECT、REFUND_APPROVE、REFUND_SUCCESS。
	 */
	private String operateType;

	/**
	 * 操作标题。
	 */
	private String operateTitle;

	/**
	 * 操作内容。
	 */
	private String operateContent;

	/**
	 * 操作人类型。
	 * 例如：USER、ADMIN、SYSTEM。
	 */
	private String operatorType;

	/**
	 * 操作人ID。
	 */
	private String operatorId;

	/**
	 * 操作人名称。
	 */
	private String operatorName;

	/**
	 * 扩展信息。
	 */
	private String extraInfo;

	/**
	 * 创建时间。
	 */
	private LocalDateTime createTime;

	/**
	 * 逻辑删除标记。
	 */
	private String delFlag;
}
