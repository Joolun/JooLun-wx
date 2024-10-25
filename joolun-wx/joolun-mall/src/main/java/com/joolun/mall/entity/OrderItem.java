/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.joolun.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商城订单详情
 *
 * @author www.joolun.com
 * @date 2019-09-10 15:31:40
 */
@Data
@TableName("order_item")
@EqualsAndHashCode(callSuper = true)
public class OrderItem extends Model<OrderItem> {
	private static final long serialVersionUID = 1L;

	/**
	 * PK
	 */
	@Excel(name = "PK")
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 逻辑删除标记（0：显示；1：隐藏）
	 */
	@Excel(name = "逻辑删除标记")
	private String delFlag;
	/**
	 * 创建时间
	 */
	@Excel(name = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后更新时间
	 */
	@Excel(name = "最后更新时间")
	private LocalDateTime updateTime;
	/**
	 * 订单编号
	 */
	@Excel(name = "订单编号")
	private String orderId;
	/**
	 * 商品Id
	 */
	@Excel(name = "商品Id")
	private String spuId;
	/**
	 * 商品名
	 */
	@Excel(name = "商品名")
	private String spuName;
	/**
	 * 图片
	 */
	@Excel(name = "图片")
	private String picUrl;
	/**
	 * 商品数量
	 */
	@Excel(name = "商品数量")
	private Integer quantity;
	/**
	 * 购买单价
	 */
	@Excel(name = "购买单价")
	private BigDecimal salesPrice;
	/**
	 * 运费金额
	 */
	@Excel(name = "运费金额")
	private BigDecimal freightPrice;
	/**
	 * 支付金额（购买单价*商品数量+运费金额）
	 */
	@Excel(name = "支付金额（购买单价*商品数量+运费金额）")
	private BigDecimal paymentPrice;
	/**
	 * 备注
	 */
	@Excel(name = "备注")
	private String remark;
	/**
	 * 状态0：正常；1：退款中；2:拒绝退款；3：同意退款
	 */
	@Excel(name = "状态0：正常；1：退款中；2:拒绝退款；3：同意退款")
	private String status;
	/**
	 * 是否退款0:否 1：是
	 */
	@Excel(name = "是否退款0:否 1：是")
	private String isRefund;
}
