/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.joolun.mall.entity.GoodsSpu;
import org.apache.ibatis.annotations.Param;

/**
 * 商品 SPU Mapper。
 *
 * 多规格改造后，这里除了查询 mall_goods_spu 主表，还会在 XML 中补齐
 * SKU 聚合后的价格、库存等兼容字段，供管理端列表和小程序页面直接使用。
 *
 * @author www.joolun.com
 */
public interface GoodsSpuMapper extends BaseMapper<GoodsSpu> {

	/**
	 * 商品分页查询，返回列表所需的聚合价格和库存字段。
	 */
	IPage<GoodsSpu> selectPage1(IPage<GoodsSpu> page, @Param("query") GoodsSpu goodsSpu);

	/**
	 * 管理端详情查询，返回完整商品信息。
	 */
	GoodsSpu selectById1(String id);

	/**
	 * 小程序商品详情查询，当前实现与管理端详情共用一套聚合字段。
	 */
	GoodsSpu selectById2(String id);

	/**
	 * 购物车场景按 spuId 查询商品主信息，并补齐兼容展示字段。
	 */
	GoodsSpu selectOneByShoppingCart(String id);
}
