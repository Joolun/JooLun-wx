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
@TableName("mall_goods_spec")
@EqualsAndHashCode(callSuper = true)
/**
 * 商品规格主表实体。
 *
 * 例如颜色、尺码这类规格组都会落在这里。
 *
 * @author www.joolun.com
 */
public class GoodsSpec extends Model<GoodsSpec> {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	private String name;

	private String delFlag;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@TableField(exist = false)
	private List<GoodsSpecValue> values;
}
