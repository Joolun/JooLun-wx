/**
 * Copyright (C) 2018-2019
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
import com.joolun.mall.config.CommonConstants;
import com.joolun.mall.enums.OrderInfoEnum;
import com.joolun.weixin.entity.WxUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商城订单
 *
 * @author JL
 * @date 2019-09-10 15:21:22
 */
@Data
@TableName("order_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "商城订单")
public class OrderInfo extends Model<OrderInfo> {
	private static final long serialVersionUID = 1L;

	/**
	 * PK
	 */
	@ApiModelProperty(value = "PK")
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**
	 * 逻辑删除标记（0：显示；1：隐藏）
	 */
	@ApiModelProperty(value = "逻辑删除标记")
	private String delFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	/**
	 * 最后更新时间
	 */
	@ApiModelProperty(value = "最后更新时间")
	private LocalDateTime updateTime;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String userId;
	/**
	 * 订单单号
	 */
	@ApiModelProperty(value = "订单单号")
	private String orderNo;
	/**
	 * 支付方式1、货到付款；2、在线支付
	 */
	@ApiModelProperty(value = "支付方式")
	private String paymentWay;
	/**
	 * 是否支付0、未支付 1、已支付
	 */
	@ApiModelProperty(value = "是否支付0、未支付 1、已支付")
	private String isPay;
	/**
	 * 订单名
	 */
	@ApiModelProperty(value = "订单名")
	private String name;
	/**
	 * 状态0、待付款 1、待发货 2、待收货 3、已完成 4、已关闭
	 */
	@ApiModelProperty(value = "状态0、待付款 1、待发货 2、待收货 3、已完成 4、已关闭")
	private String status;
	/**
	 * 运费金额
	 */
	@ApiModelProperty(value = "运费金额")
	private BigDecimal freightPrice;
	/**
	 * 销售金额
	 */
	@ApiModelProperty(value = "销售金额")
	private BigDecimal salesPrice;
	/**
	 * 支付金额（销售金额+运费金额-积分抵扣金额-电子券抵扣金额）
	 */
	@ApiModelProperty(value = "支付金额（销售金额+运费金额-积分抵扣金额-电子券抵扣金额）")
	private BigDecimal paymentPrice;
	/**
	 * 付款时间
	 */
	@ApiModelProperty(value = "付款时间")
	private LocalDateTime paymentTime;
	/**
	 * 发货时间
	 */
	@ApiModelProperty(value = "发货时间")
	private LocalDateTime deliveryTime;
	/**
	 * 收货时间
	 */
	@ApiModelProperty(value = "收货时间")
	private LocalDateTime receiverTime;
	/**
	 * 成交时间
	 */
	@ApiModelProperty(value = "成交时间")
	private LocalDateTime closingTime;
	/**
	 * 买家留言
	 */
	@ApiModelProperty(value = "买家留言")
	private String userMessage;
	/**
	 * 支付交易ID
	 */
	@ApiModelProperty(value = "支付交易ID")
	private String transactionId;
	/**
	 * 物流id
	 */
	@ApiModelProperty(value = "物流id")
	private String logisticsId;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 订单详情
	 */
	@TableField(exist = false)
	private List<OrderItem> listOrderItem;
	/**
	 * 订单状态过期时间
	 */
	@TableField(exist = false)
	private Long outTime;
	/**
	 * 状态0、待付款 1、待发货 2、待收货 3、已完成 4、已关闭
	 */
	@TableField(exist = false)
	private String statusDesc;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private LocalDateTime beginTime;

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@TableField(exist = false)
	private LocalDateTime endTime;
	/**
	 * 订单物流
	 */
	@TableField(exist = false)
	private OrderLogistics orderLogistics;
	/**
	 * 物流商家
	 */
	@TableField(exist = false)
	private String logistics;
	/**
	 * 用户信息
	 */
	@TableField(exist = false)
	private WxUser userInfo;
	/**
	 * 物流单号
	 */
	@TableField(exist = false)
	private String logisticsNo;
	public String getStatusDesc() {
		if (CommonConstants.NO.equals(this.isPay) && this.status == null) {
			return "待付款";
		}
		if (this.status == null) {
			return null;
		}
		return OrderInfoEnum.valueOf(OrderInfoEnum.STATUS_PREFIX + "_" + this.status).getDesc();
	}
}
