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
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("mall_goods_sku_spec_value")
@EqualsAndHashCode(callSuper = true)
/**
 * SKU 与规格值关系实体。
 *
 * 一条记录表示某个 SKU 命中了一个规格值，
 * 多条记录组合后即可还原出完整的 SKU 规格组合。
 *
 * @author www.joolun.com
 */
public class GoodsSkuSpecValue extends Model<GoodsSkuSpecValue> {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String spuId;

	private String skuId;

	private String specValueId;

	private Integer sort;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@TableField(exist = false)
	private String specId;

	@TableField(exist = false)
	private String specName;

	@TableField(exist = false)
	private String specValueName;
}
