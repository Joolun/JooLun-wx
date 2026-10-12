/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 用户领取的优惠券。 */
@Data
@TableName("mall_coupon_user")
public class CouponUser {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String couponId;

	private String userId;

	/** 0未使用，1已锁定，2已使用。 */
	private String status;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime receiveTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime useTime;

	private String orderId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	private String delFlag;

	@Version
	private Integer version;

	@TableField(exist = false)
	private String couponName;

	@TableField(exist = false)
	private BigDecimal thresholdAmount;

	@TableField(exist = false)
	private BigDecimal discountAmount;

	@TableField(exist = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;

	@TableField(exist = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	@TableField(exist = false)
	private String description;

	@TableField(exist = false)
	private Boolean available;
}
