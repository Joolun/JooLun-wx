/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import lombok.Data;

/**
 * 小程序商城用户资料编辑参数。
 * 这里只开放商城用户自己可维护的基础资料，不允许前端直接修改主键或统计字段。
 *
 * @author www.joolun.com
 */
@Data
public class MallUserProfileDTO {

	/**
	 * 微信昵称。
	 * 由小程序 input type="nickname" 组件取得。
	 */
	private String nickName;

	/**
	 * 真实姓名。
	 */
	private String realName;

	/**
	 * 性别。
	 * 0：未知，1：男，2：女。
	 */
	private String sex;

	/**
	 * 生日，格式为 yyyy-MM-dd。
	 */
	private String birthday;
}
