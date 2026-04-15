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
import com.joolun.common.annotation.Excel;
import com.joolun.framework.config.typehandler.ArrayStringTypeHandler;
import com.joolun.mall.dto.SpuSpecDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "mall_goods_spu", autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
/**
 * 商品 SPU 聚合实体。
 *
 * 这里主要存放商品层面的展示信息，以及多规格场景下的价格区间汇总字段。
 * 具体售卖数据由 {@link GoodsSku} 承担。
 *
 * @author www.joolun.com
 */
public class GoodsSpu extends Model<GoodsSpu> {
	private static final long serialVersionUID = 1L;

	@Excel(name = "PK")
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	@Excel(name = "spuCode")
	private String spuCode;

	@Excel(name = "name")
	private String name;

	@Excel(name = "sellPoint")
	private String sellPoint;

	@Excel(name = "description")
	private String description;

	@Excel(name = "categoryFirst")
	private String categoryFirst;

	@Excel(name = "categorySecond")
	private String categorySecond;

	@Excel(name = "picUrls")
	@TableField(typeHandler = ArrayStringTypeHandler.class, jdbcType = JdbcType.VARCHAR)
	private String[] picUrls;

	@Excel(name = "shelf")
	private String shelf;

	@Excel(name = "sort")
	private Integer sort;

	@Excel(name = "priceDown")
	/** 当前 SPU 下启用 SKU 的最低销售价。 */
	private BigDecimal priceDown;

	@Excel(name = "priceUp")
	/** 当前 SPU 下启用 SKU 的最高销售价。 */
	private BigDecimal priceUp;

	@Excel(name = "saleNum")
	private Integer saleNum;

	@Excel(name = "createTime")
	private LocalDateTime createTime;

	@Excel(name = "updateTime")
	private LocalDateTime updateTime;

	@Excel(name = "specType")
	/** 规格模式：0 表示单规格，1 表示多规格。 */
	private String specType;

	@Excel(name = "delFlag")
	private String delFlag;

	@Version
	private Integer version;

	@TableField(exist = false)
	/** 商品完整 SKU 列表，管理端详情和小程序详情都会用到。 */
	private List<GoodsSku> skus;

	@TableField(exist = false)
	/** 当前商品绑定的规格组定义，供管理端编辑和详情回显。 */
	private List<SpuSpecDTO> spuSpec;

	@TableField(exist = false)
	/** 兼容旧结构的销售价字段，通常由默认 SKU 或选中 SKU 回填。 */
	private BigDecimal salesPrice;

	@TableField(exist = false)
	/** 兼容旧结构的市场价字段，通常由默认 SKU 或选中 SKU 回填。 */
	private BigDecimal marketPrice;

	@TableField(exist = false)
	/** 兼容旧结构的成本价字段，通常由默认 SKU 或选中 SKU 回填。 */
	private BigDecimal costPrice;

	@TableField(exist = false)
	/** 兼容旧结构的库存字段，通常由默认 SKU 或选中 SKU 回填。 */
	private Integer stock;
}
