/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** 会员等级。 */
@Data
@TableName("mall_member_level")
public class MemberLevel {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String name;

	private Integer levelValue;

	private Integer minGrowth;

	/** 会员折扣百分比，100表示不打折，95表示九五折。 */
	private BigDecimal discountRate;

	/** 0停用，1启用。 */
	private String status;

	private String color;

	private String remark;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private String delFlag;
}
