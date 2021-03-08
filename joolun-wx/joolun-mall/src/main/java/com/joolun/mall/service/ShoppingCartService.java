/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.ShoppingCart;

/**
 * 购物车
 *
 * @author JL
 * @date 2019-08-29 21:27:33
 */
public interface ShoppingCartService extends IService<ShoppingCart> {

	IPage<ShoppingCart> page2(IPage<ShoppingCart> page, ShoppingCart shoppingCart);
}
