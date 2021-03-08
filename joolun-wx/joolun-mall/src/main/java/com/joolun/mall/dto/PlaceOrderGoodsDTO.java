/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 下单参数
 *
 * @author www.joolun.com
 * @date 2019-08-13 10:18:34
 */
@Data
@ApiModel(description = "下单参数")
public class PlaceOrderGoodsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品Id
	 */
	@ApiModelProperty(value = "商品Id")
	private String spuId;

	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer quantity;
	/**
	 * 支付金额
	 */
	@ApiModelProperty(value = "支付金额")
	private BigDecimal paymentPrice;
	/**
	 * 运费金额
	 */
	@ApiModelProperty(value = "运费金额")
	private BigDecimal freightPrice;

}
