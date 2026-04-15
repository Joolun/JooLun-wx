/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.service.MallDashboardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城数据看板后台控制器。
 * 用于给商城后台首页提供统一的经营分析数据。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/dashboard")
public class MallDashboardController extends BaseController {

	private final MallDashboardService mallDashboardService;

	/**
	 * 查询商城数据看板。
	 * 当前默认展示近 15 天趋势，支持前端切换近 7 天、15 天、30 天口径。
	 *
	 * @param dayRange 趋势统计天数
	 * @return 看板数据
	 */
	@GetMapping("/overview")
	@PreAuthorize("@ss.hasPermi('mall:dashboard:index')")
	public AjaxResult getOverview(@RequestParam(value = "dayRange", required = false) Integer dayRange) {
		return AjaxResult.success(mallDashboardService.getDashboardData(dayRange));
	}
}
