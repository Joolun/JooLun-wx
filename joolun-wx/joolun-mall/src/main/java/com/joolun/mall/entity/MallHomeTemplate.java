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

/**
 * 商城首页装修模板实体。
 * 用于承载当前小程序首页的模板基础信息，以及组件列表。
 *
 * @author www.joolun.com
 */
@Data
@TableName("mall_home_template")
@EqualsAndHashCode(callSuper = true)
public class MallHomeTemplate extends Model<MallHomeTemplate> {
	private static final long serialVersionUID = 1L;

	/**
	 * 模板主键。
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 模板名称。
	 */
	private String templateName;

	/**
	 * 模板编码。
	 */
	private String templateCode;

	/**
	 * 是否当前启用模板：1是 0否。
	 */
	private String currentFlag;

	/**
	 * 模板状态：1启用 0停用。
	 */
	private String status;

	/**
	 * 备注。
	 */
	private String remark;

	/**
	 * 创建时间。
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间。
	 */
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除标记：0显示 1隐藏。
	 */
	private String delFlag;

	/**
	 * 模板下的组件列表。
	 * 仅用于接口返回，不直接落库。
	 */
	@TableField(exist = false)
	private List<MallHomeTemplateItem> itemList;
}
