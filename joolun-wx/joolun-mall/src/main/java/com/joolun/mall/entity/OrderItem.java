/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.joolun.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mall_order_item")
@EqualsAndHashCode(callSuper = true)
/**
 * 订单商品快照。
 *
 * 订单创建后，会把当时选中的 SKU、价格和规格文案固化到这里，
 * 避免后续商品改价、改规格后影响历史订单展示。
 *
 * @author www.joolun.com
 */
public class OrderItem extends Model<OrderItem> {
	private static final long serialVersionUID = 1L;

	@Excel(name = "PK")
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	@Excel(name = "delFlag")
	private String delFlag;

	@Excel(name = "createTime")
	private LocalDateTime createTime;

	@Excel(name = "updateTime")
	private LocalDateTime updateTime;

	@Excel(name = "orderId")
	private String orderId;

	@Excel(name = "spuId")
	/** 购买商品所属的 SPU 主键。 */
	private String spuId;

	@Excel(name = "skuId")
	/** 本次下单选中的 SKU 主键。 */
	private String skuId;

	@Excel(name = "spuName")
	private String spuName;

	@Excel(name = "picUrl")
	private String picUrl;

	@Excel(name = "quantity")
	private Integer quantity;

	@Excel(name = "salesPrice")
	/** 提交订单时的 SKU 销售价快照。 */
	private BigDecimal salesPrice;

	@Excel(name = "freightPrice")
	private BigDecimal freightPrice;

	@Excel(name = "paymentPrice")
	/** 当前明细最终支付金额 = 销售价 * 数量 + 运费。 */
	private BigDecimal paymentPrice;

	@Excel(name = "remark")
	private String remark;

	@Excel(name = "status")
	private String status;

	@Excel(name = "isRefund")
	private String isRefund;

	@Excel(name = "specInfo")
	/** 人类可读的规格文案，例如“颜色:红色; 尺码:M”。 */
	private String specInfo;

	/**
	 * 退款申请原因。
	 * 由用户发起售后时填写，方便后台客服审核。
	 */
	private String refundReason;

	/**
	 * 退款申请时间。
	 */
	private LocalDateTime refundApplyTime;

	/**
	 * 后台审核备注。
	 * 无论同意还是拒绝，都建议记录处理意见，便于后续追溯。
	 */
	private String refundAuditRemark;

	/**
	 * 后台审核时间。
	 */
	private LocalDateTime refundAuditTime;

	/**
	 * 退款完成时间。
	 * 微信退款回调成功后写入。
	 */
	private LocalDateTime refundSuccessTime;

	/**
	 * 订单编号。
	 */
	@TableField(exist = false)
	private String orderNo;

	/**
	 * 下单会员ID。
	 */
	@TableField(exist = false)
	private String userId;

	/**
	 * 会员编号。
	 */
	@TableField(exist = false)
	private String userNo;

	/**
	 * 会员昵称。
	 */
	@TableField(exist = false)
	private String userNickName;

	/**
	 * 会员真实姓名。
	 */
	@TableField(exist = false)
	private String userRealName;

	/**
	 * 会员手机号。
	 */
	@TableField(exist = false)
	private String userMobile;

	/**
	 * 会员头像。
	 */
	@TableField(exist = false)
	private String userAvatarUrl;

	/**
	 * 订单状态。
	 */
	@TableField(exist = false)
	private String orderStatus;

	/**
	 * 订单支付状态。
	 */
	@TableField(exist = false)
	private String orderIsPay;

	/**
	 * 订单创建时间。
	 */
	@TableField(exist = false)
	private LocalDateTime orderCreateTime;

	/**
	 * 订单支付时间。
	 */
	@TableField(exist = false)
	private LocalDateTime orderPaymentTime;

	/**
	 * 订单发货时间。
	 */
	@TableField(exist = false)
	private LocalDateTime orderDeliveryTime;

	/**
	 * 订单收货时间。
	 */
	@TableField(exist = false)
	private LocalDateTime orderReceiverTime;

	/**
	 * 售后状态筛选。
	 */
	@TableField(exist = false)
	private String afterSaleStatus;

	/**
	 * 商品名称模糊搜索词。
	 */
	@TableField(exist = false)
	private String spuNameKeyword;

	/**
	 * 售后时间筛选开始值。
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private LocalDateTime beginTime;

	/**
	 * 售后时间筛选结束值。
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private LocalDateTime endTime;
}
