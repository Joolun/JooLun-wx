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
import java.util.List;

@Data
@TableName("mall_goods_spu_spec")
@EqualsAndHashCode(callSuper = true)
/**
 * SPU 与规格组关系实体。
 *
 * 用于标记某个商品当前绑定了哪些规格组，以及规格组的展示顺序。
 *
 * @author www.joolun.com
 */
public class GoodsSpuSpec extends Model<GoodsSpuSpec> {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String spuId;

	private String specId;

	private String specName;

	private Integer sort;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@TableField(exist = false)
	private List<GoodsSpecValue> values;
}
