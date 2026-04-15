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
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("mall_goods_sku")
@EqualsAndHashCode(callSuper = true)
/**
 * SPU 下可独立售卖的 SKU 记录。
 *
 * 多规格改造后，价格、库存等真实售卖数据统一维护在 SKU，
 * 不再直接存放在 mall_goods_spu 主表中。
 *
 * @author www.joolun.com
 */
public class GoodsSku extends Model<GoodsSku> {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/** 商家自定义的 SKU 编码。 */
	private String skuCode;

	/** 所属 SPU 主键。 */
	private String spuId;

	/** SKU 单独图片，可为空。 */
	private String picUrl;

	/** 当前 SKU 的实际销售价，购物车和下单都以它为准。 */
	private BigDecimal salesPrice;

	/** 划线价或市场价，用于前端展示。 */
	private BigDecimal marketPrice;

	/** 内部成本价。 */
	private BigDecimal costPrice;

	/** SKU 级别库存。 */
	private Integer stock;

	/** 启用状态，用于过滤禁用或无效的 SKU。 */
	private String enable;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	private String delFlag;

	@Version
	private Integer version;

	@TableField(exist = false)
	/** 当前 SKU 对应的规格明细列表，详情接口和管理端回显时使用。 */
	private List<GoodsSkuSpecValue> specs;

	@TableField(exist = false)
	/** 规格展示文案，例如“颜色:红色; 尺码:M”。 */
	private String name;
}
