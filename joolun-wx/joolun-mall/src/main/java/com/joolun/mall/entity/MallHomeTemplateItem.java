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
import java.util.Map;

/**
 * 商城首页装修组件实体。
 * 每一条记录代表首页中的一个组件块，如轮播、公告、导航、活动图、商品分组。
 *
 * @author www.joolun.com
 */
@Data
@TableName("mall_home_template_item")
@EqualsAndHashCode(callSuper = true)
public class MallHomeTemplateItem extends Model<MallHomeTemplateItem> {
	private static final long serialVersionUID = 1L;

	/**
	 * 组件主键。
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 所属模板ID。
	 */
	private String templateId;

	/**
	 * 组件类型。
	 */
	private String itemType;

	/**
	 * 组件名称。
	 */
	private String itemName;

	/**
	 * 组件排序。
	 */
	private Integer itemSort;

	/**
	 * 组件状态：1启用 0停用。
	 */
	private String itemStatus;

	/**
	 * 组件内容JSON。
	 */
	private String contentJson;

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
	 * 组件内容对象。
	 * 仅用于前后端交互，保存时会序列化到 contentJson。
	 */
	@TableField(exist = false)
	private Map<String, Object> content;
}
