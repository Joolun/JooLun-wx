/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/** 会员积分流水。积分第一版只累计和展示，不参与支付抵扣。 */
@Data
@TableName("mall_member_point_log")
public class MemberPointLog {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String userId;

	private Integer changeValue;

	private Integer balance;

	/** PAY支付奖励，REFUND退款扣回。 */
	private String sourceType;

	private String sourceId;

	private String remark;

	private LocalDateTime createTime;
}
