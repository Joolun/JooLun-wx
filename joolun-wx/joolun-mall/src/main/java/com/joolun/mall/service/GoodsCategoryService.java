/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joolun.mall.entity.GoodsCategory;
import com.joolun.mall.entity.GoodsCategoryTree;

import java.util.List;

/**
 * 商品类目
 *
 * @author www.joolun.com
 * @date 2019-08-12 11:46:28
 */
public interface GoodsCategoryService extends IService<GoodsCategory> {

	/**
	 * 查询类目树
	 *
	 * @return 树
	 */
	List<GoodsCategoryTree> selectTree(GoodsCategory goodsCategory);
}
