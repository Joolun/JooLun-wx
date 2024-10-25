/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.web.controller.mall;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joolun.common.core.controller.BaseController;
import com.joolun.common.core.domain.AjaxResult;
import com.joolun.mall.entity.ShoppingCart;
import com.joolun.mall.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车
 *
 * @author JL
 * @date 2019-08-29 21:27:33
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/shoppingcart")
public class ShoppingCartController extends BaseController {

	private final ShoppingCartService shoppingCartService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param shoppingCart 购物车
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@ss.hasPermi('mall:shoppingcart:index')")
	public AjaxResult getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
		return AjaxResult.success(shoppingCartService.page(page,Wrappers.query(shoppingCart)));
	}

	/**
	 * 通过id查询购物车
	 * @param id
	 * @return R
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@ss.hasPermi('mall:shoppingcart:get')")
	public AjaxResult getById(@PathVariable("id") String id){
		return AjaxResult.success(shoppingCartService.getById(id));
	}

	/**
	 * 新增购物车
	 * @param shoppingCart 购物车
	 * @return R
	 */
	@PostMapping
	@PreAuthorize("@ss.hasPermi('mall:shoppingcart:add')")
	public AjaxResult save(@RequestBody ShoppingCart shoppingCart){
		return AjaxResult.success(shoppingCartService.save(shoppingCart));
	}

	/**
	 * 修改购物车
	 * @param shoppingCart 购物车
	 * @return R
	 */
	@PutMapping
	@PreAuthorize("@ss.hasPermi('mall:shoppingcart:edit')")
	public AjaxResult updateById(@RequestBody ShoppingCart shoppingCart){
		return AjaxResult.success(shoppingCartService.updateById(shoppingCart));
	}

	/**
	 * 通过id删除购物车
	 * @param id
	 * @return R
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@ss.hasPermi('mall:shoppingcart:del')")
	public AjaxResult removeById(@PathVariable String id){
		return AjaxResult.success(shoppingCartService.removeById(id));
	}

}
