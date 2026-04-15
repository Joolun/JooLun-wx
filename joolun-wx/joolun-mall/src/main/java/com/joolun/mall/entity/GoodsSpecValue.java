/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("mall_goods_spec_value")
@EqualsAndHashCode(callSuper = true)
/**
 * 商品规格值实体。
 *
 * 例如红色、黑色、L、XL 等具体规格值会记录在这里。
 *
 * @author www.joolun.com
 */
public class GoodsSpecValue extends Model<GoodsSpecValue> {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String specId;

	private String name;

	private String delFlag;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;
}
