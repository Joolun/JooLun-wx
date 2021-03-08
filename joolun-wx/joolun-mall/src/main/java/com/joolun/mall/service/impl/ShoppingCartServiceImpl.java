/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.entity.ShoppingCart;
import com.joolun.mall.mapper.ShoppingCartMapper;
import com.joolun.mall.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 购物车
 *
 * @author JL
 * @date 2019-08-29 21:27:33
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(ShoppingCart entity) {
		ShoppingCart shoppingCart = baseMapper.selectOne(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId,entity.getUserId())
				.eq(ShoppingCart::getSpuId,entity.getSpuId()));
		if(shoppingCart != null){
			entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
			baseMapper.deleteById(shoppingCart);
			return super.save(entity);
		}else{
			return super.save(entity);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(ShoppingCart entity) {
		ShoppingCart shoppingCart = baseMapper.selectOne(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId,entity.getUserId())
				.eq(ShoppingCart::getSpuId,entity.getSpuId()));
		if(shoppingCart != null && !entity.getId().equals(shoppingCart.getId())){
			entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
			baseMapper.deleteById(shoppingCart);
			return super.updateById(entity);
		}else{
			return super.updateById(entity);
		}
	}

	@Override
	public IPage<ShoppingCart> page2(IPage<ShoppingCart> page, ShoppingCart shoppingCart) {
		return baseMapper.selectPage2(page, shoppingCart);
	}
}
