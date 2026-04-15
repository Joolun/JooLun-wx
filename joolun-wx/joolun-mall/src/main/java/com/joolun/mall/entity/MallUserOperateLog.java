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

/**
 * 商城用户运营记录实体。
 * 用于沉淀后台对会员执行的标签、备注、状态变更以及人工跟进动作。
 *
 * @author www.joolun.com
 */
@Data
@TableName("mall_user_operate_log")
@EqualsAndHashCode(callSuper = true)
public class MallUserOperateLog extends Model<MallUserOperateLog> {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键。
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private String id;

	/**
	 * 商城用户ID。
	 */
	private String userId;

	/**
	 * 操作类型。
	 * 例如：FOLLOW_UP、TAG、REMARK、STATUS、PROFILE。
	 */
	private String operateType;

	/**
	 * 操作标题。
	 */
	private String operateTitle;

	/**
	 * 操作内容。
	 */
	private String operateContent;

	/**
	 * 操作人ID。
	 */
	private String operatorId;

	/**
	 * 操作人账号。
	 */
	private String operatorName;

	/**
	 * 扩展信息。
	 * 预留给后续记录来源页面、变更前后值摘要等场景。
	 */
	private String extraInfo;

	/**
	 * 创建时间。
	 */
	private LocalDateTime createTime;

	/**
	 * 逻辑删除标记。
	 */
	private String delFlag;
}
