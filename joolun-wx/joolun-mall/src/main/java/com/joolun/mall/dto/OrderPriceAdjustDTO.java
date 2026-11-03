/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 后台订单改价参数。
 *
 * @author www.joolun.com
 */
@Data
public class OrderPriceAdjustDTO {

	/** 订单主键。 */
	private String id;

	/** 调整后的订单应付金额，必须大于 0。 */
	private BigDecimal paymentPrice;

	/** 调整说明，便于事后追溯。 */
	private String remark;
}
