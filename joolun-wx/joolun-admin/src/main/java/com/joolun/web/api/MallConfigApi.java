/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api;

import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.service.MallTradeConfigService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序商城配置接口。
 * 当前只返回下单链路需要的轻量配置，后续如果还有前台开关可继续在这里扩展。
 *
 * @author www.joolun.com
 */
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/mall/config")
public class MallConfigApi {

	private final MallTradeConfigService mallTradeConfigService;

	/**
	 * 获取小程序商城运行时配置。
	 *
	 * @return 运行时配置
	 */
	@GetMapping
	public AjaxResult getRuntimeConfig() {
		return AjaxResult.success(mallTradeConfigService.getRuntimeConfig());
	}
}
