/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.dto;

import lombok.Data;

/**
 * 商城前台运行时配置。
 * 这里仅返回小程序页面需要直接感知的轻量配置，避免把后台参数体系直接暴露给前端。
 *
 * @author www.joolun.com
 */
@Data
public class MallRuntimeConfigDTO {

	/**
	 * 下单时是否必须先绑定手机号。
	 */
	private boolean needMemberPhoneForOrder;
}
