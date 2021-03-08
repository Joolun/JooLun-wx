/**
 * Copyright (C) 2018-2019
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
import com.joolun.weixin.utils.ThirdSessionHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 *
 * @author JL
 * @date 2019-08-29 21:27:33
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/weixin/api/ma/shoppingcart")
@Api(value = "shoppingcart", tags = "购物车API")
public class ShoppingCartApi {

    private final ShoppingCartService shoppingCartService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param shoppingCart 购物车
	 * @return
	 */
	@ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public AjaxResult getShoppingCartPage(Page page, ShoppingCart shoppingCart) {
		shoppingCart.setUserId(ThirdSessionHolder.getWxUserId());
		return AjaxResult.success(shoppingCartService.page2(page, shoppingCart));
    }

	/**
	 * 数量
	 * @param shoppingCart
	 * @return
	 */
	@ApiOperation(value = "查询数量")
	@GetMapping("/count")
	public AjaxResult getShoppingCartCount(ShoppingCart shoppingCart) {
		shoppingCart.setUserId(ThirdSessionHolder.getWxUserId());
		return AjaxResult.success(shoppingCartService.count(Wrappers.query(shoppingCart)));
	}

	/**
	 * 加入购物车
	 * @param shoppingCart
	 * @return
	 */
	@ApiOperation(value = "加入购物车")
	@PostMapping
	public AjaxResult save(@RequestBody ShoppingCart shoppingCart){
		shoppingCart.setUserId(ThirdSessionHolder.getWxUserId());
		return AjaxResult.success(shoppingCartService.save(shoppingCart));
	}

	/**
	 * 修改购物车商品
	 * @param shoppingCart
	 * @return
	 */
	@ApiOperation(value = "修改购物车商品")
	@PutMapping
	public AjaxResult edit(@RequestBody ShoppingCart shoppingCart){
		shoppingCart.setUserId(ThirdSessionHolder.getWxUserId());
		return AjaxResult.success(shoppingCartService.updateById(shoppingCart));
	}

	/**
	 * 删除购物车商品数量
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "删除购物车商品数量")
	@PostMapping("/del")
	public AjaxResult del(@RequestBody List<String> ids){
		return AjaxResult.success(shoppingCartService.removeByIds(ids));
	}
}
