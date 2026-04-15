/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.api;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.ShoppingCart;
import com.joolun.mall.service.ShoppingCartService;
import com.joolun.web.api.support.MallUserSessionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序购物车接口。
 * 购物车业务中的 userId 统一使用商城用户主键，接口层负责把当前微信登录态转换成 mall_user.id。
 *
 * @author www.joolun.com
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/shoppingcart")
public class ShoppingCartApi {

	private final ShoppingCartService shoppingCartService;
	private final MallUserSessionService mallUserSessionService;

	/**
	 * 分页查询当前用户购物车。
	 *
	 * @param page 分页对象
	 * @param shoppingCart 查询条件
	 * @return 购物车分页数据
	 */
	@GetMapping("/page")
	public AjaxResult getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
		shoppingCart.setUserId(mallUserSessionService.getCurrentMallUserId());
		return AjaxResult.success(shoppingCartService.page2(page, shoppingCart));
	}

	/**
	 * 查询当前用户购物车商品数量。
	 *
	 * @param shoppingCart 查询条件
	 * @return 商品数量
	 */
	@GetMapping("/count")
	public AjaxResult getShoppingCartCount(ShoppingCart shoppingCart) {
		shoppingCart.setUserId(mallUserSessionService.getCurrentMallUserId());
		return AjaxResult.success(shoppingCartService.count(Wrappers.query(shoppingCart)));
	}

	/**
	 * 加入购物车。
	 *
	 * @param shoppingCart 购物车参数
	 * @return 处理结果
	 */
	@PostMapping
	public AjaxResult save(@RequestBody ShoppingCart shoppingCart) {
		try {
			shoppingCart.setUserId(mallUserSessionService.getCurrentMallUserId());
			return AjaxResult.success(shoppingCartService.save(shoppingCart));
		} catch (IllegalArgumentException e) {
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 修改购物车商品。
	 * 更新前会校验该购物车记录是否属于当前商城用户，避免越权修改。
	 *
	 * @param shoppingCart 购物车参数
	 * @return 处理结果
	 */
	@PutMapping
	public AjaxResult edit(@RequestBody ShoppingCart shoppingCart) {
		String currentMallUserId = mallUserSessionService.getCurrentMallUserId();
		ShoppingCart current = shoppingCartService.getById(shoppingCart.getId());
		if (current == null || !currentMallUserId.equals(current.getUserId())) {
			return AjaxResult.error("购物车记录不存在");
		}
		try {
			shoppingCart.setUserId(currentMallUserId);
			return AjaxResult.success(shoppingCartService.updateById(shoppingCart));
		} catch (IllegalArgumentException e) {
			return AjaxResult.error(e.getMessage());
		}
	}

	/**
	 * 删除当前用户购物车中的商品。
	 * 只会删除属于当前商城用户的记录，避免按任意 ID 删除别人的购物车数据。
	 *
	 * @param ids 购物车主键集合
	 * @return 处理结果
	 */
	@PostMapping("/del")
	public AjaxResult del(@RequestBody List<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return AjaxResult.success(true);
		}
		return AjaxResult.success(shoppingCartService.remove(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId, mallUserSessionService.getCurrentMallUserId())
				.in(ShoppingCart::getId, ids)));
	}
}
