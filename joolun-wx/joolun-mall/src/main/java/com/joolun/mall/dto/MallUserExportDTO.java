/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import com.joolun.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商城用户导出对象。
 * 只保留后台运营关心的核心字段，避免把详情页的聚合展示字段一并塞进导出文件。
 *
 * @author www.joolun.com
 */
@Data
public class MallUserExportDTO {

	/**
	 * 商城用户ID。
	 */
	@Excel(name = "商城用户ID", sort = 1)
	private String id;

	/**
	 * 会员编号。
	 */
	@Excel(name = "会员编号", sort = 2)
	private String userNo;

	/**
	 * 昵称。
	 */
	@Excel(name = "昵称", sort = 3)
	private String nickName;

	/**
	 * 真实姓名。
	 */
	@Excel(name = "真实姓名", sort = 4)
	private String realName;

	/**
	 * 手机号。
	 */
	@Excel(name = "手机号", sort = 5)
	private String mobile;

	/**
	 * 运营标签。
	 */
	@Excel(name = "运营标签", sort = 6)
	private String userTag;

	/**
	 * 会员身份文案。
	 */
	@Excel(name = "会员身份", sort = 7)
	private String memberIdentity;

	/**
	 * 入会时间。
	 */
	@Excel(name = "入会时间", sort = 8, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime memberTime;

	/**
	 * 注册来源文案。
	 */
	@Excel(name = "注册来源", sort = 9)
	private String registerSourceText;

	/**
	 * 状态文案。
	 */
	@Excel(name = "状态", sort = 10)
	private String statusText;

	/**
	 * 累计下单次数。
	 */
	@Excel(name = "累计下单", sort = 11)
	private Integer orderCount;

	/**
	 * 累计支付订单数。
	 */
	@Excel(name = "累计支付", sort = 12)
	private Integer consumeCount;

	/**
	 * 累计退款单数。
	 */
	@Excel(name = "累计退款", sort = 13)
	private Integer refundCount;

	/**
	 * 累计消费金额。
	 */
	@Excel(name = "累计消费", sort = 14, scale = 2)
	private BigDecimal consumeAmount;

	/**
	 * 注册时间。
	 */
	@Excel(name = "注册时间", sort = 15, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime registerTime;

	/**
	 * 最后登录时间。
	 */
	@Excel(name = "最后登录时间", sort = 16, dateFormat = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastLoginTime;

	/**
	 * 备注。
	 */
	@Excel(name = "备注", sort = 17, width = 28)
	private String remark;
}
