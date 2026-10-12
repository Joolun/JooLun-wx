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

/**
 * 全场满减券模板。
 *
 * 第一版只保留商家最常用的固定有效期、满额减免模型，避免过早引入
 * 商品范围、折扣券和复杂叠加规则。
 */
@Data
@TableName("mall_coupon")
public class Coupon {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String name;

	/** 满足此商品金额后可用。 */
	private BigDecimal thresholdAmount;

	/** 固定减免金额。 */
	private BigDecimal discountAmount;

	private Integer totalQuantity;

	private Integer receivedQuantity;

	private Integer usedQuantity;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endTime;

	/** 0停用，1启用。 */
	private String status;

	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	private String delFlag;

	@Version
	private Integer version;

	/** 当前用户是否已经领取，仅用于小程序展示。 */
	@TableField(exist = false)
	private Boolean received;
}
