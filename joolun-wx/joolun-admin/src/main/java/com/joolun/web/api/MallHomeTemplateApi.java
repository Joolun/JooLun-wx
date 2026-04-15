/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api;

import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.service.MallHomeTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序首页装修接口。
 * 小程序首页进入后先拉当前模板，再按组件配置完成页面渲染。
 *
 * @author www.joolun.com
 */
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/home/template")
public class MallHomeTemplateApi {

	private final MallHomeTemplateService mallHomeTemplateService;

	/**
	 * 查询当前小程序首页模板。
	 *
	 * @return 首页模板
	 */
	@GetMapping("/current")
	public AjaxResult getCurrentTemplate() {
		return AjaxResult.success(mallHomeTemplateService.getMaCurrentTemplate());
	}
}
