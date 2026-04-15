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

/**
 * 商城用户标签库实体。
 * 用于沉淀后台可维护的会员标签主数据，支撑快捷打标和标签库管理。
 *
 * @author www.joolun.com
 */
@Data
@TableName("mall_user_tag")
@EqualsAndHashCode(callSuper = true)
public class MallUserTag extends Model<MallUserTag> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键。
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 标签名称。
	 */
	private String tagName;

	/**
	 * 标签状态。
	 * 0：启用，1：停用。
	 */
	private String status;

	/**
	 * 排序值。
	 */
	private Integer sort;

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
	 * 逻辑删除标记。
	 */
	private String delFlag;

	/**
	 * 使用人数。
	 * 仅用于后台标签库展示，不落库。
	 */
	@TableField(exist = false)
	private Integer useCount;
}
