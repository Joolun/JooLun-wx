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

/**
 * 运费模板。
 *
 * <p>模板按商品 SPU 绑定，一次下单内属于同一模板的商品合并计算首件与续件，
 * 不同模板各自收取首运费。</p>
 *
 * @author www.joolun.com
 */
@Data
@TableName("mall_freight_templat")
public class FreightTemplat {

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String name;

	/** 模板类型：1 买家承担运费，2 卖家包邮。 */
	private String type;

	/** 计费方式：1 按件数，2 按重量，3 按体积。 */
	private String chargeType;

	/** 首件、首重量(kg)、首体积(m³)。 */
	private BigDecimal firstNum;

	/** 首运费。 */
	private BigDecimal firstFreight;

	/** 续件、续重量(kg)、续体积(m³)。 */
	private BigDecimal continueNum;

	/** 续运费。 */
	private BigDecimal continueFreight;

	private Integer sort;

	/** 0 停用，1 启用。 */
	private String status;

	private String remark;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private String delFlag;
}
