/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.MallHomeTemplate;
import com.joolun.mall.service.MallHomeTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城首页装修后台控制器。
 *
 * @author www.joolun.com
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mallhometemplate")
public class MallHomeTemplateController extends BaseController {

	private final MallHomeTemplateService mallHomeTemplateService;

	/**
	 * 查询当前首页模板详情。
	 *
	 * @return 模板详情
	 */
	@GetMapping("/current")
	@PreAuthorize("@ss.hasPermi('mall:hometemplate:index')")
	public AjaxResult getCurrentTemplate() {
		return AjaxResult.success(mallHomeTemplateService.getCurrentTemplateDetail());
	}

	/**
	 * 保存当前首页模板。
	 *
	 * @param mallHomeTemplate 模板数据
	 * @return 处理结果
	 */
	@PutMapping("/current")
	@PreAuthorize("@ss.hasPermi('mall:hometemplate:edit')")
	public AjaxResult saveCurrentTemplate(@RequestBody MallHomeTemplate mallHomeTemplate) {
		return AjaxResult.success(mallHomeTemplateService.saveCurrentTemplate(mallHomeTemplate));
	}

	/**
	 * 手动刷新首页装修缓存。
	 *
	 * @return 处理结果
	 */
	@PutMapping("/current/cache")
	@PreAuthorize("@ss.hasPermi('mall:hometemplate:edit')")
	public AjaxResult refreshCurrentTemplateCache() {
		mallHomeTemplateService.refreshCurrentTemplateCache();
		return AjaxResult.success("首页装修缓存刷新成功");
	}
}
