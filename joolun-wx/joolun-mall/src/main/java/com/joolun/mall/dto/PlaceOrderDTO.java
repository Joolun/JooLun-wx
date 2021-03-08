/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.joolun.mall.entity.ShoppingCart;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 下单参数
 *
 * @author JL
 * @date 2019-08-13 10:18:34
 */
@Data
@ApiModel(description = "下单参数")
public class PlaceOrderDTO extends Model<ShoppingCart> {
	private static final long serialVersionUID = 1L;

	/**
	 * 支付方式1、货到付款；2、在线支付
	 */
	@ApiModelProperty(value = "支付方式1、货到付款；2、在线支付")
	private String paymentWay;
	/**
	 * 配送方式1、普通快递；2、上门自提
	 */
	@ApiModelProperty(value = "配送方式")
	private String deliveryWay;
	/**
	 * 付款方式1、微信支付
	 */
	@ApiModelProperty(value = "付款方式")
	private String paymentType;
	/**
	 * 买家留言
	 */
	@ApiModelProperty(value = "买家留言")
	private String userMessage;
	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String userId;
	/**
	 * 用户收货地址ID
	 */
	@ApiModelProperty(value = "用户收货地址ID")
	private String userAddressId;
	/**
	 * 订单类型（0、普通订单；）
	 */
	@ApiModelProperty(value = "订单类型")
	private String orderType;

	@ApiModelProperty(value = "商品")
	private List<PlaceOrderGoodsDTO> skus;
}
