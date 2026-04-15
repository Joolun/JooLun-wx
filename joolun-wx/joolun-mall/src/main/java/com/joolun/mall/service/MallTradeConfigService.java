/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.joolun.mall.dto.MallRuntimeConfigDTO;

/**
 * 商城交易配置服务。
 * 商城核心模块只依赖该抽象，不直接耦合系统参数实现，便于后续替换成租户配置或独立配置中心。
 *
 * @author www.joolun.com
 */
public interface MallTradeConfigService {

	/**
	 * 获取小程序端需要的商城运行时配置。
	 *
	 * @return 商城运行时配置
	 */
	MallRuntimeConfigDTO getRuntimeConfig();

	/**
	 * 下单时是否必须先绑定手机号。
	 *
	 * @return true：必须绑定；false：不限制
	 */
	boolean needMemberPhoneForOrder();
}
