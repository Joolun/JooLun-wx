/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import com.joolun.common.annotation.Excel;
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
public class PlaceOrderGoodsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品Id
	 */
	@Excel(name = "商品Id")
	private String spuId;

	/**
	 * 数量
	 */
	@Excel(name = "数量")
	private Integer quantity;
	/**
	 * 支付金额
	 */
	@Excel(name = "支付金额")
	private BigDecimal paymentPrice;
	/**
	 * 运费金额
	 */
	@Excel(name = "运费金额")
	private BigDecimal freightPrice;

}
