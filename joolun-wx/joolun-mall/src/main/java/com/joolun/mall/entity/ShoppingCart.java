/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.joolun.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("mall_shopping_cart")
@EqualsAndHashCode(callSuper = true)
/**
 * 购物车明细行。
 *
 * 每一条记录都绑定一个用户和一个 SKU，
 * 同时保留轻量快照，避免商品主数据变化后购物车无法正确展示。
 *
 * @author www.joolun.com
 */
public class ShoppingCart extends Model<ShoppingCart> {
	private static final long serialVersionUID = 1L;

	@Excel(name = "PK")
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	@Excel(name = "delFlag")
	private String delFlag;

	@Excel(name = "createTime")
	private LocalDateTime createTime;

	@Excel(name = "updateTime")
	private LocalDateTime updateTime;

	@Excel(name = "userId")
	private String userId;

	@Excel(name = "spuId")
	/** 所属 SPU 主键，供商品跳转和兼容旧查询逻辑使用。 */
	private String spuId;

	@Excel(name = "skuId")
	/** 当前购物车选中的具体 SKU 主键。 */
	private String skuId;

	@Excel(name = "addPrice")
	/** 加入或修改购物车时记录的 SKU 销售价快照。 */
	private BigDecimal addPrice;

	@Excel(name = "quantity")
	private Integer quantity;

	@Excel(name = "spuName")
	private String spuName;

	@Excel(name = "picUrl")
	/** SKU 图片快照，SKU 未单独配置图片时回退到商品主图首张。 */
	private String picUrl;

	@Excel(name = "specInfo")
	/** 规格展示文案，购物车页和确认订单页直接使用。 */
	private String specInfo;

	@TableField(exist = false)
	/** 关联查询出的商品信息，页面渲染时会再补齐当前 SKU 的展示字段。 */
	private GoodsSpu goodsSpu;
}
