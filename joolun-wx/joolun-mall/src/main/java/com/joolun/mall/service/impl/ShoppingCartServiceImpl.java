/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joolun.mall.entity.GoodsSku;
import com.joolun.mall.entity.GoodsSpu;
import com.joolun.mall.entity.ShoppingCart;
import com.joolun.mall.mapper.ShoppingCartMapper;
import com.joolun.mall.service.GoodsSpuService;
import com.joolun.mall.service.ShoppingCartService;
import com.joolun.mall.support.MallGoodsSkuSupport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 购物车服务。
 * 按 SKU 维度维护购物车行，业务 userId 统一表示 mall_user.id。
 *
 * @author www.joolun.com
 */
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

	private final GoodsSpuService goodsSpuService;
	private final MallGoodsSkuSupport mallGoodsSkuSupport;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(ShoppingCart entity) {
		// 保存逻辑等价于按 userId + skuId 做 upsert。
		// 同一用户重复加入同一 SKU 时，只叠加数量，不新增重复行。
		fillSkuSnapshot(entity);
		ShoppingCart shoppingCart = baseMapper.selectOne(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId, entity.getUserId())
				.eq(ShoppingCart::getSkuId, entity.getSkuId()));
		if (shoppingCart != null) {
			entity.setId(shoppingCart.getId());
			entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
			return super.updateById(entity);
		}
		return super.save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(ShoppingCart entity) {
		// 商品页切换规格时 skuId 可能变化。
		// 如果目标 skuId 已存在购物车记录，则把数量合并到同一行。
		fillSkuSnapshot(entity);
		ShoppingCart shoppingCart = baseMapper.selectOne(Wrappers.<ShoppingCart>lambdaQuery()
				.eq(ShoppingCart::getUserId, entity.getUserId())
				.eq(ShoppingCart::getSkuId, entity.getSkuId()));
		if (shoppingCart != null && !entity.getId().equals(shoppingCart.getId())) {
			entity.setQuantity(entity.getQuantity() + shoppingCart.getQuantity());
			baseMapper.deleteById(shoppingCart.getId());
		}
		return super.updateById(entity);
	}

	@Override
	public IPage<ShoppingCart> page2(IPage<ShoppingCart> page, ShoppingCart shoppingCart) {
		// 保持原有页面返回结构，但把 goodsSpu 上的临时价格、库存补成当前 SKU 的值，
		// 这样旧页面代码仍可直接读取 goodsSpu.salesPrice / stock。
		IPage<ShoppingCart> result = baseMapper.selectPage2(page, shoppingCart);
		result.getRecords().forEach(this::fillPageSkuData);
		return result;
	}

	/**
	 * 把购物车行整理成稳定的 SKU 快照。
	 * skuId、价格、图片、规格文案、spuId 都在这里统一补齐。
	 *
	 * @param entity 购物车实体
	 */
	private void fillSkuSnapshot(ShoppingCart entity) {
		if (StrUtil.isBlank(entity.getSkuId()) && StrUtil.isNotBlank(entity.getId())) {
			ShoppingCart current = getById(entity.getId());
			if (current != null) {
				if (StrUtil.isBlank(entity.getSpuId())) {
					entity.setSpuId(current.getSpuId());
				}
				entity.setSkuId(current.getSkuId());
			}
		}
		GoodsSku goodsSku = mallGoodsSkuSupport.resolveSku(entity.getSpuId(), entity.getSkuId(), true);
		if (goodsSku == null) {
			throw new IllegalArgumentException("规格商品不存在");
		}
		GoodsSpu goodsSpu = goodsSpuService.getById(goodsSku.getSpuId());
		if (goodsSpu == null) {
			throw new IllegalArgumentException("商品不存在");
		}
		entity.setSpuId(goodsSku.getSpuId());
		entity.setSkuId(goodsSku.getId());
		entity.setAddPrice(goodsSku.getSalesPrice());
		entity.setSpuName(goodsSpu.getName());
		entity.setPicUrl(StrUtil.blankToDefault(goodsSku.getPicUrl(), firstPic(goodsSpu.getPicUrls())));
		entity.setSpecInfo(mallGoodsSkuSupport.buildSpecInfo(goodsSku.getId()));
	}

	/**
	 * 兼容旧页面结构，把当前 SKU 的展示数据补到关联商品对象中。
	 *
	 * @param shoppingCart 购物车记录
	 */
	private void fillPageSkuData(ShoppingCart shoppingCart) {
		GoodsSku goodsSku = mallGoodsSkuSupport.resolveSku(shoppingCart.getSpuId(), shoppingCart.getSkuId(), false);
		if (goodsSku == null || shoppingCart.getGoodsSpu() == null) {
			return;
		}
		shoppingCart.setAddPrice(goodsSku.getSalesPrice());
		shoppingCart.setPicUrl(StrUtil.blankToDefault(goodsSku.getPicUrl(), shoppingCart.getPicUrl()));
		shoppingCart.setSpecInfo(mallGoodsSkuSupport.buildSpecInfo(goodsSku.getId()));
		shoppingCart.getGoodsSpu().setSalesPrice(goodsSku.getSalesPrice());
		shoppingCart.getGoodsSpu().setMarketPrice(goodsSku.getMarketPrice());
		shoppingCart.getGoodsSpu().setCostPrice(goodsSku.getCostPrice());
		shoppingCart.getGoodsSpu().setStock(goodsSku.getStock());
	}

	private String firstPic(String[] picUrls) {
		if (picUrls == null || picUrls.length == 0) {
			return null;
		}
		return picUrls[0];
	}
}
