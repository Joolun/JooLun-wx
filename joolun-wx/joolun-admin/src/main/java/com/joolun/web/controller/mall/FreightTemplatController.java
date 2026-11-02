/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.web.controller.mall;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.FreightTemplat;
import com.joolun.mall.entity.GoodsSpu;
import com.joolun.mall.service.FreightTemplatService;
import com.joolun.mall.service.GoodsSpuService;
import com.joolun.mall.support.FreightCalculator;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 运费模板后台管理。
 *
 * <p>模板按商品 SPU 绑定，商品一级只能选一个模板；
 * 模板被商品引用时不允许删除，避免商品静默变成不计运费。</p>
 *
 * @author www.joolun.com
 */
@RestController
@AllArgsConstructor
@RequestMapping("/freighttemplat")
public class FreightTemplatController extends BaseController {

	/** 系统内置的默认包邮模板，存量商品升级后统一绑定到它。 */
	private static final String DEFAULT_TEMPLAT_ID = "freight_default_free";

	private final FreightTemplatService freightTemplatService;
	private final GoodsSpuService goodsSpuService;

	/**
	 * 分页查询运费模板。
	 *
	 * @param page 分页对象
	 * @param freightTemplat 查询条件
	 * @return 分页结果
	 */
	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('mall:freighttemplat:index')")
	public AjaxResult getPage(Page page, FreightTemplat freightTemplat) {
		return AjaxResult.success(freightTemplatService.page(page, Wrappers.<FreightTemplat>lambdaQuery()
				.eq(FreightTemplat::getDelFlag, "0")
				.eq(StrUtil.isNotBlank(freightTemplat.getType()), FreightTemplat::getType, freightTemplat.getType())
				.like(StrUtil.isNotBlank(freightTemplat.getName()), FreightTemplat::getName, freightTemplat.getName())
				.orderByAsc(FreightTemplat::getSort)
				.orderByAsc(FreightTemplat::getId)));
	}

	/**
	 * 启用中的运费模板列表，供商品表单下拉选择。
	 *
	 * @return 模板列表
	 */
	@GetMapping("/list")
	public AjaxResult getList() {
		return AjaxResult.success(freightTemplatService.listEnabled());
	}

	/**
	 * 通过 ID 查询运费模板。
	 *
	 * @param id 模板 ID
	 * @return 模板
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@ss.hasPermi('mall:freighttemplat:get')")
	public AjaxResult getById(@PathVariable("id") String id) {
		return AjaxResult.success(freightTemplatService.getById(id));
	}

	/**
	 * 新增运费模板。
	 *
	 * @param freightTemplat 模板
	 * @return 操作结果
	 */
	@PostMapping
	@PreAuthorize("@ss.hasPermi('mall:freighttemplat:add')")
	public AjaxResult save(@RequestBody FreightTemplat freightTemplat) {
		normalize(freightTemplat);
		freightTemplat.setCreateTime(LocalDateTime.now());
		freightTemplat.setUpdateTime(LocalDateTime.now());
		freightTemplat.setDelFlag("0");
		return AjaxResult.success(freightTemplatService.save(freightTemplat));
	}

	/**
	 * 修改运费模板。
	 *
	 * @param freightTemplat 模板
	 * @return 操作结果
	 */
	@PutMapping
	@PreAuthorize("@ss.hasPermi('mall:freighttemplat:edit')")
	public AjaxResult update(@RequestBody FreightTemplat freightTemplat) {
		if (StrUtil.isBlank(freightTemplat.getId())) {
			throw new IllegalArgumentException("运费模板ID不能为空");
		}
		normalize(freightTemplat);
		freightTemplat.setUpdateTime(LocalDateTime.now());
		return AjaxResult.success(freightTemplatService.updateById(freightTemplat));
	}

	/**
	 * 删除运费模板。
	 *
	 * @param id 模板 ID
	 * @return 操作结果
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@ss.hasPermi('mall:freighttemplat:del')")
	public AjaxResult removeById(@PathVariable("id") String id) {
		if (DEFAULT_TEMPLAT_ID.equals(id)) {
			return AjaxResult.error("默认包邮模板为系统内置模板，不能删除");
		}
		long usedCount = goodsSpuService.count(Wrappers.<GoodsSpu>lambdaQuery()
				.eq(GoodsSpu::getFreightTemplateId, id));
		if (usedCount > 0) {
			return AjaxResult.error("该运费模板已有 " + usedCount + " 个商品使用，请先调整商品绑定的模板");
		}
		return AjaxResult.success(freightTemplatService.removeById(id));
	}

	/**
	 * 校验并补默认值。
	 *
	 * @param freightTemplat 模板
	 */
	private void normalize(FreightTemplat freightTemplat) {
		if (StrUtil.isBlank(freightTemplat.getName())) {
			throw new IllegalArgumentException("请填写运费模板名称");
		}
		if (FreightCalculator.TEMPLAT_TYPE_FREE.equals(freightTemplat.getType())) {
			// 卖家包邮模板不带计费规则，统一清零，避免脏数据干扰后续排查。
			freightTemplat.setChargeType(FreightCalculator.CHARGE_TYPE_QUANTITY);
			freightTemplat.setFirstNum(BigDecimal.ZERO);
			freightTemplat.setFirstFreight(BigDecimal.ZERO);
			freightTemplat.setContinueNum(BigDecimal.ZERO);
			freightTemplat.setContinueFreight(BigDecimal.ZERO);
		} else if (FreightCalculator.TEMPLAT_TYPE_BUYER.equals(freightTemplat.getType())) {
			validateChargeRule(freightTemplat);
		} else {
			throw new IllegalArgumentException("请选择模板类型");
		}
		if (freightTemplat.getSort() == null) {
			freightTemplat.setSort(0);
		}
		if (StrUtil.isBlank(freightTemplat.getStatus())) {
			freightTemplat.setStatus("1");
		}
	}

	private void validateChargeRule(FreightTemplat freightTemplat) {
		String chargeType = freightTemplat.getChargeType();
		if (!FreightCalculator.CHARGE_TYPE_QUANTITY.equals(chargeType)
				&& !FreightCalculator.CHARGE_TYPE_WEIGHT.equals(chargeType)
				&& !FreightCalculator.CHARGE_TYPE_VOLUME.equals(chargeType)) {
			throw new IllegalArgumentException("请选择计费方式");
		}
		if (freightTemplat.getFirstNum() == null || freightTemplat.getFirstNum().signum() <= 0) {
			throw new IllegalArgumentException("首件、首重量或首体积必须大于0");
		}
		if (freightTemplat.getFirstFreight() == null || freightTemplat.getFirstFreight().signum() < 0) {
			throw new IllegalArgumentException("请填写正确的首运费");
		}
		if (freightTemplat.getContinueNum() == null || freightTemplat.getContinueNum().signum() <= 0) {
			throw new IllegalArgumentException("续件、续重量或续体积必须大于0");
		}
		if (freightTemplat.getContinueFreight() == null || freightTemplat.getContinueFreight().signum() < 0) {
			throw new IllegalArgumentException("请填写正确的续运费");
		}
	}
}
