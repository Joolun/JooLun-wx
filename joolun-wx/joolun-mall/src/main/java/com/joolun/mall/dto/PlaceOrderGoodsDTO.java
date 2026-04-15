/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import com.joolun.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
/**
 * 下单时的一条商品明细。
 *
 * 多规格改造后，skuId 才是真正的结算主键。
 *
 * @author www.joolun.com
 */
public class PlaceOrderGoodsDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Excel(name = "spuId")
	/** 所属 SPU id，用于展示补充和安全校验。 */
	private String spuId;

	@Excel(name = "skuId")
	/** 用户当前选中的具体 SKU。 */
	private String skuId;

	@Excel(name = "quantity")
	/** 当前 SKU 的购买数量。 */
	private Integer quantity;

	@Excel(name = "paymentPrice")
	/** 前端传来的金额快照，当前不是后端结算的最终依据。 */
	private BigDecimal paymentPrice;

	@Excel(name = "freightPrice")
	/** 前端传来的运费快照，保留是为了兼容旧的提交结构。 */
	private BigDecimal freightPrice;
}
