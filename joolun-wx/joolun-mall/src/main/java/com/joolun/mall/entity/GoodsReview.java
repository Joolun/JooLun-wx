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

/** 订单商品评价。 */
@Data
@TableName("mall_goods_review")
public class GoodsReview {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String orderId;

	private String orderItemId;

	private String userId;

	private String spuId;

	private String skuId;

	private String spuName;

	private String specInfo;

	private String nickName;

	private String avatarUrl;

	/** 1至5星。 */
	private Integer rating;

	private String content;

	/** 0实名展示，1匿名展示。 */
	private String anonymousFlag;

	/** 0隐藏，1显示。 */
	private String status;

	private String replyContent;

	private LocalDateTime replyTime;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private String delFlag;
}
