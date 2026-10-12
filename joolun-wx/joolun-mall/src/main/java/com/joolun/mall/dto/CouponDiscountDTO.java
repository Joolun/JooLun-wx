/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/** 已锁定优惠券的结算结果。 */
@Data
@AllArgsConstructor
public class CouponDiscountDTO {
	private String couponUserId;
	private String couponName;
	private BigDecimal discountAmount;
}
